package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * father class of effects
 */
public abstract class Effect implements Serializable {

    /**
     * blue cost of the effect
     */
    private int costBlue;
    /**
     * red cost of the effect
     */
    private int costRed;
    /**
     * yellow cost of the effect
     */
    private int costYellow;
    /**
     * name of the effect
     */
    private String name;
    /**
     * all of the constraint that this method has
     */
    private ArrayList<Constraint> constraints= new ArrayList<Constraint>();
    /**
     * constraint positivity of each constraint
     */
    private ArrayList<Boolean> constraintPositivity= new ArrayList<Boolean>();
    /**
     * descriptor of the effect for the client
     */
    private String description;
    /**
     * is true if the damage should convert the marks in damages, false otherwise
     */
    private boolean markActivator;

    /**
     * constructor method of Effect
     * @param costBlue blue cost of the effect
     * @param costRed red cost of the effect
     * @param costYellow yellow cost of the effect
     * @param name name of the effect
     * @param constraints all of the constraint that this method has
     * @param constraintPositivity constraint positivity of each constraint
     */
    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity ) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
        this.constraintPositivity=constraintPositivity;
    }

    /**
     * this method apply the effect after checking the constraint
     * @param target is a TargetParameter
     * @param previousTarget is the list of targets of the previous effects
     * @throws InvalidTargetException when not all of the constraints return true
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    abstract public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException;

    /**
     * generate the constraintSquare for the constraints
     * @param targetParameter is a target
     * @throws InvalidTargetException when not all of the constraints return true
     */
    abstract protected void constraintSquareGenerator(TargetParameter targetParameter) throws InvalidTargetException;

    /**
     * this method check if all of the constraint are verified
     * @param target is a TargetParameter
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the effect can be applied
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     * @throws InvalidTargetException when not all of the constraints return true
     */
    public boolean constraintsCheck(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException, InvalidTargetException {
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

    /**
     * utility method for the usage of the previous targets list
     * @param previousTarget is the list of targets of the previous effects
     * @param enemy is the player needed to add or remove from previos target list
     * @param addToList addToList true if the enemy is needed to be added to the previous target list at level 0
     * @param removeFromList true if the enemy is needed to be removed from the previous target list at level 0, and added at level 1
     */
    void previousMan(ArrayList<ArrayList<Player>> previousTarget, Player enemy, boolean addToList, boolean removeFromList){
        if(addToList){
            previousTarget.get(0).add(enemy);
        }
        if(removeFromList){
            previousTarget.get(0).remove(enemy);
            previousTarget.get(1).add(enemy);
        }
    }

    /**
     * utility method to apply the damages and marks on the targets
     * @param owner owner of the weapon
     * @param enemy player who was shot
     * @param damage the amount of damage to apply
     * @param mark the amount of mark to apply
     * @param previousTarget is the list of targets of the previous effects
     */
    void doRealDamage(Player owner, Player enemy, int damage, int mark, ArrayList<ArrayList<Player>> previousTarget){
        if(enemy!=owner){
            if(damage!=0){
                previousTarget.get(2).add(enemy);
                enemy.wound(damage,owner, this.markActivator);
            }
            enemy.marked(mark,owner);
        }
    }

    /**
     * getter method of cost blue
     * @return the blue cost of the effect
     */
    public int getCostBlue() {
        return costBlue;
    }

    /**
     * getter method of cost red
     * @return the red cost of the effect
     */
    public int getCostRed() {
        return costRed;
    }

    /**
     * getter method of cost yellow
     * @return the yellow cost of the effect
     */
    public int getCostYellow() {
        return costYellow;
    }

    /**
     * getter method of name
     * @return the name of the effect
     */
    public String getName() {
        return name;
    }

    /**
     * getter method of the constraints
     * @return the list of constraints
     */
    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * getter method of descriptor
     * @return the description of the effect
     */
    public String getDescription() {
        return description;
    }
}
