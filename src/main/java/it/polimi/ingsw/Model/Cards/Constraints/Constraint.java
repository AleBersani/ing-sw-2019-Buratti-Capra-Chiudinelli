package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public abstract class Constraint {

    abstract public boolean canShoot(ArrayList<Square> targets, Player owner);

}
