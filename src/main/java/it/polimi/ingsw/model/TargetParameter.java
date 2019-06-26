package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Room;
import it.polimi.ingsw.model.map.Square;

/**
 * This class represents the target of the effect of the weapon or powerup
 */
public class TargetParameter {
    /**
     * This attribute indicates the square of the movement
     */
    private Square movement;
    /**
     * This attribute indicates the player owner
     */
    private Player owner;
    /**
     * This attribute indicates the enemy player
     */
    private Player enemyPlayer;
    /**
     * This attribute indicates the room that the owner is targeting
     */
    private Room targetRoom;
    /**
     * This attribute indicates the square that the owner is targeting
     */
    private Square targetSquare;
    /**
     * This attribute indicates the square where the constraint are working
     */
    private Square constraintSquare;
    /**
     * This attribute indicates the type of fire of the weapon
     */
    private String typeOfFire;

    /**
     * This constructor instantiates the target parameter
     * @param movement This parameter defines the position where the target of the effect will be
     * @param owner This parameter defines the player that owns the weapon or power up used
     * @param enemyPlayer This parameter defines the enemy player that is targeted by the owner
     * @param targetRoom This parameter defines the room that is targeted
     * @param targetSquare This parameter defines the square that is targeted
     * @param typeOfFire This parameter defines the type of fire of the weapon
     */
    public TargetParameter(Square movement, Player owner, Player enemyPlayer, Room targetRoom, Square targetSquare, String typeOfFire) {
        this.movement = movement;
        this.owner = owner;
        this.enemyPlayer = enemyPlayer;
        this.targetRoom = targetRoom;
        this.targetSquare = targetSquare;
        this.typeOfFire = typeOfFire;
    }

    /**
     * This method return the final position of the targeted player
     * @return A square where the player is
     */
    public Square getMovement() {
        return movement;
    }

    /**
     * This method return the owner of the weapon or the power up used
     * @return The owner player
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * This method return the enemy player targeted by the owner
     * @return The enemy player targeted
     */
    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    /**
     * This method return the room that is targeted
     * @return The room that is targeted
     */
    public Room getTargetRoom() {
        return targetRoom;
    }

    /**
     * This method return the square that is targeted
     * @return The square that is targeted
     */
    public Square getTargetSquare() {
        return targetSquare;
    }

    /**
     *  This method return the type of fire of the weapon
     * @return The type of fire of the weapon
     */
    public String getTypeOfFire() {
        return typeOfFire;
    }

    /**
     * This method return the square where the constraints work
     * @return The square where the constraints work
     */
    public Square getConstraintSquare() {
        return constraintSquare;
    }

    /**
     * This method sets the movement of the enemy player targeted
     * @param movement This parameter defines the position where the target of the effect will be
     */
    public void setMovement(Square movement) {
        this.movement = movement;
    }

    /**
     * This method sets the owner of the weapon or power up used
     * @param owner This parameter defines the player that owns the weapon or power up used
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * This method sets the enemy player that is targeted by the owner
     * @param enemyPlayer This parameter defines the enemy player that is targeted by the owner
     */
    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    /**
     * This method sets the room that is targeted
     * @param targetRoom This parameter defines the room that is targeted
     */
    public void setTargetRoom(Room targetRoom) {
        this.targetRoom = targetRoom;
    }

    /**
     * This method sets the square that is targeted
     * @param targetSquare This parameter defines the square that is targeted
     */
    public void setTargetSquare(Square targetSquare) {
        this.targetSquare = targetSquare;
    }

    /**
     * This method sets the square where the constraints are working
     * @param constraintSquare This parameter defines the square where the constraints are working
     */
    public void setConstraintSquare(Square constraintSquare) {
        this.constraintSquare = constraintSquare;
    }
}
