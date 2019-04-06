package it.polimi.ingsw.Model.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Match;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Board {
    private ArrayList<Room> rooms= new ArrayList<Room>();
    private ArrayList<AmmoTile> ammoList= new ArrayList<AmmoTile>();
    private ArrayList<PowerUp> powerUpList= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weaponsList= new ArrayList<Weapon>();
    private Match match;
    private Gson gSon= new Gson();
    private String type;
    private BufferedReader br;


    public Board(Match match, String type){
        this.match=match;
        this.type= type;
        ArrayList<Integer> tempX= new ArrayList<>();
        ArrayList<Integer> tempY= new ArrayList<>();
        ArrayList<String> tempC= new ArrayList<>();
        ArrayList<Integer> coord= new ArrayList<>();
        Square temp1,temp2;
        int i, x,y;

        try {
            br = new BufferedReader(new FileReader(type));
            JsonObject jsonObject = gSon.fromJson(br, JsonObject.class);


            for(i=1; i<=jsonObject.get("nRooms").getAsInt(); i++){

                for (JsonElement j : jsonObject.get("xAmmo"+i).getAsJsonArray()) {
                    tempX.add(j.getAsInt());
                }
                for (JsonElement j : jsonObject.get("yAmmo"+i).getAsJsonArray()) {
                    tempY.add(j.getAsInt());
                }
                for (JsonElement j : jsonObject.get("colors"+i).getAsJsonArray()) {
                    tempC.add(j.getAsString());
                }
                rooms.add(new Room(jsonObject.get("size"+i).getAsInt(),
                        tempX,
                        tempY,
                        jsonObject.get("xSpawn"+i).getAsInt(),
                        jsonObject.get("ySpawn"+i).getAsInt(),
                        tempC,
                        this
                        ));
            }
            for (i=1; i<=jsonObject.get("nDoors").getAsInt(); i++){
                for (JsonElement j : jsonObject.get("from"+i).getAsJsonArray()) {
                    coord.add(j.getAsInt());
                }
                temp1=this.find(coord.get(0),coord.get(1));
                for (JsonElement j : jsonObject.get("to"+i).getAsJsonArray()) {
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

    }

    public Square find(int x, int y) throws NotFoundException {

        for (Room r : rooms){
            try {
                return r.find(x,y);
            } catch (Exception e) {}
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
    private void reShuffleAmmo(){
        //TODO
    }

    private void ReShufflePuwerUps(){
        //TODO
    }

}