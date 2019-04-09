package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int distance, boolean linear) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.distance = distance;
        this.linear = linear;
    }

    private int distance;
    private boolean linear;

    public void apply(Square destination, Player target){
        target.setPreviousPosition(target.getPosition());
        target.setPosition(destination);
        return;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isLinear() {
        return linear;
    }
}
