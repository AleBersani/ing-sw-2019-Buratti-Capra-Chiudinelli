package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

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
