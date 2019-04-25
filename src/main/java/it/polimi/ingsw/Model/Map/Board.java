package it.polimi.ingsw.Model.Map;

import com.google.gson.*;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.*;
import it.polimi.ingsw.Model.Cards.Effects.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;


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


    public Board(Match match, String type){
        this.match=match;
        this.type= type;
        ArrayList<Integer> tempX= new ArrayList<>();
        ArrayList<Integer> tempY= new ArrayList<>();
        ArrayList<String> tempC= new ArrayList<>();
        ArrayList<Integer> coord= new ArrayList<>();
        Square temp1,temp2;
        int i;

        try {
            br = new BufferedReader(new FileReader(type));
            JsonObject jsonObject = gSon.fromJson(br, JsonObject.class);


            for(i=1; i<=jsonObject.get("nRooms").getAsInt(); i++){
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
            for (i=1; i<=jsonObject.get("nDoors").getAsInt(); i++){
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
        //this.reShuffleWeapons();
    }

    public Square find(int x, int y) throws NotFoundException {

        for (Room r : rooms){
            if (r.find(x,y)!=null){
                return r.find(x,y);
            }
        }
        throw (new NotFoundException());
    }

    public Weapon nextWeapon(){
        Weapon w;
        if (weaponsList.size()>=0) {
            w = weaponsList.get(weaponsList.size()-1);
            weaponsList.remove(weaponsList.size()-1);
            return w;
        }
        return null;
    }


    public AmmoTile nextAmmo(){
        AmmoTile a;
        a= ammoList.get(ammoList.size()-1);
        ammoList.remove(ammoList.size()-1);
        return a;
    }


    public PowerUp nextPowerUp(){
        PowerUp p;
        p= powerUpList.get(powerUpList.size()-1);
        powerUpList.remove(powerUpList.size()-1);
        return p;
    }

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
        for (j=0; i<jsonObject.getEffectsVsDirection().size()+ jsonObject.getEffectsVsPlayer().size()+jsonObject.getMovementEffects().size();i++, j++) {
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

    private class AmmoGson{
        private ArrayList<AmmoTile> elements = new ArrayList<>();

        public ArrayList<AmmoTile> get(){
            return elements;
        }

    }
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

    private class PowerUpGson{
        private ArrayList<PseudoPowerUp> pseudo = new ArrayList<>();
        private ArrayList<MovementEffect> movementEffects=new ArrayList<>();
        private ArrayList<EffectVsPlayer> effectsVsPlayer=new ArrayList<>();
        private ArrayList<EffectVsRoom> effectsVsRoom=new ArrayList<>();
        private ArrayList<EffectVsSquare> effectsVsSquare= new ArrayList<>();
        private ArrayList<EffectsVsDirection> effectsVsDirection= new ArrayList<>();
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

        public ArrayList<EffectsVsDirection> getEffectsVsDirection() {
            return effectsVsDirection;
        }

    }

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
                    case "EffectVsDirection":
                        return context.deserialize(jsonObject,
                                EffectsVsDirection.class);
                }
            }
            return null;
        }
    }
    private class ConstraintDeserializer implements JsonDeserializer<Constraint> {
        @Override
        public Constraint deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement type = jsonObject.get("type");
            if (type != null) {
                switch (type.getAsString()) {
                    case "MaximumDistance":
                        return context.deserialize(jsonObject,
                                MaximumDistance.class);
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
                    case "SameRoom":
                        return context.deserialize(jsonObject,
                                SameRoom.class);
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