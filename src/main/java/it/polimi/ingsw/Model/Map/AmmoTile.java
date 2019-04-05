package it.polimi.ingsw.Model.Map;

public class AmmoTile {
    private int red, blue, yellow;
    private String type;

    public AmmoTile(int red, int blue, int yellow, String type) {
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
        this.type = type;
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

    public String getType() {
        return type;
    }
}
