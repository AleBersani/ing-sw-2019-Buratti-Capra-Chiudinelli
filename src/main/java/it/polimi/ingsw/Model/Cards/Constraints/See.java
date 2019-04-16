package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public class See extends Constraint {

    @Override
    public boolean canShoot(ArrayList<Square> targets, Player owner) {
        boolean i;
        for (Square s : targets) {
            i = false;
            if(owner.getPosition().getRoom()!= s.getRoom()){
                for(Square d: owner.getPosition().getDoors()){
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
