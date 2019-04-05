package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

public class SameDirection extends Constraint {

    public boolean canShoot(Square target){

        return false;
    }

    public boolean canShoot(Square target, Square target2, Player owner){

        return false;
    }
}
