package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class See extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        /*
        boolean i;
        for (Square s : target.getConstraintSquareList()) {
            i = false;
            if(target.getOwner().getPosition().getRoom()!= s.getRoom()){
                for(Square d: target.getOwner().getPosition().getDoors()){
                    if(d.getRoom()== s.getRoom()){
                        i= true;
                    }
                }
                if(!i) {
                    return false;
                }
            }
        }
        */
        return true;
    }
}
