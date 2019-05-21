package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.ClientHandler;
import it.polimi.ingsw.communication.MultiServer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private Map<String,ClientHandler> nicknameList = new ConcurrentHashMap<>();
    private MultiServer server;
    public String readString(){
        return null;
    }

    public void sendString(String msg, ClientHandler clientHandler) {
        clientHandler.print(msg);
    }

    public void launchServer(int port) throws IOException {
        MultiServer server = new MultiServer(port,this);
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
    }

    public void quit(ClientHandler clientHandler){
        String nickName;
        nickName="gigi";
        clientHandler.setDisconnect(true);
        if (nicknameList.containsValue(clientHandler)){
            for(Map.Entry e : nicknameList.entrySet()){
                if (e.getValue().equals(clientHandler)){
                    nickName=(String) e.getKey();
                    nicknameList.remove(nickName);
                }
            }
            for (ClientHandler c : nicknameList.values()){
                sendString(nickName+" disconnected", c);
            }
        }
    }

    public Map<String, ClientHandler> getNicknameList() {
        return nicknameList;
    }

}
