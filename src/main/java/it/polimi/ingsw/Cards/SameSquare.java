package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Square;

public class SameSquare extends Constraint {

    public boolean canShoot(Square target){

        return false;
    }

    public boolean canShoot(Square target, Square target2){

        return false;
    }

    public boolean canShoot(Square target, Square target2, Square target3, Player owner){

        return false;
    }
}