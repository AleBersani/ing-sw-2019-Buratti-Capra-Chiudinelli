package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class See extends Constraint {

    boolean concatenate;

    public See(boolean concatenate) { this.concatenate = concatenate; }

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget)throws NoOwnerException {

        if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
        }
        ArrayList<Square> allTarget = new ArrayList<Square>();
        allTarget.add(target.getOwner().getPosition());
        if(concatenate){
            for(Player previousPlayer: previousTarget){
                allTarget.add(previousPlayer.getPosition());
            }
        }
        allTarget.add(target.getConstraintSquare());
        boolean i;
        for(int j=1;j<allTarget.size();j++){
            i=!constraintPositivity;
            if(allTarget.get(j).getRoom()!=allTarget.get(j-1).getRoom()){
                for(Square door:allTarget.get(j-1).getDoors()){
                    if(door.getRoom()==allTarget.get(j).getRoom()){
                        i=constraintPositivity;
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
    //fare i due casi per concatenazione oppure no, per renderla in modo safe anche se ci fossero previous target ma me ne frega solo di un bersaglio finale
}
