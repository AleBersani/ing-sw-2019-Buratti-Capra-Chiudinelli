package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoAmmoException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class represent a single weapon
 */
public abstract class Weapon implements Serializable {
    /**
     * color of the weapon
     */
    private String color;
    /**
     * name of the weapon
     */
    private String name;
    /**
     * blue cost of the weapon
     */
    private int costBlue;
    /**
     * red cost of the weapon
     */
    private int costRed;
    /**
     * yellow cost of the weapon
     */
    private int costYellow;
    /**
     * if the weapon is loaded or not
     */
    private boolean load;
    /**
     * list of weapon's effects
     */
    private ArrayList<Effect> effect;
    /**
     * list of previous target shooted with this weapon in this turn
     */
    private ArrayList<ArrayList<Player>> previousTarget;
    /**
     * reference to the owner of this weapon
     */
    private Player owner;

    /**
     * constructor method of Weapon
     * @param color color of the weapon
     * @param name name of the weapon
     * @param costBlue blue cost of the weapon
     * @param costRed red cost of the weapon
     * @param costYellow yellow cost of the weapon
     * @param effect list of weapon's effects
     */
    public Weapon(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect) {
        this.color = color;
        this.name = name;
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.effect = effect;
        load=true;
        owner=null;
        previousTarget= new ArrayList<>();
        previousTarget.add(new ArrayList<Player>());
        previousTarget.add(new ArrayList<Player>());
    }

    /**
     * this method uses all the effects in the attribute effect
     * @param target are the targets of the fire
     * @throws InvalidTargetException if at least one of the targets is invalid
     * @throws NoOwnerException when in at least one of the targets there is not the owner or owner position
     */
    public void fire(ArrayList<TargetParameter> target) throws InvalidTargetException, NoOwnerException {
        int min = calcMinim(target,effect.size());
        for(int i=0;i<min;i++){
                effect.get(i).apply(target.get(i), this.previousTarget);
        }
    }

    /**
     * utility method for measure the minimum between the targets and the effects
     * @param target are the targets of the fire
     * @param effectSize the number of effects
     * @return minimum between the targets and the effects
     * @throws InvalidTargetException when there is no targets
     */
    int calcMinim(ArrayList<TargetParameter> target, int effectSize) throws InvalidTargetException {
        int min = effectSize;
        if(target==null){
            throw new InvalidTargetException();
        }
        if(target.size()<effectSize){
            min = target.size();
        }
        return min;
    }

    void pay(Player owner, int red, int yellow, int blue, ArrayList<PowerUp> powerUps, int which) throws NoAmmoException {
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

    private ArrayList<Integer> powerUpToPayment(ArrayList<PowerUp> powerUps, int red, int yellow, int blue){
        ArrayList<Integer> payment=new ArrayList<>();
        for (PowerUp p : powerUps){
            switch (p.getColor()) {
                case "red": {
                    red++;
                    break;
                }
                case "yellow": {
                    yellow++;
                    break;
                }
                case "blue":{
                    blue++;
                    break;
                }
                default:
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

    @Override
    public String toString(){
        return name;
    }

    public abstract boolean isOptional();

    public abstract boolean isAlternative();
}
