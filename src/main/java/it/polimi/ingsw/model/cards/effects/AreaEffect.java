package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Room;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

/**
 * this class apply the Area effects
 */
public class AreaEffect extends Effect {

    /**
     * is the number of damages this effect does
     */
    private int damage;
    /**
     * is the number of marks this effect does
     */
    private int mark;
    /**
     * is the distance where this effect is applied
     */
    private int distance;

    /**
     * constructor method of AreaEffect
     * @param costBlue blue cost of the effect
     * @param costRed red cost of the effect
     * @param costYellow yellow cost of the effect
     * @param name name of the effect
     * @param constraints all of the constraint that this method has
     * @param constraintPositivity constraint positivity of each constraint
     * @param damage is the number of damages this effect does
     * @param mark is the number of marks this effect does
     * @param distance is the distance where this effect is applied
     */
    public AreaEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark, int distance) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.distance = distance;
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
            for(Room room: target.getOwner().getPosition().getRoom().getBoard().getRooms()){
                for(Square square: room.getSquares()){
                    if(square!=target.getOwner().getPosition()){
                        if(target.getOwner().getPosition().calcDist(square)<=this.distance){
                            for(Player enemy: square.getOnMe()){
                                doRealDamage(target.getOwner(),enemy,this.damage,this.mark, previousTarget);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * for this type of effects there aren't constraints
     * @param targetParameter is a target
     */
    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) {
        return;
    }
}
