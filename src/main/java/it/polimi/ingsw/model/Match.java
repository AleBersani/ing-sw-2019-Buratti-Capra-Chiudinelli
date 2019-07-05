package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Board;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the match
 */
public class Match implements Serializable {
    /**
     * This attribute is the circular list of the players
     */
    private CircularArrayList<Player> players;
    /**
     * This attribute is the number of the players that are playing
     */
    private int numPlayers;
    /**
     * This attribute is the number of skulls for this game
     */
    private int skulls;
    /**
     * This attribute is the killshot track
     */
    private ArrayList<Player> killShotTrack= new ArrayList<>();
    /**
     * This attribute is the frenzy enable setting
     */
    private boolean frenzyEn;
    /**
     * This attribute is the mode that the players are playing
     */
    private String mode;
    /**
     * This attribute is the turn of a player
     */
    private Turn turn;
    /**
     * This attribute is the board of the game
     */
    private Board board;

    /**
     * This attribute is the array list that represent if the value of blood is double
     */
    private ArrayList<Boolean> doubleOnKillShotTrack;

    /**
     * This attributes means if the game is ended or not
     */
    private boolean endgame;

    private int i;

    /**
     * This attribute is the array list of the winner players
     */
    private ArrayList<Player> winner;

    /**
     * This class is the circular array list of players
     * @param <Player> This parameter is the player list
     */
    private class CircularArrayList<Player> extends ArrayList<Player>
    {
        CircularArrayList(ArrayList<Player> players) {
            this.addAll(players);
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

    /**
     * This constructor instantiates the match
     * @param players This parameter is the list of the players that are playing
     * @param numPlayers This parameter is number of player that are playing
     * @param skulls This parameter is the number of skulls that the game'll have
     * @param frenzyEn This parameter is the frenzy enable, true if the players want the frenzy turn, false otherwise
     * @param mode This parameter is the mode that the players are playing
     * @param type This parameter is the path of the board
     */
    public Match(ArrayList<Player> players, int numPlayers, int skulls, boolean frenzyEn, String mode, String type) {
        this.players = new CircularArrayList<>(players);
        this.numPlayers = numPlayers;
        this.skulls = skulls;
        this.frenzyEn = frenzyEn;
        this.mode = mode;
        this.i=0;
        this.turn=new Turn(false,null,this);
        this.board= new Board(this, type);
        this.doubleOnKillShotTrack=new ArrayList<>();
    }

    /**
     * This method starts the game
     */
    public void start() {
        this.turn.endTurn();
    }

    /**
     * This method starts the turn
     */
    void startTurn() {
        boolean frenzy = setFrenzy();
        this.turn.reset(frenzy, players.get(i));
        i++;
        if(frenzy){
            for(Player player : this.players){
                if(player.getDamageCounter()==0 && !player.isTurnedPlank()){
                    player.setTurnedPlank(true);
                    player.setSkull(0);
                }
            }
        }
    }

    /**
     * This method sets the turn to a frenzy turn
     * @return True if the number of skulls is 0 and the frenzy enable is set on true, false otherwise
     */
    boolean setFrenzy(){
        return this.skulls == 0 && this.frenzyEn;
    }

    /**
     * This method computes the points on the killshot track and control who is the winner
     */
    public void endGame(){
        ArrayList<Player> killPlayer = new ArrayList<>();
        ArrayList<Integer> killCounter = new ArrayList<>();
        ArrayList<Player> killPriority = new ArrayList<>();
        ArrayList<Player> winPlayer = new ArrayList<>();
        int k;
        int max=0;
        int index=0;
        boolean found;
        boolean added;
        boolean equal=true;

        for (i = 0; i < this.killShotTrack.size(); i++) {
            for (k = 0,found=false; k < killPlayer.size() && !found; k++)
                if (this.killShotTrack.get(i) == killPlayer.get(k)) {
                    killCounter.set(k, killCounter.get(k) + 1);
                    found = true;
                    if(doubleOnKillShotTrack.get(k)){
                        killCounter.set(k, killCounter.get(k) + 1);
                    }
                }
            if (!found) {
                killPlayer.add(this.killShotTrack.get(i));
                killCounter.add(1);
                if(doubleOnKillShotTrack.get(k)){
                    killCounter.set(k, killCounter.get(k) + 1);
                }
            }
        }

        for(i=0;!killPlayer.isEmpty();i++) {    // SET POINT FOR ALL KILLER
            for (k = 0,max=0,index=0;k<killCounter.size(); k++)
                if (killCounter.get(k) > max) {
                    max = killCounter.get(k);
                    index = k;
                }
            killPlayer.get(index).setPoints(killPlayer.get(index).getPoints() + getTurn().calcPoints(i));
            killPriority.add(killPlayer.get(index));
            killCounter.remove(index);
            killPlayer.remove(index);
        }

        max=0;
        do {
            for (i = 0,added = false; i < this.numPlayers; i++)
                if (this.players.get(i).getPoints() >= max && !winPlayer.contains(this.players.get(i))) {
                    max = this.players.get(i).getPoints();
                    index = i;
                    added = true;
                }
            if (added)
                winPlayer.add(this.players.get(index));
        }
        while(added);

        //if there are more than one player with the same point, control and remove if there are player that didn't make a kill
        if(winPlayer.size()>1) {
            for (i = 0; i < winPlayer.size(); i++)
                if(killPriority.contains(winPlayer.get(i))) {
                    equal = false;
                    for (i = 0; i < winPlayer.size(); i++)
                        if (!killPriority.contains(winPlayer.get(i))) {
                            winPlayer.remove(i);
                            i--;
                        }
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

        this.winner= winPlayer;
        this.endgame = true;
    }

    /**
     * This method returns the board of this match
     * @return The board of this match
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method sets the board of this match
     * @param board This parameter is the board that will be played
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * This method returns the turn of the match
     * @return The turn of the match
     */
    public Turn getTurn() {
        return turn;
    }

    /**
     * This method sets the turn of the match
     * @param turn This parameter is the turn that the match'll have
     */
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    /**
     * This method returns the list of players that are playing
     * @return The list of players that the match has
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method return the number of skulls of the match
     * @return The number of the actual skulls of the match
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     * This method sets the skulls of the match
     * @param skulls This parameter is the number of skulls that the match'll have
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * This method return the killshot track of the match
     * @return The killshot track that the match'll have
     */
    public ArrayList<Player> getKillShotTrack() {
        return killShotTrack;
    }

    /**
     * This method sets the killshot track of the match
     * @param killShotTrack This parameter is the killshot track that the match'll have
     */
    void setKillShotTrack(ArrayList<Player> killShotTrack) {
        this.killShotTrack = killShotTrack;
    }

    /**
     * This method return if the frenzy turn is enable or not
     * @return The settings of the frenzy enable
     */
    boolean isFrenzyEn() {
        return frenzyEn;
    }

    /**
     * This method return the list of the double points of the killshot track
     * @return The array list of the double points of the killshot track
     */
    public ArrayList<Boolean> getDoubleOnKillShotTrack() {
        return doubleOnKillShotTrack;
    }

    /**
     * This method sets the list of the double points of the killshot track
     * @param doubleOnKillShotTrack This parameter is the array list of the double points of the killshot track
     */
    void setDoubleOnKillShotTrack(ArrayList<Boolean> doubleOnKillShotTrack) {
        this.doubleOnKillShotTrack = doubleOnKillShotTrack;
    }

    /**
     * This method return is the game is ended or not
     * @return True if the game is ended, false otherwise
     */
    public boolean isEndgame() {
        return endgame;
    }

    /**
     * This method return the array list of winner players
     * @return The array list of winner players
     */
    public ArrayList<Player> getWinner() {
        return winner;
    }
}
