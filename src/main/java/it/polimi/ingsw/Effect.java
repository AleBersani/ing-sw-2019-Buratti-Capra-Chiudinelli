package it.polimi.ingsw;

import java.util.ArrayList;

public abstract class Effect {

    private int costBlue;
    private int costRed;
    private int costYellow;
    private String name;
    private ArrayList<Constraint> constraints= new ArrayList<Constraint>();
    private ArrayList<Boolean> constraintPositivity= new ArrayList<Boolean>();

    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
        this.constraintPositivity = constraintPositivity;
    }
}
