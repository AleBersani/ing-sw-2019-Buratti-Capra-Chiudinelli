package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Room;
import it.polimi.ingsw.model.map.Square;
import java.util.ArrayList;

public class TargetParameter {
    private Square movement;
    private Player owner;
    private Player enemyPlayer;
    private Room targetRoom;
    private Square targetSquare;
    //constraintSquareList is made from other parameter input in the Controller
    private ArrayList<Square> constraintSquareList = new ArrayList<Square>();
    private ArrayList<Player> directionPlayer = new ArrayList<Player>();
    private ArrayList<Square> directionSquare = new ArrayList<Square>();

    public TargetParameter(Square movement, Player owner, Player enemyPlayer, Room targetRoom, Square targetSquare) {
        this.movement = movement;
        this.owner = owner;
        this.enemyPlayer = enemyPlayer;
        this.targetRoom = targetRoom;
        this.targetSquare = targetSquare;
    }

    public Square getMovement() {
        return movement;
    }

    public Player getOwner() {
        return owner;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public Room getTargetRoom() {
        return targetRoom;
    }

    public Square getTargetSquare() {
        return targetSquare;
    }

    public ArrayList<Square> getConstraintSquareList() {
        return constraintSquareList;
    }

    public ArrayList<Player> getDirectionPlayer() {
        return directionPlayer;
    }

    public ArrayList<Square> getDirectionSquare() {
        return directionSquare;
    }

    public void setMovement(Square movement) {
        this.movement = movement;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public void setTargetRoom(Room targetRoom) {
        this.targetRoom = targetRoom;
    }

    public void setTargetSquare(Square targetSquare) {
        this.targetSquare = targetSquare;
    }

}
