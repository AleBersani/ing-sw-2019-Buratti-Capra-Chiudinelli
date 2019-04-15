package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

public class See extends Constraint {

    public boolean canShoot(Square target, Player owner){
        int i;
        if(owner.getPosition().getRoom()== target.getRoom()){
            return true;
        }
        for(i=0;i < owner.getPosition().getDoors().size();i++){
            if(owner.getPosition().getDoors().get(i).getRoom()== target.getRoom()){
                return true;
            }
        }
        return false;
    }
}
