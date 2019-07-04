package it.polimi.ingsw.model.map;

import java.io.Serializable;

public class AmmoTile implements Serializable {
    /**
     * the amount of red ammo
     */
    private int red;
    /**
     * the amount of blue ammo
     */
    private int blue;
    /**
     * the amount of yellow ammo
     */
    private int yellow;
    /**
     * the amount of powerUp
     */
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

    /**
     * getter method of red ammo
     * @return red ammo quantity
     */
    public int getRed() {
        return red;
    }

    /**
     * getter method of blue ammo
     * @return blue ammo quantity
     */
    public int getBlue() {
        return blue;
    }

    /**
     * getter method of yellow ammo
     * @return yellow ammo quantity
     */
    public int getYellow() {
        return yellow;
    }

    /**
     * getter method of the amount of powerUp
     * @return the amount of powerUp
     */
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
            return this.red == aT.red &&
                    this.blue == aT.blue &&
                    this.yellow == aT.yellow &&
                    this.powerUp == aT.powerUp;
        }
        return false;
    }
}
