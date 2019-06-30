package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;

/**
 * class of power up
 */
public class PowerUp implements Serializable {
    /**
     * color of the power up
     */
    private String color;
    /**
     * name of the power up
     */
    private String name;
    /**
     * effect of the power up
     */
    private Effect effect;
    /**
     * if this power up must be used in response of a specific action
     */
    private boolean onResponse;

    /**
     * constructor method of Power Up
     * @param color color of the power up
     * @param name name of th power up
     */
    public PowerUp(String color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     * method to apply the effect of the power up if there is all target needed
     * @param target target parameters from the client
     * @throws InvalidTargetException when the parameters of the target are wrong for this type of power up
     * @throws NoOwnerException when in the target parameters is missing the owner
     */
    public void useEffect(TargetParameter target) throws InvalidTargetException, NoOwnerException {
        effect.apply(target,null);
    }

    /**
     * getter method of color
     * @return a color
     */
    public String getColor() {
        return color;
    }

    /**
     * getter method of name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * getter method of effect
     * @return the effect
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * setter method of effect
     * @param effect the effect to set
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    /**
     * getter method of on response
     * @return on response
     */
    public boolean getOnResponse() {
        return onResponse;
    }

}
