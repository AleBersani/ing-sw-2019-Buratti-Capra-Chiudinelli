package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class checks if the player shoot at a room
 */
public class AdjacentRoom extends Constraint {

    public AdjacentRoom(int level) {
        super(level);
    }

    /**
     * this method evaluates if the player can shoot at a room through a door
     * @param target contains the room which the player wants to shoot
     * @return true if the player can shoot at the target room, false if the player can't shoot
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget)throws NoOwnerException {
        if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
        }
        if(target.getTargetRoom() == target.getOwner().getPosition().getRoom()){
            return false;
        }
        for(Square square: target.getOwner().getPosition().getDoors()){
            if(square.getRoom() == target.getTargetRoom()){
                return true;
            }
        }
        return false;
    }
}
