package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int distance, boolean linear) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
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
        if(!constraintsCheck(target)){
            throw new InvalidTargetExcepion();
        }
        else{
            target.getEnemyPlayer().setPreviousPosition(target.getEnemyPlayer().getPosition());
            target.getEnemyPlayer().setPosition(target.getMovement());
        }
    }
}
