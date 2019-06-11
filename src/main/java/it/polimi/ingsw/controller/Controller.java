package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.communication.server.MultiServer;
import it.polimi.ingsw.exception.MaxHandSizeException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.map.Square;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private static final int MAX_ACTIONS = 2;
    private Map<String,ClientInfo> nicknameList = new ConcurrentHashMap<>();
    private ArrayList<String> disconnected = new ArrayList<>();
    private MultiServer server;
    private int skulls;
    private boolean frenzyEn;
    private String board;
    private int timer;
    private String mode;
    private ArrayList<Integer> availableBoards;
    private ArrayList<Integer> availableSkulls;
    private ArrayList<String> availableColors;
    private boolean gameStarted;
    private Match match;
    private final Object nicknameListLock;
    private static final int ETIQUETTE = 4;
    private static final int MAX_PLAYERS_NUMBER = 5;

    public Controller(){
        Gson gSon= new Gson();
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Configuration.json")));
        Configuration configuration = gSon.fromJson(br, Configuration.class);
        this.board="/Board/Board" +Integer.toString(configuration.board)+ ".json";
        this.skulls=configuration.skulls;
        this.frenzyEn=configuration.frenzy;
        this.timer=configuration.timer;
        this.mode=configuration.mode;
        this.availableBoards=configuration.availableBoards;
        this.availableSkulls=configuration.availableSkulls;
        this.availableColors=configuration.availableColors;
        this.gameStarted=false;
        this.nicknameListLock = new Object();
    }

    public void sendString(String msg, ClientHandler clientHandler) {
        clientHandler.print(msg);
    }

    public void launchServer(int port) throws IOException {
        this.server = new MultiServer(port,this);
        server.init(timer);
        server.lifeCycle();
    }

    public void understandMessage(String msg, ClientHandler clientHandler){
        if(msg.startsWith("LOG-")){
            this.login(msg.substring(ETIQUETTE),clientHandler);
        }
        else {
            switch (getNicknameList().get(clientHandler.getName()).state) {
                case LOGIN: {
                    if (msg.startsWith("SET-")) {
                        this.understandSettingMessages(msg.substring(ETIQUETTE), clientHandler);
                    } else {
                        sendString("Wrong Etiquette", clientHandler);
                    }
                    break;
                }
                case GAME: {
                    if (msg.startsWith("SPW-")){
                        spawn(clientHandler, match.getTurn().getCurrent(), Integer.parseInt(msg.substring(ETIQUETTE)));
                    }
                    //TODO gestire spawn in end

                    //TODO
                    break;
                }
            }
        }
    }

    public void login(String command, ClientHandler clientHandler) {
        if (gameStarted) {
            if (this.disconnected.contains(command)) {
                this.disconnected.remove(command);
                this.nicknameList.put(command, new ClientInfo(clientHandler, ClientInfo.State.GAME));
            }
            else {
                sendString(">>> Game already started",clientHandler);
            }
        }
        else {
            if (getNicknameList().size() < MAX_PLAYERS_NUMBER) {
                boolean first = false;

                if (getNicknameList().isEmpty()) {
                    first = true;
                }

                if (!clientHandler.isLogged()) {
                    if (!getNicknameList().containsKey(command)) {
                        clientHandler.setName(command);
                        getNicknameList().put(command, new ClientInfo(clientHandler));
                        System.out.println("<<< " + clientHandler.getSocket().getRemoteSocketAddress() + " is logged as: " + command);
                        sendString("logged as: " + command, clientHandler);
                        clientHandler.setLogged(true);
                        if (first) {
                            setGameRules(clientHandler);
                        }
                        else{
                            sendString("Now you are in the waiting room", clientHandler);
                            getNicknameList().get(command).nextState();
                            for(ClientInfo clientInfo: getNicknameList().values()){
                                if(clientInfo.state.equals(ClientInfo.State.WAIT)){
                                    this.waitingRoom(clientInfo.clientHandler);
                                }
                            }
                        }
                    }
                    else {
                        sendString(">>> This nickname is already use", clientHandler);
                    }
                }
                else {
                    sendString(">>> You are already logged", clientHandler);
                }
            }
            else {
                sendString(">>> Game full", clientHandler);
            }
        }

    }

    private void setGameRules( ClientHandler clientHandler) {
        sendString("setting",clientHandler);
        sendString("Select a board-".concat(availableBoards.toString()),clientHandler); //Select a board[1,2,3,4]
    }

    private void understandSettingMessages(String msg, ClientHandler clientHandler){
        switch (msg.substring(0,ETIQUETTE)){
            case "BRD-":{ //Board
                this.selectBoard(msg.substring(ETIQUETTE),clientHandler);
                break;
            }
            case "SKL-":{ //Skull
                this.setSkulls(msg.substring(ETIQUETTE),clientHandler);
                break;
            }
            case "FRZ-":{ //Frenzy
                this.setFrenzy(msg.substring(ETIQUETTE),clientHandler);
                break;
            }
            default:{
                sendString("ERROR",clientHandler);
            }
        }
    }

    public void selectBoard(String msg, ClientHandler clientHandler){
        if(availableBoards.contains(Integer.parseInt(msg))) {
            board = "/Board/Board" + msg + ".json";
            sendString("You selected the board number " + msg, clientHandler);
            sendString("Select the number of skulls-".concat(availableSkulls.toString()), clientHandler);
        }
        else{
            sendString(">>>This board doesn't exist, please select another one", clientHandler);
        }
    }

    public void setSkulls(String msg, ClientHandler clientHandler){
        if(availableSkulls.contains(Integer.parseInt(msg))) {
            skulls = Integer.parseInt(msg);
            sendString("You selected " + msg + " skulls", clientHandler);
            sendString("Do you like to play with frenzy? Y/N", clientHandler);
        }
        else{
            sendString(">>>Value not valid", clientHandler);
        }
    }

    public void setFrenzy(String msg, ClientHandler clientHandler){
        switch (msg){
            case "Y": {
                frenzyEn = true;
                sendString("You enabled frenzy",clientHandler);
                getNicknameList().get(clientHandler.getName()).nextState();
                sendString("Now you are in the waiting room", clientHandler);
                this.waitingRoom(clientHandler);
                break;
            }
            case "N": {
                frenzyEn = false;
                sendString("You disabled frenzy", clientHandler);
                getNicknameList().get(clientHandler.getName()).nextState();
                sendString("Now you are in the waiting room", clientHandler);
                this.waitingRoom(clientHandler);
                break;
            }
            default:{
                sendString(">>>Please respond Y or N", clientHandler);
            }
        }
    }

    public void waitingRoom(ClientHandler clientHandler){
        String playersNames = "§§§";
        String[] allNames = getNicknameList().keySet().toArray(new String[0]);
        for(String name: allNames){
            playersNames = playersNames.concat("-" + name);
        }
        sendString(playersNames,clientHandler);

    }

    public void quit(ClientHandler clientHandler){
        clientHandler.setDisconnect(true);
        if(gameStarted) {
            disconnected.add(clientHandler.getName());
        }
        getNicknameList().remove(clientHandler.getName());
        for (ClientInfo clientInfo : getNicknameList().values()){
            sendString(">>>" + clientHandler.getName() + " disconnected", clientInfo.clientHandler);
            if(clientInfo.state.equals(ClientInfo.State.WAIT)){
                this.waitingRoom(clientInfo.clientHandler);
            }
        }
        server.print(clientHandler.getName() + " disconnected");
    }

    public void startGame() {
        Random random=new Random();
        int n;
        n=random.nextInt(getNicknameList().size());
        ArrayList<Player> players;
        players=new ArrayList<>();
        for(Map.Entry e : getNicknameList().entrySet()){
            players.add(new Player(false,"",(String) e.getKey()));
        }
        players.get(n).setFirst(true);
        for (Player p : players){
            p.setColor(availableColors.get(0));
            availableColors.remove(availableColors.get(0));
        }
        System.out.println("Match started");
        gameStarted=true;
        match = new Match(players, getNicknameList().size(), skulls, frenzyEn, mode, board);
        match.start();
        for(ClientInfo clientInfo: getNicknameList().values()){
            sendString("Match started",clientInfo.clientHandler);
            sendString(boardDescriptor(),clientInfo.clientHandler);
            sendString(playersDescriptor(clientInfo.clientHandler),clientInfo.clientHandler);
            sendString(youDescriptor(clientInfo.clientHandler),clientInfo.clientHandler);
            sendString(killshotTrackDescriptor(),clientInfo.clientHandler);
            clientInfo.nextState();
            clientInfo.clientHandler.setYourTurn(false);
            if(match.getTurn().getCurrent().getNickname().equals(clientInfo.clientHandler.getName())){
                clientInfo.clientHandler.setYourTurn(true);
                lifeCycle(clientInfo.clientHandler,match.getTurn().getCurrent());
            }
        }


    }

    private void lifeCycle(ClientHandler actual,Player player ) {
        if(player.getPosition()==null){
            try {
                player.draw();
                player.draw();
            } catch (MaxHandSizeException e) {
            }
            sendString("Select a power up"+player.getPowerUps().toString(),actual);
        }
        else {
            if(match.getTurn().getActionCounter()<MAX_ACTIONS) {
                sendString("Insert a command", actual);
            }
            else {
                //TODO
            }
        }
    }

    private void spawn(ClientHandler actual, Player player,int powerUpPosition) {
        PowerUp powerUp= player.getPowerUps().get(powerUpPosition);
        Square spawningPosition;
        try {
            spawningPosition = match.getBoard().findSpawnPoint(powerUp.getColor());
            player.discard(powerUp);
            player.setPosition(spawningPosition);
        } catch (NotFoundException e) {
            sendString("SpawnPoint not found", actual);
        }
        lifeCycle(actual,player);
    }

    private String killshotTrackDescriptor() {
        String killshotTrackDescriptor= "KLL-";
        for (Player p : match.getKillShotTrack()) {
            killshotTrackDescriptor = killshotTrackDescriptor.concat(p.getColor()).concat(".");
        }
        killshotTrackDescriptor=killshotTrackDescriptor.concat(";").concat(match.getDoubleOnKillShotTrack().toString());
        return killshotTrackDescriptor;
    }

    private String boardDescriptor() {
        String boardDescriptor="BRD-";
        boardDescriptor=boardDescriptor.concat(match.getBoard().getRooms().toString());
        return boardDescriptor;
    }

    private String playersDescriptor(ClientHandler current){
        String you= current.getName();
        ArrayList<Player> enemies= (ArrayList<Player>) this.match.getPlayers().clone();
        Player i=null;
        for (Player p : enemies){
            if(p.getNickname().equals(you)){
                i=p;
            }
        }
        enemies.remove(i);
        String playersDescriptor="PLR-";
        playersDescriptor=playersDescriptor.concat(enemies.toString());
        return playersDescriptor;
    }

    private String youDescriptor(ClientHandler current){
        String you= current.getName();
        Player y=null;
        for (Player p: match.getPlayers()){
            if(p.getNickname().equals(you)){
                y=p;
            }
        }
        String youDescriptor="YOU-";
        youDescriptor=youDescriptor.concat(y.describe());
        return youDescriptor;
    }

    private class Configuration{
        private int board;
        private int skulls;
        private boolean frenzy;
        private int timer;
        private ArrayList<Integer> availableBoards;
        private ArrayList<Integer> availableSkulls;
        private String mode;
        private ArrayList<String> availableColors;
    }

    public Map<String, ClientInfo> getNicknameList() {
        synchronized (nicknameListLock){
            return nicknameList;
        }
    }
}