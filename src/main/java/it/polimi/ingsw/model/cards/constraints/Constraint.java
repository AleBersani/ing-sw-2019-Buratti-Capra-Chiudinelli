package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.TargetParameter;

public abstract class Constraint {

    abstract public boolean canShoot(TargetParameter target);

}
