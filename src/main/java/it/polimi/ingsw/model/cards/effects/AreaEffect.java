package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Room;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

public class AreaEffect extends Effect {

    private int damage,mark,distance;

    public AreaEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark, int distance) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.distance = distance;
    }

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
                                doRealDamage(target.getOwner(),enemy,this.damage,this.mark);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) {
        return;
    }
}
