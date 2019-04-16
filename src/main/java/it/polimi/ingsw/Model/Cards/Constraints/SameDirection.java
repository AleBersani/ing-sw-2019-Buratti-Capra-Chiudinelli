package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;
import sun.plugin2.message.OverlayWindowMoveMessage;

import java.security.acl.Owner;
import java.util.ArrayList;

public class SameDirection extends Constraint {

    @Override
    public boolean canShoot(ArrayList<Square> targets, Player owner) {

        ArrayList<Integer> positions;
        positions = new ArrayList<Integer>();
        int i;

        for(Square s: targets){
            if((owner.getPosition().getY() != s.getY())&&(owner.getPosition().getX() != s.getX())){
                return false;
            }
            if((owner.getPosition().getY() == s.getY())&&(owner.getPosition().getX() == s.getX())){
                positions.add(1);
            }
            if(owner.getPosition().getX() == s.getX()){
                if(owner.getPosition().getY() < s.getY()){
                    positions.add(2);
                }
                if(owner.getPosition().getY() > s.getY()){
                    positions.add(4);
                }
            }
            if(owner.getPosition().getY() == s.getY()){
                if(owner.getPosition().getX() < s.getX()){
                    positions.add(5);
                }
                if(owner.getPosition().getX() > s.getX()){
                    positions.add(3);
                }
            }
        }

        for(i=1;i<positions.size();i++){
            if((positions.get(i)!= 1)&&(positions.get(i-1)!=1)){
                if(positions.get(i)!=positions.get(i-1)){
                    return false;
                }
            }
        }

        return true;
    }
}