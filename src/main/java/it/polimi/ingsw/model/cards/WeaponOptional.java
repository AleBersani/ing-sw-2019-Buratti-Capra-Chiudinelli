package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoAmmoException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class WeaponOptional extends Weapon {

    private ArrayList<ArrayList<Effect>> optionalEffect= new ArrayList<ArrayList<Effect>>();

    public WeaponOptional(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<ArrayList<Effect>> optionalEffect) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.optionalEffect=optionalEffect;
    }

    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws InvalidTargetException, NoAmmoException {
        Player owner = target.get(0).getOwner();
        Effect effect = optionalEffect.get(which).get(0);
        if((owner.getRedAmmo()<effect.getCostRed())||(owner.getBlueAmmo()<effect.getCostBlue())||(owner.getYellowAmmo()<effect.getCostYellow())){
            throw new NoAmmoException();
        }
        for(int i=0;i<optionalEffect.get(which).size();i++){
            optionalEffect.get(which).get(i).apply(target.get(i),this.getPreviousTarget());
        }
        this.pay(owner,effect);
        return;
    }

    @Override
    public void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

}
