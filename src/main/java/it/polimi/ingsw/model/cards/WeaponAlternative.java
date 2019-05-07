package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoAmmoException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class WeaponAlternative extends Weapon {

    private ArrayList<Effect> alternativeEffect;

    public WeaponAlternative(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<Effect> alternativeEffect) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.alternativeEffect=alternativeEffect;
    }


    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    public void fireAlternative(ArrayList<TargetParameter> target) throws InvalidTargetException, NoAmmoException {
        Player owner = target.get(0).getOwner();
        Effect effect = alternativeEffect.get(0);
        if((owner.getRedAmmo()<effect.getCostRed())||(owner.getBlueAmmo()<effect.getCostBlue())||(owner.getYellowAmmo()<effect.getCostYellow())){
            throw new NoAmmoException();
        }
        for(int i=0;i<alternativeEffect.size();i++){
            alternativeEffect.get(i).apply(target.get(i), this.getPreviousTarget());
        }
        this.pay(owner,effect);
        return;
    }
}
