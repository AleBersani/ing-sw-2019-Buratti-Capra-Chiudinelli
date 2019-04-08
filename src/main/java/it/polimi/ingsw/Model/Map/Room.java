package it.polimi.ingsw.Model.Map;

import java.util.ArrayList;

public class Room {

    private int size;
    private ArrayList<Square> squares= new ArrayList<Square>();
    private Board board;

    public Room(int size, ArrayList<Integer> xAmmo, ArrayList<Integer> yAmmo, int xSpawn, int ySpawn ,ArrayList<String> colors, Board board) {
        int i;
        this.size = size;
        this.board= board;
        for (i=0; i<xAmmo.size();i++){
            squares.add(i,new AmmoPoint(xAmmo.get(i),yAmmo.get(i) ,colors.get(i), this));
        }
        if (xSpawn!=0 && ySpawn!=0) {
            squares.add(i, new SpawnPoint(xSpawn, ySpawn, colors.get(xAmmo.size()), this));
        }
    }

    public Square find(int x, int y)  {
        int i;
        for (i=0; i<squares.size();i++) {
            if (squares.get(i).getX() == x && squares.get(i).getY() == y) {
                return squares.get(i);
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public Board getBoard() {
        return board;
    }
}
