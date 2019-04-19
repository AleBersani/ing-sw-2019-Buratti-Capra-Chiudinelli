package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Model.Cards.Constraints.*;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

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

    public boolean constraintsCheck(TargetParameter target){
        int i;
        for(i=0;i<constraints.size();i++){
            if(constraints.get(i).canShoot(target) !=constraintPositivity.get(i)){
                return false;
            }
        }
        return true;
    }

    public int getCostBlue() {
        return costBlue;
    }

    public int getCostRed() {
        return costRed;
    }

    public int getCostYellow() {
        return costYellow;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public ArrayList<Boolean> getConstraintPositivity() {
        return constraintPositivity;
    }
}
