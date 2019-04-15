package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

public class SameSquare extends Constraint {

    public boolean canShoot(Square target, Player owner){
        if(target == owner.getPosition()){
            return true;
        }
        return false;
    }

    public boolean canShoot(Square target, Square target2, Player owner){
        if(target == owner.getPosition()){
            if(target2 == owner.getPosition()){
                return true;
            }
        }
        return false;
    }

    public boolean canShoot(Square target, Square target2, Square target3, Player owner){
        if(target == owner.getPosition()){
            if(target2 == owner.getPosition()){
                if(target3 == owner.getPosition()){
                    return true;
                }
            }
        }
        return false;
    }
}