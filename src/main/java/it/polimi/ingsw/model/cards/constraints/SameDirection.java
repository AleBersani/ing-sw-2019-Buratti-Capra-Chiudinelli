package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class check if the enemies are on the same direction with the Owner
 */
public class SameDirection extends Constraint {
    /**
     * constructor of SameDirection
     * @param level of the previousTarget control
     */
    public SameDirection(int level) {
        super(level);
    }

    /**
     * this method evaluates if the player, the targeted enemy and the previous target are on the same direction
     * @param target contain a player or a square
     * @param constraintPositivity is not needed in this method
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget)throws NoOwnerException {
        if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        ArrayList<Square> allTarget = new ArrayList<Square>();
        for(Player previousPlayer: previousTarget.get(getLevel())){
            allTarget.add(previousPlayer.getPosition());
        }
        allTarget.add(target.getConstraintSquare());

        for(Square s: allTarget){
            if((target.getOwner().getPosition().getY() != s.getY())&&(target.getOwner().getPosition().getX() != s.getX())){
                return false;
            }
            if((target.getOwner().getPosition().getY() == s.getY())&&(target.getOwner().getPosition().getX() == s.getX())){
                positions.add(1);
            }
            if(target.getOwner().getPosition().getX() == s.getX()){
                if(target.getOwner().getPosition().getY() < s.getY()){
                    positions.add(2);
                }
                if(target.getOwner().getPosition().getY() > s.getY()){
                    positions.add(4);
                }
            }
            if(target.getOwner().getPosition().getY() == s.getY()){
                if(target.getOwner().getPosition().getX() < s.getX()){
                    positions.add(5);
                }
                if(target.getOwner().getPosition().getX() > s.getX()){
                    positions.add(3);
                }
            }
        }

        for(int i=1;i<positions.size();i++){
            if((positions.get(i)!= 1)&&(positions.get(i-1)!=1)){
                if(positions.get(i)!=positions.get(i-1)){
                    return false;
                }
            }
        }
        return true;
    }

}