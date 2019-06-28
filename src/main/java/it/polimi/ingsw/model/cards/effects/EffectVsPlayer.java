package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class apply the effects vs a single player
 */
public class EffectVsPlayer extends Effect {
    /**
     * is the number of damages this effect does
     */
    private int damage;
    /**
     * is the number of marks this effect does
     */
    private int mark;
    /**
     * true if the enemy is needed to be added to the previous target list at level 0
     */
    private boolean addToList;
    /**
     * true if the enemy is needed to be removed from the previous target list at level 0, and added at level 1
     */
    private boolean removeFromList;

    /**
     * constructor method of EffectVsPlayer
     * @param costBlue blue cost of the effect
     * @param costRed red cost of the effect
     * @param costYellow yellow cost of the effect
     * @param name name of the effect
     * @param constraints all of the constraint that this method has
     * @param constraintPositivity constraint positivity of each constraint
     * @param damage is the number of damages this effect does
     * @param mark is the number of marks this effect does
     * @param addToList true if the enemy is needed to be added to the previous target list at level 0
     * @param removeFromList true if the enemy is needed to be removed from the previous target list at level 0, and added at level 1
     */
    public EffectVsPlayer(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int damage, int mark, boolean addToList, boolean removeFromList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.addToList = addToList;
        this.removeFromList = removeFromList;
    }

    /**
     * getter method of addToList
     * @return addToList
     */
    public boolean isAddToList() {
        return addToList;
    }

    /**
     * getter method of removeFromList
     * @return removeFromList
     */
    public boolean isRemoveFromList() {
        return removeFromList;
    }

    /**
     * this method apply the effect after checking the constraint
     * @param target is a TargetParameter
     * @param previousTarget is the list of targets of the previous effects
     * @throws InvalidTargetException when not all of the constraints return true
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            if(target.getEnemyPlayer()==target.getOwner()){
                throw new InvalidTargetException();
            }
            doRealDamage(target.getOwner(),target.getEnemyPlayer(),this.damage,this.mark);
            previousMan(previousTarget,target.getEnemyPlayer(),this.addToList,this.removeFromList);
        }
    }

    /**
     * generate the constraintSquare for the constraints
     * @param targetParameter is a target
     */
    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) throws InvalidTargetException {
        if(targetParameter.getEnemyPlayer()==null){
            throw new InvalidTargetException();
        }
        targetParameter.setConstraintSquare(targetParameter.getEnemyPlayer().getPosition());
    }
}
