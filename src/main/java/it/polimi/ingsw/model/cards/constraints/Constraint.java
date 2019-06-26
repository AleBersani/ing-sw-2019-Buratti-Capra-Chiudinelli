package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * father method of all constraints
 */
public abstract class Constraint implements Serializable {
    /**
     * level of the previousTarget control
     */
    private int level;

    /**
     * constructor method of Constraint
     * @param level of the previousTarget control
     */
    public Constraint(int level) {
        this.level = level;
    }

    /**
     * evaluates if the player can shoot
     * @param target is a TargetParameter
     * @param constraintPositivity changes the behavior of child class
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    abstract public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException;

    /**
     * getter method of level
     * @return the level of the previous target requested in this class
     */
    public int getLevel() { return level; }
}