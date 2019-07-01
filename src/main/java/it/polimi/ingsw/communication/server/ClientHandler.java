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
    private String name;
    private boolean firstSpawn;

    public ClientHandler(Socket socket, Controller controller) {
        this.socket=socket;
        this.controller=controller;
        this.yourTurn=true;
        this.is=null;
        this.os=null;
        this.logged=false;
        this.firstSpawn=true;
    }

    private void init(){
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out = new PrintWriter(os,true);
        in = new BufferedReader(new InputStreamReader(is));
    }

    public void handleConnection(Socket clientConnection) throws IOException {
        String msg;
        disconnect = false;
        init();
        try {
            this.print("Login");
            while(!disconnect) {
                msg = read();
                System.out.println(msg); //poi da togliere
                if(msg.equals("quit")){
                    controller.quit(this);
                }
                if(msg.equals("SPD-")){
                    controller.revertSuspension(this);
                }
                if(yourTurn){
                    controller.understandMessage(msg,this);
                }
                else{
                    this.print(">>>This isn't your turn, please wait");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

            if ((os != null)&&(is != null)) {
                os.close();
                is.close();
            }
            clientConnection.close();
        }
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

    public synchronized void print(String msg){
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isFirstSpawn() {
        return firstSpawn;
    }

    public void setFirstSpawn(boolean firstSpawn) {
        this.firstSpawn = firstSpawn;
    }
}
