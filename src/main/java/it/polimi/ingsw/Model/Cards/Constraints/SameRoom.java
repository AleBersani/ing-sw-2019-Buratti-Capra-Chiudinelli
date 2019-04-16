package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public class SameRoom extends Constraint {

    @Override
    public boolean canShoot(ArrayList<Square> targets, Player owner) {
        for (Square s : targets) {
            if (s.getRoom() != owner.getPosition().getRoom()){
                return false;
            }
        }
        return true;
    }
}
