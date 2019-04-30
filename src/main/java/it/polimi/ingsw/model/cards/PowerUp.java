package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

public class PowerUp {
    private String color, name;
    private Effect effect;

    public PowerUp(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public void useEffect(TargetParameter target) throws InvalidTargetException {
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
}
