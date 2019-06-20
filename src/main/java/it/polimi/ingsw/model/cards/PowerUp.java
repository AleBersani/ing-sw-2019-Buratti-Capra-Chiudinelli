package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

import java.io.Serializable;

public class PowerUp implements Serializable {
    private String color, name;
    private Effect effect;
    private boolean onResponse;

    public PowerUp(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public void useEffect(TargetParameter target) throws InvalidTargetException, NoOwnerException {
        effect.apply(target,null);
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public boolean getOnResponse() {
        return onResponse;
    }

}
