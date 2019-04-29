package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class EffectsVsDirection extends Effect {

    private ArrayList<Integer> damageList;
    private ArrayList<Integer> markList;

    public EffectsVsDirection(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Integer> damageList, ArrayList<Integer> markList) {
        super(costBlue, costRed, costYellow, name, constraints);
        this.damageList = damageList;
        this.markList = markList;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetException {
        int i;
        if(!constraintsCheck(target)){
            throw new InvalidTargetException();
        }
        else{
//TODO possibile modifica che suddivide l'effetto contro i giocatori in due effetti vs Player con costraint sameDirection sul secondo
            for(i=0;i<target.getDirectionSquare().size();i++){
                for(Player player: target.getDirectionSquare().get(i).getOnMe()){
                    if(player!=target.getOwner()){
                        player.wound(this.damageList.get(i),target.getOwner());
                        player.marked(this.markList.get(i),target.getOwner());
                    }
                }
            }
            for(i=0;i<target.getDirectionPlayer().size();i++){
                target.getDirectionPlayer().get(i).wound(this.damageList.get(i),target.getOwner());
                target.getDirectionPlayer().get(i).marked(this.markList.get(i),target.getOwner());
            }
        }
    }
}
