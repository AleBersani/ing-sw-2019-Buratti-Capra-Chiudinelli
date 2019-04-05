package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public abstract class Square {

    private int x, y;
    private ArrayList<Square> doors;
    private String color;
    private Room room;
    private ArrayList<Player> onMe= new ArrayList<Player>();


    public Square(int x, int y, String color, Room room) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.room = room;
    }

    public int calcDist(Square destination){

        ArrayList<Integer> dist= new ArrayList<Integer>();
        int i=0;
        int min;

        if (destination.room==this.room)
            return Math.abs(this.x-destination.x)+Math.abs(this.y-destination.y);
        for (Square s : this.room.getSquares()){
            for (Square d : s.doors){
                dist.add(i, this.calcDist(d)+ d.calcDist(destination));
                i++;
            }
        }
        min = dist.get(0);
        for (Integer ind : dist){
            min=Math.min(ind,min);
        }

        return min;
    }

    public void leaves (Player player){
        this.onMe.remove(player);
    }

    public void arrives (Player player){
        this.onMe.add(player);
    }

    public abstract boolean require();

    public abstract void generate();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Square> getDoors() {
        return doors;
    }

    public String getColor() {
        return color;
    }

    public Room getRoom() {
        return room;
    }

    public ArrayList<Player> getOnMe() {
        return onMe;
    }
}
