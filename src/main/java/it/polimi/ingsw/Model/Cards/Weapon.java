package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public class Weapon {

    private String color,name;
    private int costBlue,costRed,costYellow;
    private boolean load;
    private ArrayList<Effect> effect= new ArrayList<Effect>();
    private ArrayList<Player> previousTarget= new ArrayList<Player>();
    private Player owner;

    public void fire(){

        return;
    }

    public void reload(){

        return;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getCostBlue() {
        return costBlue;
    }

    public int getCostRed() {
        return costRed;
    }

    public int getCostYellow() {
        return costYellow;
    }

    public boolean isLoad() {
        return load;
    }

    public ArrayList<Effect> getEffect() {
        return effect;
    }

    public ArrayList<Player> getPreviousTarget() {
        return previousTarget;
    }

    public Player getOwner() {
        return owner;
    }
}
