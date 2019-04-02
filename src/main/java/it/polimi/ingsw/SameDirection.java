package it.polimi.ingsw;

public class SameDirection extends Constraint {

    public boolean canShoot(Square target){

        return false;
    }

    public boolean canShoot(Square target, Square target2){

        return false;
    }
}
