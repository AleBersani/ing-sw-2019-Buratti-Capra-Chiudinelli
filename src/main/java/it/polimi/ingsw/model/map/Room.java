package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;

/**
 * This class represent a room
 */
public class Room {

    private int size;
    private ArrayList<Square> squares= new ArrayList<Square>();
    private Board board;

    /**
     * this method generate a room and all the squares in it
     * @param size is the number of squares in the room
     * @param xAmmo is a list of x coordinate of the squares in this room, any element of this list needs to be paired with an element in yAmmo in the same position
     * @param yAmmo is a list of y coordinate of the squares in this room, any element of this list needs to be paired with an element in xAmmo in the same position
     * @param xSpawn is the x coordinate of the spawnPoint. If 0 there are not spawnPoints in this room
     * @param ySpawn is the y coordinate of the spawnPoint. If 0 there are not spawnPoints in this room
     * @param colors is a list of color that will be assigned of each square
     * @param board is the board in which this room is set
     */
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

    /**
     * This method return the Square from the coordinates
     * @param x represent the x coordinate of the Square to find
     * @param y represent the y coordinate of the Square to find
     * @return the Square of coordinate x, when does not exist in this room a Square with coordinates x,y return null
     */
    public Square find(int x, int y)  {
        for (int i=0; i<squares.size();i++) {
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

    @Override
    public String toString(){
        String cells="";
        for(Square s :squares){
            cells=cells.concat(Integer.toString(s.getX())).concat(";")
                    .concat(Integer.toString(s.getY())).concat(";")
                    .concat(s.getColor().concat(";"));
            if (s instanceof SpawnPoint){
                cells=cells.concat("SpawnPoint").concat(";");
                for(Weapon w:((SpawnPoint) s).getWeapons()) {
                    cells=cells.concat(w.getName()).concat(".");
                }
                if(!((SpawnPoint) s).getWeapons().isEmpty()){
                    cells=cells.concat(";");
                }
            }
            if (s instanceof AmmoPoint){
                cells=cells.concat("AmmoPoint").concat(";");
                cells=cells.concat("Y:")
                        .concat(Integer.toString(((AmmoPoint)s).getAmmo().getYellow())).concat(".")
                        .concat("R:")
                        .concat(Integer.toString(((AmmoPoint)s).getAmmo().getRed())).concat(".")
                        .concat("B:")
                        .concat(Integer.toString(((AmmoPoint)s).getAmmo().getBlue())).concat(".")
                        .concat("PU:")
                        .concat(Integer.toString(((AmmoPoint)s).getAmmo().getPowerUp())).concat(";");
            }
            cells= cells.concat("Players;");
            for(Player p : s.getOnMe()) {
                cells = cells.concat(p.getColor()).concat(".");
            }
            cells=cells.concat(";Doors;");
            for(Square door : s.getDoors()){
                cells=cells.concat(Integer.toString(door.getX()))
                        .concat(".")
                        .concat(Integer.toString(door.getY()));
                if(!door.equals(s.getDoors().get(s.getDoors().size()-1))){
                    cells = cells.concat("§");
                }
            }
            cells=cells.concat(" - ");
        }
        return cells;
    }
}
