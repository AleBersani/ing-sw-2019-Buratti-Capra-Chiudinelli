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
    private Square constraintSquare;
    private String typeOfFire;

    public TargetParameter(Square movement, Player owner, Player enemyPlayer, Room targetRoom, Square targetSquare, String typeOfFire, Square constraintSquare) {
        this.movement = movement;
        this.owner = owner;
        this.enemyPlayer = enemyPlayer;
        this.targetRoom = targetRoom;
        this.targetSquare = targetSquare;
        this.typeOfFire = typeOfFire;
        this.constraintSquare = constraintSquare;
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

    public Square getConstraintSquare() { return constraintSquare; }

    public void setConstraintSquare(Square constraintSquare) { this.constraintSquare = constraintSquare; }

    public void setMovement(Square movement) { this.movement = movement; }

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

    public String getTypeOfFire() {
        return typeOfFire;
    }
}
