package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.communication.server.MultiServer;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private Map<String,ClientHandler> nicknameList = new ConcurrentHashMap<>();
    private Map<String,ClientHandler> disconnected = new ConcurrentHashMap<>();
    private MultiServer server;
    private Match match;
    private int skulls;
    private boolean frenzyEn;
    private String board;
    private boolean first;
    private int timer;

    public Controller(){
        //TODO genera game di default da file + timer
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
            case "login": {
                login(command, clientHandler);
                break;
            }
            case "quit": {
                quit(clientHandler);
                break;
            }
            case "?": {
                sendString("help menù", clientHandler);
                break;
            }
            default: {
                sendString("This command doesn't exist, type '?' for the list of all available commands", clientHandler);
            }
        }
    }

    public void login(String[] command, ClientHandler clientHandler) {
        if (disconnected.containsKey(command[1])){
            nicknameList.put(command[1],clientHandler);
            disconnected.remove(command[1],clientHandler);
        }
        if(nicknameList.isEmpty()){
            first=true;
        }

        if (!clientHandler.isLogged()) {
            if (!this.getNicknameList().containsKey(command[1])) {
                this.getNicknameList().put(command[1], clientHandler);
                System.out.println("<<< " + clientHandler.getSocket().getRemoteSocketAddress() + " is logged as: " + command[1]);
                sendString(">>> logged as: " + command[1], clientHandler);
                clientHandler.setLogged(true);
            } else
                sendString(">>> " + command[1] + " is already use, choose another nickname", clientHandler);

        }
        else{
            sendString(">>> You are already logged", clientHandler);
        }
        if (first){
            setGameRules(clientHandler);
            first=false;
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
                clientHandler.setServiceMessage("Insert a command:");
                break;
            }
            case "N": {
                frenzyEn = false;
                sendString("You disabled frenzy", clientHandler);
                clientHandler.setServiceMessage("Insert a command:");
                break;
            }
            default:{
                sendString("Please respond Y or N", clientHandler);
            }
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
        //TODO sistemare
        ArrayList<Player> players;
        players=new ArrayList<>();
        match=new Match(players,nicknameList.size(),skulls, frenzyEn,"",board);
        for(Map.Entry e : nicknameList.entrySet()){
            players.add(new Player(false,"",(String) e.getKey()));
        }
        players.get(0).setFirst(true);
        match.start();
    }
}
