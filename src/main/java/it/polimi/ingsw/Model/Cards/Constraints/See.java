package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class See extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {
        boolean i;
        for (Square s : target.getConstraintSquareList()) {
            i = false;
            if(target.getOwner().getPosition().getRoom()!= s.getRoom()){
                for(Square d: target.getOwner().getPosition().getDoors()){
                    if(d.getRoom()== s.getRoom()){
                        i= true;
                    }
                }
                if(!i)
                    return false;
            }
        }
        return true;
    }
}
