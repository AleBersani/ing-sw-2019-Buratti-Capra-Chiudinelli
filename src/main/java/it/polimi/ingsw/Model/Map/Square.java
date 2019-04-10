package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public abstract class Square {

    private int x, y;
    private ArrayList<Square> doors =new ArrayList<>();
    private String color;
    private Room room;
    private ArrayList<Player> onMe= new ArrayList<Player>();

    private boolean visited;

    public Square(int x, int y, String color, Room room) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.room = room;

    }

    public int calcDist(Square destination){
        ArrayList<Integer> alreadyDone=new ArrayList<>();
        int i=0;
        int j=0;
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

    public void setOnMe(ArrayList<Player> onMe) {
        this.onMe = onMe;
    }
}
