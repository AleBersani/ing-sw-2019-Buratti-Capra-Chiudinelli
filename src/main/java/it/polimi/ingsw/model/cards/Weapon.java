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

//TODO questi metodi sono da revisionare
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

    /**
     * abstract method to verify if owner can pay the payment
     * @param payment the cost in the different ammo amounts
     * @param which which effect
     * @return true if the owner can pay
     */
    protected abstract boolean canPay(ArrayList<Integer> payment, int which);

    /**
     * set load to true
     */
    public void reload(){
        this.load=true;
    }

    /**
     * getter method of color
     * @return the color of the weapon
     */
    public String getColor() {
        return color;
    }

    /**
     * getter method of name
     * @return the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * getter method of cost blue
     * @return the blue cost of the weapon
     */
    public int getCostBlue() {
        return costBlue;
    }

    /**
     * getter method of cost red
     * @return the red cost of the weapon
     */
    public int getCostRed() {
        return costRed;
    }

    /**
     * getter method of cost yellow
     * @return the yellow cost of the weapon
     */
    public int getCostYellow() {
        return costYellow;
    }

    /**
     * getter method of load
     * @return true or false
     */
    public boolean isLoad() {
        return load;
    }

    /**
     * setter method of load
     * @param load true or false
     */
    public void setLoad(boolean load) {
        this.load = load;
    }

    /**
     * getter method of effect
     * @return the list of effects
     */
    public ArrayList<Effect> getEffect() {
        return effect;
    }

    /**
     * getter method of previous target
     * @return the lists of previous targets
     */
    public ArrayList<ArrayList<Player>> getPreviousTarget() {
        return previousTarget;
    }

    /**
     * getter method of owner
     * @return the owner of thw weapon
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * setter method of owner
     * @param owner the player that take this weapon
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * apply the effects of the optional fire
     * @param target are the targets of the fire
     * @param which which optional effect is going to be apply
     * @throws NotThisKindOfWeapon this is not a optional weapon
     * @throws InvalidTargetException if at least one of the targets is invalid
     * @throws NoAmmoException the owner don't have enough ammo to pay this effect
     * @throws NoOwnerException when in at least one of the targets there is not the owner or owner position
     */
    public abstract void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException, NoOwnerException;

    /**
     * apply the effects of the alternative mode
     * @param target are the targets of the fire
     * @throws NotThisKindOfWeapon this is not a optional weapon
     * @throws InvalidTargetException if at least one of the targets is invalid
     * @throws NoAmmoException the owner don't have enough ammo to pay this effect
     * @throws NoOwnerException when in at least one of the targets there is not the owner or owner position
     */
    public abstract void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException, NoOwnerException;

    /**
     * create a string whit the name of the weapon
     * @return name
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * request if this weapon is an optional weapon
     * @return true if the weapon is an optional weapon
     */
    public abstract boolean isOptional();

    /**
     * request if this weapon is an alternative weapon
     * @return true if the weapon is an alternative weapon
     */
    public abstract boolean isAlternative();
}
