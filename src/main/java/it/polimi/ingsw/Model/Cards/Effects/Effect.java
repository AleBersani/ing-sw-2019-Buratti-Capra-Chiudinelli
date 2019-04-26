package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
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

    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
    }

    abstract public void apply(TargetParameter target) throws InvalidTargetException;

    public boolean constraintsCheck(TargetParameter target){
        int i;
        if(constraints.isEmpty()){
            return true;
        }
        for(i=0;i<constraints.size();i++){
            if(!constraints.get(i).canShoot(target)){
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

}
