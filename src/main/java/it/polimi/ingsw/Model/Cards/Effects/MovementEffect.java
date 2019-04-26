package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, int distance, boolean linear) {
        super(costBlue, costRed, costYellow, name, constraints);
        this.distance = distance;
        this.linear = linear;
    }

    private int distance;
    private boolean linear;

    public int getDistance() {
        return distance;
    }

    public boolean isLinear() {
        return linear;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetExcepion {
        int mDist;

        if(target.getEnemyPlayer().getPosition().calcDist(target.getMovement())>this.distance){
            throw new InvalidTargetExcepion();
        }

        if(linear){
            if((target.getEnemyPlayer().getPosition().getX()!=target.getMovement().getX())&&(target.getEnemyPlayer().getPosition().getY()!=target.getMovement().getY())){
                throw new InvalidTargetExcepion();
            }

            mDist = Math.abs(target.getEnemyPlayer().getPosition().getX() - target.getMovement().getX()) + Math.abs(target.getEnemyPlayer().getPosition().getY() - target.getMovement().getY());
            if(mDist < target.getEnemyPlayer().getPosition().calcDist(target.getMovement())){
                throw new InvalidTargetExcepion();
            }
        }

        if(!constraintsCheck(target)){
            throw new InvalidTargetExcepion();
        }
        else{
            target.getEnemyPlayer().getPosition().leaves(target.getEnemyPlayer());
            target.getEnemyPlayer().setPreviousPosition(target.getEnemyPlayer().getPosition());
            target.getEnemyPlayer().setPosition(target.getMovement());
            target.getMovement().arrives(target.getEnemyPlayer());
        }
    }
}
