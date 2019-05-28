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
        boolean first=false;
        if (disconnected.containsKey(command)){
            nicknameList.put(command,clientHandler);
            disconnected.remove(command,clientHandler);
        }
        if(nicknameList.isEmpty()){
            first=true;
        }

        if (!clientHandler.isLogged()) {
            if (!this.getNicknameList().containsKey(command)) {
                this.getNicknameList().put(command, clientHandler);
                System.out.println("<<< " + clientHandler.getSocket().getRemoteSocketAddress() + " is logged as: " + command);
                sendString(">>> logged as: " + command, clientHandler);
                clientHandler.setLogged(true);
                if (first){
                    setGameRules(clientHandler);
                }
                else {
                    clientHandler.setServiceMessage("Now you are in the waiting room");
                }
            }
            else {
                sendString(">>> " + command + " is already use, choose another nickname", clientHandler);
            }
        }
        else{
            sendString(">>> You are already logged", clientHandler);
        }

    }

    private void setGameRules( ClientHandler clientHandler) {
        clientHandler.setServiceMessage("Select a board");

    }

    public void selectBoard(String msg, ClientHandler clientHandler){
        //TODO controllare
        board="/Board/Board"+msg;
        sendString("You selected the board number "+msg, clientHandler);
        clientHandler.setServiceMessage("Select the number of skulls");
    }

    public void setSkulls(String msg, ClientHandler clientHandler){
        //TODO controllare
        skulls=Integer.parseInt(msg);
        sendString("You selected "+msg+" skulls",clientHandler);
        clientHandler.setServiceMessage("Do you like to play with frenzy? Y/N");
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

    public void waitingRoom(String msg, ClientHandler clientHandler){
        if(msg.equals("quit")){
            this.quit(clientHandler);
        }
        else{
            String playersNames = new String();
            String[] allNames = nicknameList.keySet().toArray(new String[0]);
            for(String name: allNames){
                playersNames =  name + "-" + playersNames;
            }
            sendString(playersNames,clientHandler);
        }
    }

    public void quit(ClientHandler clientHandler){
        String nickName;
        nickName="gigi";
        clientHandler.setDisconnect(true);
        if (nicknameList.containsValue(clientHandler)){
            for(Map.Entry e : nicknameList.entrySet()){
                if (e.getValue().equals(clientHandler)){
                    nickName=(String) e.getKey();
                    disconnected.put((String) e.getKey(),(ClientHandler) e.getValue());
                    nicknameList.remove(nickName);
                }
            }
            for (ClientHandler c : nicknameList.values()){
                sendString(nickName+" disconnected", c);
            }
            server.print(nickName+ " disconnected");
        }
    }

    public Map<String, ClientHandler> getNicknameList() {
        return nicknameList;
    }

    public void startGame() {
        //TODO sistemare passare json della boaurd a tutti + gestione colori
        ArrayList<Player> players;
        players=new ArrayList<>();
        Match match = new Match(players, nicknameList.size(), skulls, frenzyEn, mode, board);
        for(Map.Entry e : nicknameList.entrySet()){
            players.add(new Player(false,"",(String) e.getKey()));
        }
        players.get(0).setFirst(true);
        match.start();
    }
    private class Configuration{
        private int board;
        private int skulls;
        private boolean frenzy;
        private int timer;
        private ArrayList<Integer> availableBoards;
        private ArrayList<Integer> availableSkulls;
        private String mode;
    }
}
