package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Exception.NotThisKindOfWeapon;
import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public abstract class Weapon {

    private String color,name;
    private int costBlue,costRed,costYellow;
    private boolean load;
    private ArrayList<Effect> effect;
    private ArrayList<Player> previousTarget;
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
        previousTarget= new ArrayList<Player>();
    }

    public void fire(TargetParameter target) throws InvalidTargetException {
        for (Effect e: effect){
                e.apply(target);
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

    public ArrayList<Effect> getEffect() {
        return effect;
    }

    public ArrayList<Player> getPreviousTarget() {
        return previousTarget;
    }

    public Player getOwner() {
        return owner;
    }

    public abstract void fireOptional(TargetParameter target, int which) throws NotThisKindOfWeapon, InvalidTargetException;

    public abstract void fireAlternative(TargetParameter target) throws NotThisKindOfWeapon, InvalidTargetException;
}
