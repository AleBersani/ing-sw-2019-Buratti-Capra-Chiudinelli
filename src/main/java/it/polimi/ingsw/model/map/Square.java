package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.ElementNotFoundException;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent a single square on the Board
 */
public abstract class Square implements Serializable {
    /**
     * x coordinate of this square
     */
    private int x;
    /**
     * y coordinate of this square
     */
    private int y;
    /**
     * list of doors in this square
     */
    private ArrayList<Square> doors =new ArrayList<>();
    /**
     * color of this square
     */
    private String color;
    /**
     * room that contain this square
     */
    private Room room;
    /**
     * list of player on this square
     */
    private ArrayList<Player> onMe= new ArrayList<>();
    /**
     * utility attribute for clalcDist method
     */
    private boolean visited;

    /**
     * this method is the basic constructor of a Square
     * @param x is the x coordinate of the square
     * @param y is the y coordinate of the square
     * @param color is the color of the square
     * @param room is the room in which is positioned this square
     */
    public Square(int x, int y, String color, Room room) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.room = room;
    }

    /**
     * this method calculate the length of the path you have to do for reaching the square destination
     * @param destination is the square you want to reach
     * @return the length of the path you have to do for reaching the square destination
     */
    public int calcDist(Square destination){
        ArrayList<Integer> alreadyDone=new ArrayList<>();
        int i=0;
        int min;
        if (destination.room==this.room) {
            return Math.abs(this.x - destination.x) + Math.abs(this.y - destination.y);
        }
        for (Square s : this.room.getSquares()){
            for (Square d : s.getDoors()){
                if(!d.visited) {
                    alreadyDone.add(i, Math.abs(this.x - s.x) + Math.abs(this.y - s.y) + 1);
                    i++;
                }
            }
        }
        for (Square s : this.room.getSquares()) {
            s.visited=true;
        }

        i=0;
        for (Square s : this.room.getSquares()) {
            for (Square d : s.getDoors()) {
                if(!d.visited) {
                    alreadyDone.set(i, alreadyDone.get(i) + d.calcDist(destination));
                    i++;
                }
            }
        }
        if (alreadyDone.isEmpty()){
            for (Square s : this.getRoom().getSquares()){
                s.visited=false;
            }

            return Integer.MAX_VALUE/2;
        }
        min=alreadyDone.get(0);
        for(int dist : alreadyDone) {
            min= Math.min(dist, min);
        }
        for (Square s : this.getRoom().getSquares()){
            s.visited=false;
        }

        return min;
    }

    /**
     * this method is used for update the list of player on this square
     * @param player is the player that is removed from the list
     */
    public void leaves (Player player){
        this.onMe.remove(player);
    }

    /**
     * this method is used for update the list of player on this square
     * @param player is the player that is added to the list
     */
    public void arrives (Player player){
        this.onMe.add(player);
    }

    /**
     * this method return if this square need a new resource
     * @return true if this square need a new resource
     */
    public abstract boolean require();

    /**
     * this method add to this a resource based on the type of square
     */
    public abstract void generate();

    /**
     * this method return the ammoTile on this
     * @return the ammoTile on this, if there are not ammoTile on this return null
     * @throws ElementNotFoundException is thrown if this is not an AmmoPoint
     */
    public abstract AmmoTile grabAmmo() throws ElementNotFoundException;

    /**
     * this method return a weapon on this
     * @param position is the index of what weapon needs to be returned
     * @return the weapon on this at the index position, if there are not weapon on that index return null
     * @throws ElementNotFoundException is thrown if this is not a SpawnPoint
     */
    public abstract Weapon grabWeapon( int position) throws ElementNotFoundException;

    /**
     * getter method of x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * getter method of y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * getter method of the list of doors
     * @return the list of doors
     */
    public ArrayList<Square> getDoors() {
        return doors;
    }

    /**
     * getter method of this square's color
     * @return the color of this square
     */
    public String getColor() {
        return color;
    }

    /**
     * getter method of the room that contains this square
     * @return room that contains this square
     */
    public Room getRoom() {
        return room;
    }

    /**
     * getter method of the list of player on this square
     * @return list of player on this square
     */
    public ArrayList<Player> getOnMe() {
        return onMe;
    }

    /**
     * setter method of the list of player on this square
     * @param onMe a list of player
     */
    void setOnMe(ArrayList<Player> onMe) {
        this.onMe = onMe;
    }
}
