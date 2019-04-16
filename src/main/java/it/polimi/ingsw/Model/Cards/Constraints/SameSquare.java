package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public class SameSquare extends Constraint {

    @Override
    public boolean canShoot(ArrayList<Square> targets, Player owner) {
        for (Square s : targets) {
            if(s != owner.getPosition()){
                return false;
            }
        }
        return true;
    }
}