package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.MultiServer;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private Map<String,Socket> nicknameList = new ConcurrentHashMap<>();

    private MultiServer server;

    public String readString(){
        return null;
    }

    public void sendString(String msg){

    }

    public void understandMessage(String msg, Socket socket){
        String [] command = msg.split(" ");
        switch(command[0]){
            case "login": {
                if(!this.getNicknameList().containsKey(command[1])) {
                    this.getNicknameList().put(command[1],socket);
                    System.out.println("<<< " + socket.getRemoteSocketAddress() + " is logged as: " + msg);
                    sendString(">>> logged as: " + msg);
                }
                else
                    sendString(">>> " + msg + " is already use, choose another nickname");
                break;
            }
            case "?": {
                sendString("help menù");
                break;
            }
            default: {
                sendString("This command doesn't exist\n Type '?' for the list of all available commands");
            }
        }
    }

    public Map<String, Socket> getNicknameList() {
        return nicknameList;
    }

}
