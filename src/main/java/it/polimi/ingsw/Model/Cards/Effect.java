package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public abstract class Effect {

    private int costBlue;
    private int costRed;
    private int costYellow;
    private String name;
    private ArrayList<Constraint> constraints= new ArrayList<Constraint>();
    private ArrayList<Boolean> constraintPositivity= new ArrayList<Boolean>();

    public Effect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity) {
        this.costBlue = costBlue;
        this.costRed = costRed;
        this.costYellow = costYellow;
        this.name = name;
        this.constraints = constraints;
        this.constraintPositivity = constraintPositivity;
    }
/*
    public boolean constraintsCheck(Square targetSquare, Square targetSquare2, Square targetSquare3, Room targetRoom, Player owner){
        int i;
        for(i=0; i < this.constraints.size(); i++){
            if(constraints.get(i).getClass() == MinimumDistance.class){
                if(this.constraintPositivity.get(i) != ((MinimumDistance) constraints.get(i)).canShoot(targetSquare,owner)){
                    return false;
                }
            }
            if(constraints.get(i).getClass() == See.class){
                if(this.constraintPositivity.get(i) != ((See) constraints.get(i)).canShoot(targetSquare,owner)){
                    return false;
                }
            }
            if(constraints.get(i).getClass() == SameRoom.class){
                if(this.constraintPositivity.get(i) != ((SameRoom) constraints.get(i)).canShoot(targetRoom,owner)){
                    return false;
                }
            }
            if(constraints.get(i).getClass() == SameDirection.class){
                if(this.constraintPositivity.get(i) != ((SameDirection) constraints.get(i)).canShoot(targetSquare,targetSquare2,owner)){
                    return false;
                }

            }
        }
        return true;
    }
*/
    public int getCostBlue() {
        return costBlue;
    }

    public int getCostRed() {
        return costRed;
    }

    public int getCostYellow() {
        return costYellow;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public ArrayList<Boolean> getConstraintPositivity() {
        return constraintPositivity;
    }
}
