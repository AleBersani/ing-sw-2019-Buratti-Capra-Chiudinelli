package it.polimi.ingsw;

public class MinimumDistance extends Constraint{

    private int distance;

    public MinimumDistance(int distance) {
        this.distance = distance;
    }

    public boolean canShoot(Square target){

        return false;
    }
}
