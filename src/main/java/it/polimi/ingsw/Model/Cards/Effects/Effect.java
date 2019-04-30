package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;
import java.util.List;

public abstract class Effect {

    private int costBlue;
    private int costRed;
    private int costYellow;
    private String name;
    private ArrayList<Constraint> constraints= new ArrayList<Constraint>();
    private ArrayList<Boolean> constraintPositivity= new ArrayList<>();

    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity ) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
        this.constraintPositivity=constraintPositivity;
    }

    abstract public void apply(TargetParameter target, ArrayList<Player> previousTarget) throws InvalidTargetException;

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
