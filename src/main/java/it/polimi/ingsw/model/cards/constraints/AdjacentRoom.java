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
    /**
     * constructor of AdjacentRoom
     * @param level of the previousTarget control
     */
    public AdjacentRoom(int level) {
        super(level);
    }

    /**
     * this method evaluates if the player can shoot at a room through a door
     * @param target contains the room which the player wants to shoot
     * @param constraintPositivity is not needed in this method
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot at the target room, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget)throws NoOwnerException {
        if(target.getTargetRoom() == null){
            return false;
        }
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
