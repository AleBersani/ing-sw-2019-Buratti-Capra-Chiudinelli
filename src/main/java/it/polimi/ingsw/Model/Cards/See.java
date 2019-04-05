package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

public class See extends Constraint {

    public boolean canShoot(Square target, Player owner){

        return false;
    }
}
