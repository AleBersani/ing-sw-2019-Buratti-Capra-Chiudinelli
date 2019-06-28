package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class move a player to a specific square
 */
public class MovementEffect extends Effect {
    /**
     * is the maximum distance within a player can be moved
     */
    private int distance;
    /**
     * true if the movement must be in one direction
     */
    private boolean linear;
    /**
     * true if the enemy is needed to be added to the previous target list at level 0
     */
    private boolean addToList;
    /**
     * true if the enemy is needed to be removed from the previous target list at level 0, and added at level 1
     */
    private boolean removeFromList;
    /**
     * true if the player to move is the owner
     */
    private boolean self;

    /**
     * constructor method of MovementEffect
     * @param costBlue blue cost of the effect
     * @param costRed red cost of the effect
     * @param costYellow yellow cost of the effect
     * @param name name of the effect
     * @param constraints all of the constraint that this method has
     * @param constraintPositivity constraint positivity of each constraint
     * @param distance is the maximum distance within a player can be moved
     * @param linear true if the movement must be in one direction
     * @param addToList true if the enemy is needed to be added to the previous target list at level 0
     * @param removeFromList true if the enemy is needed to be removed from the previous target list at level 0, and added at level 1
     * @param self true if the player to move is the owner
     */
    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int distance, boolean linear, boolean addToList, boolean removeFromList, boolean self) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.distance = distance;
        this.linear = linear;
        this.addToList=addToList;
        this.removeFromList=removeFromList;
        this.self=self;
    }

    /**
     * getter method of distance
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * getter method of linear
     * @return linear
     */
    public boolean isLinear() {
        return linear;
    }

    /**
     * this method apply the effect after checking the constraint
     * @param target is a TargetParameter
     * @param previousTarget is the list of targets of the previous effects
     * @throws InvalidTargetException when not all of the constraints return true
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        int mDist;
        Player player;

        if(self){
            player=target.getOwner();
        }
        else{
            if(target.getEnemyPlayer()==null) {
                throw new InvalidTargetException();
            }
            else {
                if (target.getEnemyPlayer().equals(target.getOwner())) {
                    throw new InvalidTargetException();
                }
                else {
                    player = target.getEnemyPlayer();
                }
            }
        }

        if(target.getMovement() == null){
            throw new InvalidTargetException();
        }
        if(player.getPosition().calcDist(target.getMovement())>this.distance){
            throw new InvalidTargetException();
        }

        if(linear){
            if((player.getPosition().getX()!=target.getMovement().getX())&&(player.getPosition().getY()!=target.getMovement().getY())){
                throw new InvalidTargetException();
            }
            mDist = Math.abs(player.getPosition().getX() - target.getMovement().getX()) + Math.abs(player.getPosition().getY() - target.getMovement().getY());
            if(mDist < player.getPosition().calcDist(target.getMovement())){
                throw new InvalidTargetException();
            }
        }

        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            player.getPosition().leaves(player);
            player.setPreviousPosition(player.getPosition());
            player.setPosition(target.getMovement());
            target.getMovement().arrives(player);
            this.previousMan(previousTarget,target.getEnemyPlayer(),this.addToList,this.removeFromList);
        }
    }

    /**
     * generate the constraintSquare for the constraints
     * @param targetParameter is a target
     */
    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) throws InvalidTargetException {
        if(targetParameter.getMovement() == null){
            throw new InvalidTargetException();
        }
        targetParameter.setConstraintSquare(targetParameter.getMovement());
    }
}
