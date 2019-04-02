package it.polimi.ingsw;

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

        return;
    }
}
