package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Board;

import java.util.ArrayList;

public class Match {

    private CircularArrayList<Player> players;
    private int numPlayers,skulls;
    private ArrayList<Player> killShotTrack= new ArrayList<Player>();
    private boolean frenzyEn;
    private String mode;
    private Turn turn;
    private Board board;
    int i;

    private class CircularArrayList<Player> extends ArrayList<Player>
    {
        public CircularArrayList(ArrayList<Player> players) {
            for(Player p : players)
                this.add(p);
        }

        public Player get(int index)
        {
            if (index >= players.size())
            {
                index = index%players.size() ;
            }
            return super.get(index);
        }
    }

    public Match(ArrayList<Player> players, int numPlayers, int skulls, boolean frenzyEn, String mode) {
        this.players = new CircularArrayList<>(players);
        this.numPlayers = numPlayers;
        this.skulls = skulls;
        this.frenzyEn = frenzyEn;
        this.mode = mode;
        i=0;
    }

    public void start() {
        getTurn().endTurn();
    }

    public void startTurn() {
        this.turn = new Turn(this.players.get(i+1),this.setFrenzy(),this.players.get(i),this);
        /* TODO PUT IT IN CONTROLLER
        if(getTurn().getCurrent().getPosition() == null)
            throw new SpawnNeedException();
         */
        i++;
    }

    public boolean setFrenzy(){
        if(this.skulls==0)
            return true;
        return false;
    }

    public void endGame(){
        ArrayList<Player> killPlayer = new ArrayList<>();
        ArrayList<Integer> killCounter = new ArrayList<>();
        ArrayList<Player> killPriority = new ArrayList<>();
        ArrayList<Player> winPlayer = new ArrayList<>();
        int i;
        int k;
        int max=0;
        int index=0;
        boolean found;
        boolean added;
        boolean equal=true;

        if (getTurn().getCurrent().isLastKill() && getTurn().isFrenzy()) {
            for (i = 0, found = false; i < this.killShotTrack.size(); i++) {
                for (k = 0; k < killPlayer.size(); k++)
                    if (this.killShotTrack.get(i) == killPlayer.get(k)) {
                        killCounter.set(k, killCounter.get(k) + 1);
                        found = true;
                    }
                if (!found) {
                    killPlayer.add(k, this.killShotTrack.get(i));
                    killCounter.add(k, 1);
                }
            }

            for(i=0;killPlayer.isEmpty();i++) {    // SET POINT FOR ALL KILLER
                for (k = 0;k<killCounter.size(); k++)
                    if (killCounter.get(k) > max) {
                        max = killCounter.get(k);
                        index = k;
                    }
                killPlayer.get(index).setPoints(killPlayer.get(index).getPoints() + getTurn().calcPoints(i));
                killPriority.add(killPlayer.get(index));
                killCounter.remove(index);
                killPlayer.remove(index);
            }

            do {
                for (i = 0,added=false; i < this.numPlayers; i++)
                    if (this.players.get(i).getPoints() >=  max && !winPlayer.contains(this.players.get(i))) {
                        max = this.players.get(i).getPoints();
                        index = i;
                        added=true;
                    }
                if(added)
                    winPlayer.add(this.players.get(index));
            }
            while(added);

            //if there are more than one player with the same point, control and remove if there are player that didn't make a kill
            if(winPlayer.size()>1) {
                for (i = 0; i < winPlayer.size(); i++)
                    if (!killPriority.contains(winPlayer.get(i))) {
                        winPlayer.remove(i);
                        i--;
                        equal = false;
                    }

                if(!equal) {
                    found = false;
                    for (i = 0; i < killPriority.size() && !found; i++)
                        if (winPlayer.contains(killPriority.get(i))) {
                            found = true;
                            winPlayer.clear();
                            winPlayer.add(killPriority.get(i));
                        }
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getSkulls() {
        return skulls;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public ArrayList<Player> getKillShotTrack() {
        return killShotTrack;
    }

    public void setKillShotTrack(ArrayList<Player> killShotTrack) {
        this.killShotTrack = killShotTrack;
    }

    public boolean isFrenzyEn() {
        return frenzyEn;
    }
}
