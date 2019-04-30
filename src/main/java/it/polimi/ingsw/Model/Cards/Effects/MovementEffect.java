package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int distance, boolean linear, boolean addToList, boolean removeFromList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.distance = distance;
        this.linear = linear;
        this.addToList=addToList;
        this.removeFromList=removeFromList;
    }

    private int distance;
    private boolean linear;
    private boolean addToList;
    private boolean removeFromList;

    public int getDistance() {
        return distance;
    }

    public boolean isLinear() {
        return linear;
    }

    @Override
    public void apply(TargetParameter target, ArrayList<Player> previousTarget) throws InvalidTargetException {

        int mDist;

        if(target.getEnemyPlayer().getPosition().calcDist(target.getMovement())>this.distance){
            throw new InvalidTargetException();
        }

        if(linear){
            if((target.getEnemyPlayer().getPosition().getX()!=target.getMovement().getX())&&(target.getEnemyPlayer().getPosition().getY()!=target.getMovement().getY())){
                throw new InvalidTargetException();
            }
            mDist = Math.abs(target.getEnemyPlayer().getPosition().getX() - target.getMovement().getX()) + Math.abs(target.getEnemyPlayer().getPosition().getY() - target.getMovement().getY());
            if(mDist < target.getEnemyPlayer().getPosition().calcDist(target.getMovement())){
                throw new InvalidTargetException();
            }
        }

        if(!constraintsCheck(target)){
            throw new InvalidTargetException();
        }
        else{
            target.getEnemyPlayer().getPosition().leaves(target.getEnemyPlayer());
            target.getEnemyPlayer().setPreviousPosition(target.getEnemyPlayer().getPosition());
            target.getEnemyPlayer().setPosition(target.getMovement());
            target.getMovement().arrives(target.getEnemyPlayer());
        }
    }
}
