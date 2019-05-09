package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.effects.Effect;

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

    public void fireAlternative(ArrayList<TargetParameter> target) throws InvalidTargetException, NoOwnerException {
        int min = calcMinim(target,alternativeEffect.size());
        for(int i=0;i<min;i++){
            alternativeEffect.get(i).apply(target.get(i), this.getPreviousTarget());
        }

        return;
    }

    @Override
    protected boolean canPay(ArrayList<Integer> payment, int which){
        return this.alternativeEffect.get(0).getCostBlue()==payment.get(2) && this.alternativeEffect.get(0).getCostRed()==payment.get(0) && this.alternativeEffect.get(0).getCostYellow()==payment.get(1);

    }
}
