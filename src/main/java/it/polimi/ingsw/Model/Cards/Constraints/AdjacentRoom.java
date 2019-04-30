package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

/**
 * this class checks if the player shoot at a room
 */
public class AdjacentRoom extends Constraint {
    /**
     * this method evaluates if the player can shoot at a room through a door
     * @param target contains the room which the player wants to shoot
     * @return true if the player can shoot at the target room, false if the player can't shoot
     */
    @Override
    public boolean canShoot(TargetParameter target) {
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
