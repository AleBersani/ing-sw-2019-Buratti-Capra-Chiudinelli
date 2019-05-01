package it.polimi.ingsw.model.map;

public class AmmoTile {
    private int red, blue, yellow;
    private int powerUp;

    /**
     * This class represent a single token
     * @param red represent the amount of red ammo that this AmmoTile gives you
     * @param blue represent the amount of blue ammo that this AmmoTile gives you
     * @param yellow represent the amount of yellow ammo that this AmmoTile gives you
     * @param powerUp represent the amount of PowerUp that this AmmoTile gives you
     */
    public AmmoTile(int red, int blue, int yellow, int powerUp) {
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
        this.powerUp = powerUp;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getYellow() {
        return yellow;
    }

    public int getPowerUp() {
        return powerUp;
    }

    /**
     * This method override the equals method of the Class Object
     * @param obj is the object that is confronted to this
     * @return true if all the parameters of the AmmoTile are equals to all the parameters of obj
     */
    @Override
    public boolean equals(Object obj) {
        AmmoTile aT;
        if (obj != null) {
            aT= (AmmoTile)obj;
            if (this.red == aT.red &&
                    this.blue == aT.blue &&
                    this.yellow == aT.yellow &&
                    this.powerUp == aT.powerUp) {
                return true;
            }
        }
        return false;
    }
}
