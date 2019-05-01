package it.polimi.ingsw.model.map;

import com.google.gson.*;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.cards.constraints.*;
import it.polimi.ingsw.model.cards.effects.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a board
 */
public class Board {
    private ArrayList<Room> rooms= new ArrayList<Room>();
    private ArrayList<AmmoTile> ammoList= new ArrayList<AmmoTile>();
    private ArrayList<PowerUp> powerUpList= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weaponsList= new ArrayList<Weapon>();
    private Match match;
    private Gson gSon= new Gson();
    private String type;
    private BufferedReader br;
    private Random random = new Random();

    /**
     * This method generate a board from a json file and generate the deck of cards
     * @param match represent the match that need the Board
     * @param type is the path of the json file that describes the board
     */
    public Board(Match match, String type){
        this.match=match;
        this.type= type;
        ArrayList<Integer> tempX= new ArrayList<>();
        ArrayList<Integer> tempY= new ArrayList<>();
        ArrayList<String> tempC= new ArrayList<>();
        ArrayList<Integer> coord= new ArrayList<>();
        Square temp1,temp2;

        try {
            br = new BufferedReader(new FileReader(type));
            JsonObject jsonObject = gSon.fromJson(br, JsonObject.class);


            for(int i=1; i<=jsonObject.get("nRooms").getAsInt(); i++){
                tempX.clear();
                tempY.clear();
                tempC.clear();

                for (JsonElement j : jsonObject.get("xAmmo"+Integer.toString(i)).getAsJsonArray()) {
                    tempX.add(j.getAsInt());
                }
                for (JsonElement j : jsonObject.get("yAmmo"+Integer.toString(i)).getAsJsonArray()) {
                    tempY.add(j.getAsInt());
                }
                for (JsonElement j : jsonObject.get("colors"+Integer.toString(i)).getAsJsonArray()) {
                    tempC.add(j.getAsString());
                }
                rooms.add(new Room(jsonObject.get("size"+Integer.toString(i)).getAsInt(),
                        tempX,
                        tempY,
                        jsonObject.get("xSpawn"+Integer.toString(i)).getAsInt(),
                        jsonObject.get("ySpawn"+Integer.toString(i)).getAsInt(),
                        tempC,
                        this
                        ));
            }
            for (int i=1; i<=jsonObject.get("nDoors").getAsInt(); i++){
                coord.clear();
                for (JsonElement j : jsonObject.get("from"+Integer.toString(i)).getAsJsonArray()) {
                    coord.add(j.getAsInt());
                }
                temp1=this.find(coord.get(0),coord.get(1));
                coord.clear();
                for (JsonElement j : jsonObject.get("to"+Integer.toString(i)).getAsJsonArray()) {
                    coord.add(j.getAsInt());
                }
                temp2=this.find(coord.get(0),coord.get(1));
                temp1.getDoors().add(temp2);
                temp2.getDoors().add(temp1);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        this.reShuffleAmmo();
        this.reShufflePowerUps();
        this.reShuffleWeapons();
    }

    /**
     * This method return the Square from the coordinates
     * @param x represent the x coordinate of the Square to find
     * @param y represent the y coordinate of the Square to find
     * @return the Square of coordinate x, y
     * @throws NotFoundException is thrown when does not exist in this board a Square with coordinates x,y
     */
    public Square find(int x, int y) throws NotFoundException {

        for (Room r : rooms){
            if (r.find(x,y)!=null){
                return r.find(x,y);
            }
        }
        throw (new NotFoundException());
    }

    /**
     * This method return the first card of the deck of Weapon and remove that Weapon from the deck
     * @return the first card of the deck of Weapons
     */
    public Weapon nextWeapon(){
        Weapon w;
        if (weaponsList.size()>0) {
            w = weaponsList.get(weaponsList.size()-1);
            weaponsList.remove(weaponsList.size()-1);
            return w;
        }
        return null;
    }


    /**
     * This method return the first card of the deck of AmmoTile and remove that AmmoTile from the deck
     * @return the first card of the deck of AmmoTile
     */
    public AmmoTile nextAmmo(){
        AmmoTile a;
        a= ammoList.get(ammoList.size()-1);
        ammoList.remove(ammoList.size()-1);
        return a;
    }

    /**
     * This method return the first card of the deck of PowerUp and remove that PowerUp from the deck
     * @return the first card of the deck of PowerUp
     */
    public PowerUp nextPowerUp(){
        PowerUp p;
        p= powerUpList.get(powerUpList.size()-1);
        powerUpList.remove(powerUpList.size()-1);
        return p;
    }

    /**
     * This method generate the deck of AmmoTiles from a json file, remove from the deck the AmmoTiles already
     * on the board and then reshuffle it
     */
    public void reShuffleAmmo(){
        int i;
        AmmoTile temp;

        try {
            br = new BufferedReader(new FileReader("./resources/Ammo.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AmmoGson jsonObject = gSon.fromJson(br, AmmoGson.class);

        for (i=0; i< jsonObject.get().size();i++) {
            temp = jsonObject.get().get(i);
            ammoList.add(temp);
        }

        AmmoPoint a;

        for (i=0; i<this.getRooms().size();i++) {

            for (Square s : this.getRooms().get(i).getSquares()) {
               a= s instanceof AmmoPoint ? ((AmmoPoint) s) : null;
               if(a!=null) {
                   if(this.getAmmoList().contains(a.getAmmo())){
                       this.getAmmoList().remove(a.getAmmo());
                   }
               }
            }
        }
        shuffle(ammoList);
        return;
    }

    /**
     * This method generate the deck of PowerUp from a json file, remove from the deck the PowerUp already
     * in some Player hand and then reshuffle it
     */
    public void reShufflePowerUps(){
        int i,j;
        PowerUp temp;
        ArrayList<PowerUp>  powerUpListTemp= new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("./resources/PowerUp.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PowerUpGson jsonObject = gSon.fromJson(br, PowerUpGson.class);

        for (i=0; i< jsonObject.getPseudo().size();i++) {
            temp = new PowerUp(jsonObject.getPseudo().get(i).color,jsonObject.getPseudo().get(i).name);
            powerUpListTemp.add(temp);
        }
        for (i=0; i< jsonObject.getMovementEffects().size();i++) {
            powerUpListTemp.get(i).setEffect(jsonObject.getMovementEffects().get(i));
        }
        for (j=0; i< jsonObject.getEffectsVsPlayer().size()+jsonObject.getMovementEffects().size();i++, j++) {
            powerUpListTemp.get(i).setEffect(jsonObject.getMovementEffects().get(j));
        }
        for (j=0; i<jsonObject.getEffectsVsRoom().size()+ jsonObject.getEffectsVsPlayer().size()+jsonObject.getMovementEffects().size();i++, j++) {
            powerUpListTemp.get(i).setEffect(jsonObject.getMovementEffects().get(j));
        }
        for (j=0; i<jsonObject.getEffectsVsSquare().size()+ jsonObject.getEffectsVsPlayer().size()+jsonObject.getMovementEffects().size();i++, j++) {
            powerUpListTemp.get(i).setEffect(jsonObject.getMovementEffects().get(j));
        }

        powerUpList=powerUpListTemp;
        if (match!= null) {
            for (Player p : this.match.getPlayers()) {
                for (PowerUp up : p.getPowerUps()) {
                    for (PowerUp power : powerUpList) {
                        if (power.equals(up)) {
                            this.powerUpList.remove(power);
                        }
                    }
                }
            }
        }
        shuffle(powerUpList);
        return;
    }

    /**
     * This method generate the deck of Weapon from a json file, and then reshuffle it
     */
    public void reShuffleWeapons(){
        int i;
        String temp;
        Weapon weapon;
        try {
            br = new BufferedReader(new FileReader("./resources/Weapon/WeaponPath.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        WeaponPathGson jsonObject = gSon.fromJson(br, WeaponPathGson.class);

        gSon = new GsonBuilder()
                .registerTypeAdapter(Effect.class, new EffectDeserializer())
                .registerTypeAdapter(Constraint.class, new ConstraintDeserializer())
                .create();

        for (i=0; i< jsonObject.getBase().size();i++) {
            temp = jsonObject.getBase().get(i);
            try {
                br = new BufferedReader(new FileReader(temp));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            weapon= gSon.fromJson(br, WeaponBase.class);
            weaponsList.add(weapon);
        }

        for (i=0; i< jsonObject.getAlternative().size();i++) {
            temp = jsonObject.getAlternative().get(i);
            try {
                br = new BufferedReader(new FileReader(temp));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            weapon= gSon.fromJson(br, WeaponAlternative.class);
            weaponsList.add(weapon);
        }

        for (i=0; i< jsonObject.getOptional().size();i++) {
            temp = jsonObject.getOptional().get(i);
            try {
                br = new BufferedReader(new FileReader(temp));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            weapon= gSon.fromJson(br, WeaponOptional.class);
            weaponsList.add(weapon);
        }

        shuffle(weaponsList);
        return;
    }

    /**
     * This method search the SpawnPoint of the color required
     * @param color the color of the SpawnPoint
     * @return the SpawnPoint with color=color parameter
     * @throws NotFoundException is thrown when there are not SpawnPoint with the color required
     */
    public Square findSpawnPoint(String color) throws NotFoundException {
        int i,j;
        Square isThis;
        for (i=0;i<this.rooms.size();i++) {
            for (j=0;j<this.rooms.get(i).getSquares().size(); j++) {
                isThis=this.rooms.get(i).getSquares().get(j);
                if(isThis.getClass()==SpawnPoint.class){
                    if (isThis.getColor()== color){
                        return isThis;
                    }
                }
            }
        }
        throw (new NotFoundException());
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * This class is used for parsing the AmmoTile from the json file
     */
    private class AmmoGson{
        private ArrayList<AmmoTile> elements = new ArrayList<>();

        public ArrayList<AmmoTile> get(){
            return elements;
        }

    }

    /**
     * This class is used for parsing the Weapons from the json file
     */
    private class WeaponPathGson{
        private ArrayList<String> weaponPathBase = new ArrayList<>();
        private ArrayList<String> weaponPathAlternative = new ArrayList<>();
        private ArrayList<String> weaponPathOptional = new ArrayList<>();

        public ArrayList<String> getBase(){
            return weaponPathBase;
        }
        public ArrayList<String> getAlternative(){
            return weaponPathAlternative;
        }
        public ArrayList<String> getOptional(){
            return weaponPathOptional;
        }

    }

    /**
     * This class is used for parsing the PowerUp from the json file
     */
    private class PowerUpGson{
        private ArrayList<PseudoPowerUp> pseudo = new ArrayList<>();
        private ArrayList<MovementEffect> movementEffects=new ArrayList<>();
        private ArrayList<EffectVsPlayer> effectsVsPlayer=new ArrayList<>();
        private ArrayList<EffectVsRoom> effectsVsRoom=new ArrayList<>();
        private ArrayList<EffectVsSquare> effectsVsSquare= new ArrayList<>();
        private class PseudoPowerUp{
           String color, name;
        }

        public ArrayList<PseudoPowerUp> getPseudo() {
            return pseudo;
        }

        public ArrayList<MovementEffect> getMovementEffects() {
            return movementEffects;
        }

        public ArrayList<EffectVsPlayer> getEffectsVsPlayer() {
            return effectsVsPlayer;
        }

        public ArrayList<EffectVsRoom> getEffectsVsRoom() {
            return effectsVsRoom;
        }

        public ArrayList<EffectVsSquare> getEffectsVsSquare() {
            return effectsVsSquare;
        }

    }

    /**
     * this method is used for shuffling decks
     * @param deck is the deck that needs to be shuffled
     */
    private void shuffle( ArrayList deck){
        int i,n;
        for (i=0; i<deck.size()*2;i++){
            n=random.nextInt(deck.size());
            deck.add(deck.get(n));
            deck.remove(deck.get(n));
        }
        return;
    }

    public ArrayList<AmmoTile> getAmmoList() {
        return ammoList;
    }


    /**
     * This class is used for specifying the type of Effect need to be imported from the json file
     */
    private class EffectDeserializer implements JsonDeserializer<Effect> {
        @Override
        public Effect deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement type = jsonObject.get("type");
            if (type != null) {
                switch (type.getAsString()) {
                    case "MovementEffect":
                        return context.deserialize(jsonObject,
                                MovementEffect.class);
                    case "EffectVsPlayer":
                        return context.deserialize(jsonObject,
                                EffectVsPlayer.class);
                    case "EffectVsSquare":
                        return context.deserialize(jsonObject,
                                EffectVsSquare.class);
                    case "EffectVsRoom":
                        return context.deserialize(jsonObject,
                                EffectVsRoom.class);
                }
            }
            return null;
        }
    }
    /**
     * This class is used for specifying the type of Constraint need to be imported from the json file
     */
    private class ConstraintDeserializer implements JsonDeserializer<Constraint> {
        @Override
        public Constraint deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement type = jsonObject.get("type");
            if (type != null) {
                switch (type.getAsString()) {
                    case "MinimumDistance":
                        return context.deserialize(jsonObject,
                                MinimumDistance.class);
                    case "NotSameSquare":
                        return context.deserialize(jsonObject,
                                NotSameSquare.class);
                    case "NotSee":
                        return context.deserialize(jsonObject,
                                NotSee.class);
                    case "SameDirection":
                        return context.deserialize(jsonObject,
                                SameDirection.class);
                    case "AdjacentRoom":
                        return context.deserialize(jsonObject,
                                AdjacentRoom.class);
                    case "SameSquare":
                        return context.deserialize(jsonObject,
                                SameSquare.class);
                    case "See":
                        return context.deserialize(jsonObject,
                                See.class);
                }
            }
            return null;
        }
    }
}