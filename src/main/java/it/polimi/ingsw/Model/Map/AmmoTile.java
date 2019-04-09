package it.polimi.ingsw.Model.Map;

public class AmmoTile {
    private int red, blue, yellow;
    private int powerUp;

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
}
