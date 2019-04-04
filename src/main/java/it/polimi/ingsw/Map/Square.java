package it.polimi.ingsw.Map;

import it.polimi.ingsw.Player;

import java.util.ArrayList;

public abstract class Square {

    private int x, y;
    private ArrayList<Square> doors;
    private String color;
    private Room room;
    private ArrayList<Player> onMe= new ArrayList<Player>();


    public Square(int x, int y, ArrayList<Square> doors, String color, Room room) {
        this.x = x;
        this.y = y;
        this.doors = doors;
        this.color = color;
        this.room = room;
    }

    public int calcDist(Square destination){

        return 1;
    }

    public void leave(Player player){}

    public void arrived (Player player){}

    public abstract boolean isEmpty();
}
