package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class check if the Owner can see the target
 */
public class See extends Constraint {
    /**
     * true if there must be a chain of players that can see one each others
     */
    private boolean concatenate;

    /**
     * constructor of See
     * @param concatenate true if there must be a chain of players that can see one each others
     * @param level of the previousTarget control
     */
    public See(boolean concatenate, int level) {
        super(level);
        this.concatenate = concatenate;
    }

    /**
     * this method evaluates if the Owner can see the enemy or the first of the previous target
     * @param target contains Square or Player
     * @param constraintPositivity true if the Owner must see the target, false if the Owner must not see the target
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {

        if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
        }
        ArrayList<Square> allTarget = new ArrayList<Square>();
        allTarget.add(target.getOwner().getPosition());
        if(concatenate){
            for(Player previousPlayer: previousTarget.get(getLevel())){
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
}
