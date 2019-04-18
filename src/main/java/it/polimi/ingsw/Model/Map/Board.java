package it.polimi.ingsw.Model.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        //this.reShufflePowerUps();
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
            w = weaponsList.get(weaponsList.size());
            weaponsList.remove(weaponsList.size());
            return w;
        }
        return null;
    }


    public AmmoTile nextAmmo(){
        AmmoTile a;
        a= ammoList.get(ammoList.size());
        ammoList.remove(ammoList.size());
        return a;
    }


    public PowerUp nextPowerUp(){
        PowerUp p;
        p= powerUpList.get(powerUpList.size());
        powerUpList.remove(powerUpList.size());
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
        try {
            br = new BufferedReader(new FileReader("./resources/PowerUp.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PowerUpGson jsonObject = gSon.fromJson(br, PowerUpGson.class);

        for (i=0; i< jsonObject.get().size();i++) {
            temp = jsonObject.get().get(i);
            powerUpList.add(temp);
        }

         Player p;

        for (i=0; i<this.getRooms().size();i++) {
            for(j=0; j<this.getRooms().get(i).getSquares().size(); j++) {
                for (Player s : this.getRooms().get(i).getSquares().get(j).getOnMe()) {
                    for (PowerUp up : s.getPowerUps()) {
                        for (PowerUp power : powerUpList) {
                            if (power.equals(up)) {
                                this.powerUpList.remove(power);
                            }
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
        Weapon temp;
        try {
            br = new BufferedReader(new FileReader("./resources/Weapon.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        WeaponGson jsonObject = gSon.fromJson(br, WeaponGson.class);

        for (i=0; i< jsonObject.get().size();i++) {
            temp = jsonObject.get().get(i);
            weaponsList.add(temp);
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

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }


    private class AmmoGson{
        private ArrayList<AmmoTile> elements = new ArrayList<>();

        public ArrayList<AmmoTile> get(){
            return elements;
        }

    }
    private class WeaponGson{
        private ArrayList<Weapon> elements = new ArrayList<>();

        public ArrayList<Weapon> get(){
            return elements;
        }

    }

    private class PowerUpGson{
        private ArrayList<PowerUp> elements = new ArrayList<>();

        public ArrayList<PowerUp> get(){
            return elements;
        }

    }

    private void shuffle(ArrayList deck){
        int i,n;
        for (i=0; i<deck.size();i++){
            n=random.nextInt(deck.size());
            deck.add(deck.get(n));
            deck.remove(deck.get(n));
        }
        return;
    }

    public ArrayList<AmmoTile> getAmmoList() {
        return ammoList;
    }
}