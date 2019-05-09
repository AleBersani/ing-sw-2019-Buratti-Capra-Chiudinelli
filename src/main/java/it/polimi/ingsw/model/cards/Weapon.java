package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoAmmoException;
import it.polimi.ingsw.exception.NoOwnerException;
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

    public void fire(ArrayList<TargetParameter> target) throws InvalidTargetException, NoOwnerException {
        int min = calcMinim(target,effect.size());
        for(int i=0;i<min;i++){
                effect.get(i).apply(target.get(i), this.previousTarget);
        }
        return;
    }

    protected int calcMinim(ArrayList<TargetParameter> target, int effectSize) throws InvalidTargetException {
        int min = effectSize;
        if(target==null){
            throw new InvalidTargetException();
        }
        if(target.size()<effectSize){
            min = target.size();
        }
        return min;
    }

    protected void pay(Player owner,int red, int yellow, int blue, ArrayList<PowerUp> powerUps, int which) throws NoAmmoException {
        ArrayList<Integer> payment;
        payment=powerUpToPayment(powerUps,red,yellow,blue);
        if (canPay(payment,which)) {
            owner.setRedAmmo(owner.getRedAmmo() - red);
            owner.setBlueAmmo(owner.getBlueAmmo() - blue);
            owner.setYellowAmmo(owner.getYellowAmmo() - yellow);
        }
        else {
            throw new NoAmmoException();
        }
    }

    protected ArrayList<Integer> powerUpToPayment(ArrayList<PowerUp> powerUps, int red,int yellow, int blue){
        ArrayList<Integer> payment=new ArrayList<>();
        for (PowerUp p : powerUps){
            switch (p.getColor()) {
                case "red":
                    red++;
                    break;
                case "yellow":
                    yellow++;
                    break;
                case "blue":
                    blue++;
                    break;
            }
        }
        payment.add(red);
        payment.add(yellow);
        payment.add(blue);
        return payment;
    }

    protected abstract boolean canPay(ArrayList<Integer> payment, int which);

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

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public abstract void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException, NoOwnerException;

    public abstract void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException, NoOwnerException;
}
