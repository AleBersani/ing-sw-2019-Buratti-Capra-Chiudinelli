package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Square;

public class SameDirection extends Constraint {

    public boolean canShoot(Square target){

        return false;
    }

    public boolean canShoot(Square target, Square target2, Player owner){

        return false;
    }
}
