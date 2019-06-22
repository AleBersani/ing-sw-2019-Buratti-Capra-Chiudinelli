package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Effect implements Serializable {

    private int costBlue;
    private int costRed;
    private int costYellow;
    private String name;
    private ArrayList<Constraint> constraints= new ArrayList<Constraint>();
    private ArrayList<Boolean> constraintPositivity= new ArrayList<Boolean>();
    private String description;

    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity ) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
        this.constraintPositivity=constraintPositivity;
    }

    abstract public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException;

    abstract protected void constraintSquareGenerator(TargetParameter targetParameter);

    public boolean constraintsCheck(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {
        int i;
        if(constraints.isEmpty()){
            return true;
        }
        constraintSquareGenerator(target);
        for(i=0;i<constraints.size();i++){
            if(!constraints.get(i).canShoot(target,this.constraintPositivity.get(i),previousTarget)){
                return false;
            }
        }
        return true;
    }

    protected void previousMan(ArrayList<ArrayList<Player>> previousTarget,Player enemy,boolean addToList,boolean removeFromList){
        if(addToList){
            previousTarget.get(0).add(enemy);
        }
        if(removeFromList){
            previousTarget.get(0).remove(enemy);
            previousTarget.get(1).add(enemy);
        }
    }

    protected void doRealDamage(Player owner,Player enemy,int damage,int mark){
        if(enemy!=owner){
            if(damage!=0){
                enemy.wound(damage,owner);
            }
            enemy.marked(mark,owner);
        }
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

    public String getDescription() {
        return description;
    }
}
