package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class SameDirection extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget)throws NoOwnerException {
        if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        ArrayList<Square> allTarget = new ArrayList<Square>();
        for(Player previousPlayer: previousTarget){
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