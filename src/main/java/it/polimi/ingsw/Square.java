package it.polimi.ingsw;

import java.util.ArrayList;

public abstract class Square {

    private int x, y;
    private ArrayList<Square> doors= new ArrayList<Square>();
    private String color;

    public Square(int x, int y, ArrayList<Square> doors, String color) {
        this.x = x;
        this.y = y;
        this.doors = doors;
        this.color = color;
    }

    public int calcDist(Square destination){

        return 1;
    }
}
