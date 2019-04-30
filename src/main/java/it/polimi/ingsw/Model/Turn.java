package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Turn {

    private Player next;
    private int actionCounter;
    private ArrayList <Player> deads;
    private boolean dead;
    private boolean frenzy;
    private Player current;
    private Match match;

    public Turn(Player next, boolean frenzy, Player current, Match match) {
        this.next = next;
        this.frenzy = frenzy;
        this.current = current;
        this.match = match;
        this.actionCounter=0;
        this.dead=false;
        this.deads=new ArrayList<>();
    }

    public void addDead(Player dead){
            this.deads.add(dead);
            this.dead=true;
    }

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
                getMatch().getKillShotTrack().add(this.deads.get(i).getDamage().get(11));
                this.deads.get(i).getDamage().get(11).marked(1,this.deads.get(i));
            }

            this.deads.get(i).getDamage().get(0).setPoints(this.deads.get(i).getDamage().get(0).getPoints() + 1); //FIRSTBLOOD

            for(k=0;!damagePlayer.isEmpty();k++) {// SET POINT FOR ALL DAMAGER
                for (j = 0,max=0,index=0;j<damageCounter.size();j++)
                    if (damageCounter.get(j) > max) {
                        max = damageCounter.get(j);
                        index = j;
                    }
                damagePlayer.get(index).setPoints(damagePlayer.get(index).getPoints() + calcPoints(this.deads.get(i).getSkull() + k));
                damageCounter.remove(index);
                damagePlayer.remove(index);
            }

            this.deads.get(i).setSkull(this.deads.get(i).getSkull() + 1);
            getMatch().setSkulls(getMatch().getSkulls()-1);
        }


        for(i=0;!this.deads.isEmpty();) {
            this.deads.get(i).getDamage().clear();
            //this.deads.get(i).spawn(); TODO SPAWN TO CONTROLLER
            this.deads.remove(i);
        }

    }

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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getCurrent() {
        return current;
    }

    public ArrayList<Player> getDeads() {
        return deads;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    public boolean isFrenzy() {
        return frenzy;
    }

    public void setDeads(ArrayList<Player> deads) {
        this.deads = deads;
    }
}
