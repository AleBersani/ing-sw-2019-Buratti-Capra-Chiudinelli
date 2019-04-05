package it.polimi.ingsw.Model.Map;

import java.util.ArrayList;

public class Room {

    private int size;
    private ArrayList<Square> squares= new ArrayList<Square>();
    private Board board;

    public Room(int size, ArrayList<Integer> xAmmo, ArrayList<Integer> yAmmo, ArrayList<Integer> xSpawn, ArrayList<Integer> ySpawn ,ArrayList<String> colors, Board board) {
        int i;
        this.size = size;
        this.board= board;
        for (i=0; i<xAmmo.size();i++){
            squares.add(i,new AmmoPoint(xAmmo.get(i),yAmmo.get(i),null ,colors.get(i), this));
        }
        for (i=0; i<xSpawn.size();i++){
            squares.add(i,new SpawnPoint(xSpawn.get(i),ySpawn.get(i),null ,colors.get(i+xAmmo.size()), this));
        }
    }  // sitemare doors

    public Square find(int x, int y) throws Exception {
        int i;
        for (i=0; i<squares.size();i++) {
            if (squares.get(i).getX() == x && squares.get(i).getY() == y) {
                return squares.get(i);
            }
        }
        throw (new Exception());
    } // aggiungere una nuova eccezzione

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
