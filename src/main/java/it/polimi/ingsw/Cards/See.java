package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Square;

public class See extends Constraint {

    public boolean canShoot(Square target, Player owner){

        return false;
    }
}
