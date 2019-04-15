package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Cards.Effects.Effect;

public class PowerUp {
    private String color, name;
    private Effect effect;

    public void useEffect(){
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
}
