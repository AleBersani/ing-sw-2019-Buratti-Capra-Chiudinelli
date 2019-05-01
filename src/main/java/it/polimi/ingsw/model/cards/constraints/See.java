package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class See extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        ArrayList<Square> allTarget = new ArrayList<Square>();
        for(Player previousPlayer: previousTarget){
            allTarget.add(previousPlayer.getPosition());
        }
        allTarget.add(target.getConstraintSquare());
        boolean i;
        for (Square targetSquare : allTarget) {
            i = !constraintPositivity;
            if(target.getOwner().getPosition().getRoom()!= targetSquare.getRoom()){
                for(Square door: target.getOwner().getPosition().getDoors()){
                    if(door.getRoom()== targetSquare.getRoom()){
                        i= constraintPositivity;
                    }
                }
                if(!i) {
                    return false;
                }
            }
            else{
                if(!constraintPositivity){
                    return false;
                }
            }
        }
        return true;
    }
}
