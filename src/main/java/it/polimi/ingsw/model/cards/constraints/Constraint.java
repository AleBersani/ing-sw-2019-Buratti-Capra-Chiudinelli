package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Constraint implements Serializable {

    private int level;

    public Constraint(int level) {
        this.level = level;
    }

    abstract public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException;

    public int getLevel() { return level; }
}