package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.TargetParameter;

public class PowerUp {
    private String color, name;
    private Effect effect;

    public PowerUp(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public void useEffect(TargetParameter target) throws InvalidTargetException {
        effect.apply(target);
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
