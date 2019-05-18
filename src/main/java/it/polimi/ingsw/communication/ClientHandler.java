package it.polimi.ingsw.communication;

import it.polimi.ingsw.controller.Controller;

import java.io.*;
import java.net.Socket;

import static java.lang.System.out;

public class ClientHandler implements Runnable{

    private boolean yourTurn, disconect, logged;
    private Socket socket;
    private Controller controller;
    private InputStream is;
    private OutputStream os;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, Controller controller) {
        this.socket=socket;
        this.controller=controller;
        this.yourTurn=true;
        this.is=null;
        this.os=null;
        this.logged=false;
    }

    public void handleConnection(Socket clientConnection) throws IOException {
        is = clientConnection.getInputStream();
        os = clientConnection.getOutputStream();
        out = new PrintWriter(os,true);
        in = new BufferedReader(new InputStreamReader(is));
        String msg;
        disconect = false;
        try {

            while(!disconect){
                msg=read();
                if(yourTurn) {
                    sendString(msg);
                }
                else
                    print("This is not your turn, please wait for it");
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

    public void sendString(String msg){
        controller.understandMessage(msg,this);
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



    public Socket getSocket() {
        return socket;
    }
}
