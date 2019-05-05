package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public abstract class Constraint {

    abstract public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) throws NoOwnerException;

}
