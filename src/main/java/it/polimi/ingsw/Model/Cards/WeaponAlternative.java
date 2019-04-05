package it.polimi.ingsw.Model.Cards;

import java.util.ArrayList;

public class WeaponAlternative extends Weapon {

    private ArrayList<Effect> alternativeEffect= new ArrayList<Effect>();

    public WeaponAlternative(ArrayList<Effect> alternativeEffect) {
        this.alternativeEffect = alternativeEffect;
    }

    public void fireAlternative(){

        return;
    }
}
