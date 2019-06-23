package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int distance, boolean linear, boolean addToList, boolean removeFromList, boolean self) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.distance = distance;
        this.linear = linear;
        this.addToList=addToList;
        this.removeFromList=removeFromList;
        this.self=self;
    }

    private int distance;
    private boolean linear;
    private boolean addToList;
    private boolean removeFromList;
    private boolean self;

    public int getDistance() {
        return distance;
    }

    public boolean isLinear() {
        return linear;
    }

    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        int mDist;
        Player player;

        if(self){
            player=target.getOwner();
        }
        else{
            if(target.getEnemyPlayer().equals(target.getOwner())){
                throw new InvalidTargetException();
            }
            else {
                player=target.getEnemyPlayer();
            }
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

    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) {
        targetParameter.setConstraintSquare(targetParameter.getMovement());
    }
}
