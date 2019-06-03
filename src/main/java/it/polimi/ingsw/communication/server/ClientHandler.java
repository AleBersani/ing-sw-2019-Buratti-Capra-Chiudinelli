package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.controller.Controller;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private boolean yourTurn, disconnect, logged;
    private Socket socket;
    private Controller controller;
    private InputStream is;
    private OutputStream os;
    private PrintWriter out;
    private BufferedReader in;
    private String serviceMessage;

    public ClientHandler(Socket socket, Controller controller) {
        this.socket=socket;
        this.controller=controller;
        this.yourTurn=true;
        this.is=null;
        this.os=null;
        this.logged=false;
        this.serviceMessage="login";
    }

    public void handleConnection(Socket clientConnection) throws IOException {
        is = clientConnection.getInputStream();
        os = clientConnection.getOutputStream();
        out = new PrintWriter(os,true);
        in = new BufferedReader(new InputStreamReader(is));
        String msg;
        disconnect = false;
        try {

            while(!disconnect) {
                print(serviceMessage);
                msg = read();
                if(msg.equals("quit")){
                    controller.quit(this);
                }
                else{
                    switch (serviceMessage) {
                        case "Insert a command:": {
                            if (yourTurn) {
                                controller.understandMessage(msg, this);
                            } else
                                print("This is not your turn, please wait for it");
                            break;
                        }
                        case "login": {
                            controller.login(msg, this);
                            break;
                        }

                        case "Select a board": {
                            controller.selectBoard(msg, this);
                            break;
                        }
                        case "Select the number of skulls": {
                            controller.setSkulls(msg, this);
                            break;
                       }
                        case "Do you like to play with frenzy? Y/N": {
                            controller.setFrenzy(msg, this);
                            break;
                        }
                        case "Now you are in the waiting room": {
                            controller.waitingRoom(msg, this);
                            break;
                        }
                        case "Initialize board":{
                            controller.boardDescription(this);
                        }
                        default:{

                        }
                    }
                }
            }
        } finally {
            if ((os != null)&&(is != null)) {
                os.close();
                is.close();
            }
            clientConnection.close();
        }
    }

    public String readString(){
        return null;
    }

    @Override
    public void run() {
        try {
            handleConnection(socket);
        } catch (
                IOException e) {
            System.err.println("Problem with " + socket.getLocalAddress() + ": " + e.getMessage());
        }
    }

    public void print(String msg){
        out.println(msg);
    }

    public String read() throws IOException {
        return in.readLine();
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public boolean isLogged() {
        return logged;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }
}
