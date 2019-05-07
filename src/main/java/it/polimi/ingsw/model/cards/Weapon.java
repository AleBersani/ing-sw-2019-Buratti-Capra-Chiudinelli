package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public abstract class Weapon {

    private String color,name;
    private int costBlue,costRed,costYellow;
    private boolean load;
    private ArrayList<Effect> effect;
    private ArrayList<ArrayList<Player>> previousTarget;
    private Player owner;

    public Weapon(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect) {
        this.color = color;
        this.name = name;
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.effect = effect;
        load=true;
        owner=null;
        previousTarget= new ArrayList<ArrayList<Player>>();
        previousTarget.add(new ArrayList<Player>());
        previousTarget.add(new ArrayList<Player>());
    }

    public void fire(TargetParameter target) throws InvalidTargetException {
        for (Effect e: effect){
                e.apply(target, this.previousTarget);
        }
        return;
    }

    public void reload(){
        this.load=true;
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

    public void setLoad(boolean load) {
        this.load = load;
    }

    public ArrayList<Effect> getEffect() {
        return effect;
    }

    public ArrayList<ArrayList<Player>> getPreviousTarget() {
        return previousTarget;
    }

    public Player getOwner() {
        return owner;
    }

    public abstract void fireOptional(TargetParameter target, int which) throws NotThisKindOfWeapon, InvalidTargetException;

    public abstract void fireAlternative(TargetParameter target) throws NotThisKindOfWeapon, InvalidTargetException;
}
