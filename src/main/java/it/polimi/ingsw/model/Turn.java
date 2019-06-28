package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a turn
 */
public class Turn implements Serializable {
    /**
     * This attribute is the counter of the actions during a turn
     */
    private int actionCounter;
    /**
     * This attribute is the list of players dead in this turn
     */
    private ArrayList <Player> deads;
    /**
     * This attribute keep track if at least a player is dead in this turn
     */
    private boolean dead;
    /**
     * This attribute keep track if the turn is a frenzy turn
     */
    private boolean frenzy;
    /**
     * This attribute is the current player that is playing in this turn
     */
    private Player current;
    /**
     * Thi attribute is the match
     */
    private Match match;

    /**
     * This constructor instantiates the turn
     * @param frenzy This parameter is if the turn is frenzy or not
     * @param current This parameter is the current player that is playing
     * @param match This parameter is the match of the turn
     */
    public Turn( boolean frenzy, Player current, Match match) {
        this.frenzy = frenzy;
        this.current = current;
        this.match = match;
        this.actionCounter=0;
        this.dead=false;
        this.deads=new ArrayList<>();
    }

    /**
     * This method adds a dead player to a list
     * @param dead This parameter is the player that is dead in this turn
     */
    public void addDead(Player dead){
            this.deads.add(dead);
            this.dead=true;
    }

    /**
     * This method controls, at the end of each turn, if some ammo tiles are missing from the board, if someone is dead, and restart the turn or end the game
     */
    public void endTurn(){
        for(int i=0;i<getMatch().getBoard().getRooms().size();i++)
            for(int j=0;j<getMatch().getBoard().getRooms().get(i).getSquares().size();j++)
                while(getMatch().getBoard().getRooms().get(i).getSquares().get(j).require())
                    getMatch().getBoard().getRooms().get(i).getSquares().get(j).generate();
        if(this.dead)
            setPoint();
        if((!getMatch().isFrenzyEn() && getMatch().getSkulls()==0 )||(getMatch().isFrenzyEn() && this.frenzy && this.current.isLastKill()))
            getMatch().endGame();
        else
            getMatch().startTurn();
    }

    /**
     * This method sets the point to every player who damaged the deads on the list
     */
    public void setPoint() {
        ArrayList<Player> damagePlayer = new ArrayList<>();
        ArrayList<Integer> damageCounter = new ArrayList<>();
        int i;
        int j;
        int k;
        int index;
        int max;
        boolean found;

        if(this.deads.size()>=2)        //DOUBLE, TRIPLE, QUADRA KILL
           this.deads.get(0).getDamage().get(10).setPoints(this.deads.get(0).getDamage().get(10).getPoints() + 1);

        for(i=0;i<this.deads.size();i++) {
            for (j = 0; j < this.deads.get(i).getDamage().size(); j++) {
                found=false;
                for (k = 0; k < damagePlayer.size(); k++)
                    if(this.deads.get(i).getDamage().get(j)==damagePlayer.get(k)) {
                        damageCounter.set(k,damageCounter.get(k)+1);
                        found = true;
                    }
                if (!found){
                    damagePlayer.add(k, this.deads.get(i).getDamage().get(j));
                    damageCounter.add(k,1);
                }
            }

            getMatch().getKillShotTrack().add(this.deads.get(i).getDamage().get(10));
            if(this.deads.get(i).getDamage().size()==12 && this.deads.get(i).getDamage().get(10)==this.deads.get(i).getDamage().get(11)) {
                match.getDoubleOnKillShotTrack().add(true);
                this.deads.get(i).getDamage().get(11).marked(1,this.deads.get(i));
            }
            else {
                match.getDoubleOnKillShotTrack().add(false);
            }

            for(k=0;!damagePlayer.isEmpty();k++) {// SET POINT FOR ALL DAMAGER
                for (j = 0,max=0,index=0;j<damageCounter.size();j++)
                    if (damageCounter.get(j) > max) {
                        max = damageCounter.get(j);
                        index = j;
                    }
                int calc;
                if(!this.deads.get(i).isTurnedPlank()){
                    calc =calcPoints(this.deads.get(i).getSkull() + k); //CALC POINTS IF TURNED PLANK IS FALSE
                }
                else{
                    calc =calcPointsTurned(this.deads.get(i).getSkull() + k); //CALC POINTS IF TURNED PLANK IS TRUE
                }
                damagePlayer.get(index).setPoints(damagePlayer.get(index).getPoints() + calc);
                damageCounter.remove(index);
                damagePlayer.remove(index);
            }
            if(!this.deads.get(i).isTurnedPlank()) {
                this.deads.get(i).getDamage().get(0).setPoints(this.deads.get(i).getDamage().get(0).getPoints() + 1); //FIRSTBLOOD
            }
            this.deads.get(i).setSkull(this.deads.get(i).getSkull() + 1);

            getMatch().setSkulls(getMatch().getSkulls()-1);
            if(getMatch().getSkulls()<0){
                getMatch().setSkulls(0);
            }
        }

        for(i=0;!this.deads.isEmpty();) {
            this.deads.get(i).getDamage().clear();
            this.deads.get(i).setDamageCounter(0);
            this.deads.remove(i);
        }
        this.dead=false;
    }

    /**
     * This method calculates the right points the player has earned
     * @param skulls This parameter is the number of skulls that the player owns
     * @return The amount of points that the player has earned
     */
     int calcPoints(int skulls){
        if(skulls==0)
            return 8;
        else
            if(skulls==1)
                return 6;
            else
                if(skulls==2)
                    return 4;
                else
                    if(skulls==3)
                        return 2;
        return 1;
    }

    /**
     * This method calculates the right points the player has earned
     * @param skulls This parameter is the number of skulls that the player owns
     * @return The amount of points that the player has earned
     */
    int calcPointsTurned(int skulls){
        if(skulls==0)
            return 2;
        else
            return 1;
    }

    /**
     * This method return the actual match
     * @return The match of the turn
     */
    public Match getMatch() {
        return match;
    }

    /**
     * This method sets the match of this turn
     * @param match This parameter is the match that the turn'll have
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * This turn return the current player that is playing this turn
     * @return The player that is playing this turn
     */
    public Player getCurrent() {
        return current;
    }

    /**
     * This method return the list of dead players
     * @return The list of dead players
     */
    public ArrayList<Player> getDeads() {
        return deads;
    }

    /**
     * This method sets the list of dead players
     * @param deads This parameter is the list of dead players that will be set
     */
    public void setDeads(ArrayList<Player> deads) {
        this.deads = deads;
    }

    /**
     * This method return the number of actions done this turn
     * @return The number of actions done this turn
     */
    public int getActionCounter() {
        return actionCounter;
    }

    /**
     * This method sets the numbers of action in this turn
     * @param actionCounter This parameter is the number of action that will be set
     */
    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    /**
     * This method return is the turn is frenzy or not
     * @return True is the turn is a frenzy turn, false otherwise
     */
    public boolean isFrenzy() {
        return frenzy;
    }

    /**
     * This method sets a turn to frenzy or not
     * @param frenzy This parameter is true if the turn'll be frenzy, false otherwise
     */
    public void setFrenzy(boolean frenzy) {
        this.frenzy = frenzy;
    }

    public void reset( boolean frenzy, Player current) {
        this.frenzy = frenzy;
        this.current = current;
        this.actionCounter=0;
        this.dead=false;
        this.deads=new ArrayList<>();
    }
}
