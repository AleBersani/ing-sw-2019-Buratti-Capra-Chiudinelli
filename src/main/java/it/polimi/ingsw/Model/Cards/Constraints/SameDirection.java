package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

public class SameDirection extends Constraint {

    public boolean canShoot(Square target, Player owner){
        if(owner.getPosition().getX()== target.getX()){
            return true;
        }
        if(owner.getPosition().getY()== target.getY()){
            return true;
        }
        return false;
    }

    public boolean canShoot(Square target, Square target2, Player owner){
        if((owner.getPosition().getX()== target.getX())&&(owner.getPosition().getX()== target2.getX())){
            if((owner.getPosition().getY() >= target.getY())&&(owner.getPosition().getY() >= target2.getY())){
                return true;
            }
            if((owner.getPosition().getY() <= target.getY())&&(owner.getPosition().getY() <= target2.getY())){
                return true;
            }
            return false;
        }
        if((owner.getPosition().getY()== target.getY())&&(owner.getPosition().getY()== target2.getY())){
            if((owner.getPosition().getX() >= target.getX())&&(owner.getPosition().getX() >= target2.getX())){
                return true;
            }
            if((owner.getPosition().getX() <= target.getX())&&(owner.getPosition().getX() <= target2.getX())){
                return true;
            }
            return false;
        }
        return false;
    }
}
