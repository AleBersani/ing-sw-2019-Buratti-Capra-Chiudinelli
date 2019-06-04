package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.communication.server.MultiServer;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private Map<String,ClientHandler> nicknameList = new ConcurrentHashMap<>();
    private Map<String,ClientHandler> disconnected = new ConcurrentHashMap<>();
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


    public Controller(){
        Gson gSon= new Gson();
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Configuration.json")));
        Configuration configuration = gSon.fromJson(br, Configuration.class);
        this.board=Integer.toString(configuration.board);
        this.skulls=configuration.skulls;
        this.frenzyEn=configuration.frenzy;
        this.timer=configuration.timer;
        this.mode=configuration.mode;
        this.availableBoards=configuration.availableBoards;
        this.availableSkulls=configuration.availableSkulls;
        this.availableColors=configuration.availableColors;
        this.gameStarted=false;
    }

    public void sendString(String msg, ClientHandler clientHandler) {
        clientHandler.print(msg);
    }

    public void launchServer(int port) throws IOException {
        this.server = new MultiServer(port,this);
        server.init(timer);
        server.lifeCycle();
    }

    public synchronized void understandMessage(String msg, ClientHandler clientHandler){
        String [] command = msg.split(" ");
        switch(command[0]){
            case "?": {
                sendString("help menù", clientHandler);
                break;
            }
            default: {
                sendString("This command doesn't exist, type '?' for the list of all available commands", clientHandler);
            }
        }
    }

    public void login(String command, ClientHandler clientHandler) {
        if (gameStarted) {
            if (disconnected.containsKey(command)) {
                disconnected.remove(command, clientHandler);
                nicknameList.put(command, clientHandler);
            }
            else {
                sendString(">>> Game already started",clientHandler);
            }
        }
        else {
            if (nicknameList.size() < 5) {
                boolean first = false;

                if (nicknameList.isEmpty()) {
                    first = true;
                }

                if (!clientHandler.isLogged()) {
                    if (!this.getNicknameList().containsKey(command)) {
                        this.getNicknameList().put(command, clientHandler);
                        System.out.println("<<< " + clientHandler.getSocket().getRemoteSocketAddress() + " is logged as: " + command);
                        sendString("logged as: " + command, clientHandler);
                        clientHandler.setLogged(true);
                        if (first) {
                            setGameRules(clientHandler);
                        } else {
                            clientHandler.setServiceMessage("Now you are in the waiting room");
                        }
                    } else {
                        sendString(">>> This nickname is already use", clientHandler);
                    }
                } else {
                    sendString(">>> You are already logged", clientHandler);
                }
            } else {
                sendString(">>> Game full", clientHandler);
            }
        }

    }

    private void setGameRules( ClientHandler clientHandler) {
        clientHandler.setServiceMessage("Select a board");

    }

    public void selectBoard(String msg, ClientHandler clientHandler){
        if(availableBoards.contains(Integer.parseInt(msg))) {
            board = "/Board/Board" + msg + ".json";
            sendString("You selected the board number " + msg, clientHandler);
            clientHandler.setServiceMessage("Select the number of skulls");
        }
        else{
            sendString("This board doesn't exist, please select another one", clientHandler);
        }
    }

    public void setSkulls(String msg, ClientHandler clientHandler){
        if(availableSkulls.contains(Integer.parseInt(msg))) {
            skulls = Integer.parseInt(msg);
            sendString("You selected " + msg + " skulls", clientHandler);
            clientHandler.setServiceMessage("Do you like to play with frenzy? Y/N");
        }
        else{
            sendString("Value not valid", clientHandler);
        }
    }

    public void setFrenzy(String msg, ClientHandler clientHandler){
        switch (msg){
            case "Y": {
                frenzyEn = true;
                sendString("You enabled frenzy",clientHandler);
                clientHandler.setServiceMessage("Now you are in the waiting room");
                break;
            }
            case "N": {
                frenzyEn = false;
                sendString("You disabled frenzy", clientHandler);
                clientHandler.setServiceMessage("Now you are in the waiting room");
                break;
            }
            default:{
                sendString("Please respond Y or N", clientHandler);
            }
        }
    }

    public  synchronized void waitingRoom(String msg, ClientHandler clientHandler){
        if (gameStarted){
            clientHandler.setServiceMessage("Initialize board");
        }
        String playersNames = "§§§";
        String[] allNames = nicknameList.keySet().toArray(new String[0]);
        for(String name: allNames){
            playersNames =  playersNames + "-" + name;
        }
        sendString(playersNames,clientHandler);

    }

    public synchronized void quit(ClientHandler clientHandler){
        String nickName= new String();
        clientHandler.setDisconnect(true);
        if (nicknameList.containsValue(clientHandler)){
            for(Map.Entry e : nicknameList.entrySet()){
                if (e.getValue().equals(clientHandler)){
                    nickName=(String) e.getKey();
                    if(gameStarted) {
                        disconnected.put((String) e.getKey(), (ClientHandler) e.getValue());
                    }
                    nicknameList.remove(nickName);
                }
            }
            for (ClientHandler c : nicknameList.values()){
                sendString(">>>" + nickName + " disconnected", c);
            }
            server.print(nickName+ " disconnected");
        }
    }

    public Map<String, ClientHandler> getNicknameList() {
        return nicknameList;
    }

    public void startGame() {
        Random random=new Random();
        int n;
        n=random.nextInt(nicknameList.size());
        ArrayList<Player> players;
        players=new ArrayList<>();
        for(Map.Entry e : nicknameList.entrySet()){
            players.add(new Player(false,"",(String) e.getKey()));
        }
        players.get(n).setFirst(true);
        for (Player p : players){
            p.setColor(availableColors.get(0));
            availableColors.remove(availableColors.get(0));
        }
        System.out.println("Match started");
        gameStarted=true;
        match = new Match(players, nicknameList.size(), skulls, frenzyEn, mode, board);
        match.start();
    }


    public synchronized void boardDescription( ClientHandler clientHandler) {
        sendString(boardDescriptor(),clientHandler);
        clientHandler.setServiceMessage("Initialize Players");
    }

    public synchronized void playerDescription(ClientHandler clientHandler) {
        sendString(playersDescriptor(clientHandler),clientHandler);
        clientHandler.setServiceMessage("Initialize you");
    }

    public synchronized void youDescription(ClientHandler clientHandler) {
        sendString(youDescriptor(clientHandler),clientHandler);
        clientHandler.setServiceMessage("");//TODO
    }

    private String boardDescriptor() {
        String boardDescriptor="+++Board++";
        boardDescriptor=boardDescriptor.concat(match.getBoard().getRooms().toString());
        return boardDescriptor;
    }

    private String playersDescriptor(ClientHandler current){
        String you= new String();
        if (nicknameList.containsValue(current)) {
            for (Map.Entry e : nicknameList.entrySet()) {
                if (e.getValue().equals(current)) {
                    you = (String) e.getKey();
                }
            }
        }
        ArrayList<Player> enemies= (ArrayList<Player>) this.match.getPlayers().clone();
        Player i=null;
        for (Player p : enemies){
            if(p.getNickname().equals(you)){
                i=p;
            }
        }
        enemies.remove(i);
        String playersDescriptor="+++Players";
        playersDescriptor=playersDescriptor.concat(enemies.toString());
        return playersDescriptor;
    }

    private String youDescriptor(ClientHandler you){
        String yo= new String();
        if (nicknameList.containsValue(you)) {
            for (Map.Entry e : nicknameList.entrySet()) {
                if (e.getValue().equals(you)) {
                    yo = (String) e.getKey();
                }
            }
        }
        Player y=null;
        for (Player p: match.getPlayers()){
            if(p.getNickname().equals(yo)){
                y=p;
            }
        }
        String youDescriptor="+++You++++";
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
}