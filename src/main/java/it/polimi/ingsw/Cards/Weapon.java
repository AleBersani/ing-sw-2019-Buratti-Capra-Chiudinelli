package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;

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
}
