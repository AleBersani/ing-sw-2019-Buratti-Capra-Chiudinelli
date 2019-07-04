package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.controller.Controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class is the handler of a single client
 */
public class ClientHandler implements Runnable{

    /**
     * This attribute is true if is your turn, false otherwise
     */
    private boolean yourTurn;
    /**
     * This attribute is true if the client is disconnected
     */
    private boolean disconnect;
    /**
     * This attribute is set true when you log
     */
    private boolean logged;
    /**
     * This attribute is the socket that is used for the connection
     */
    private Socket socket;
    /**
     * This attribute is the controller that handle the game
     */
    private Controller controller;
    /**
     * This attribute is the input stream
     */
    private InputStream is;
    /**
     * This attribute is the output stream
     */
    private OutputStream os;
    /**
     * This attribute is the print writer that is used to write the output
     */
    private PrintWriter out;
    /**
     * This attribute is the buffered reader that is used for reading the input
     */
    private BufferedReader in;
    /**
     * This attribute is the nickname of the player
     */
    private String name;
    /**
     * This attribute is true if the player never spawned before
     */
    private boolean firstSpawn;

    /**
     * This constructor generate a clientHandler
     * @param socket This parameter is the socket that is used for the connection
     * @param controller This parameter is the controller that handle the game
     */
    ClientHandler(Socket socket, Controller controller) {
        this.socket=socket;
        this.controller=controller;
        this.yourTurn=true;
        this.is=null;
        this.os=null;
        this.logged=false;
        this.firstSpawn=true;
    }

    /**
     * This method initialize the input and output channels
     */
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

    /**
     * This method handle the input messages
     * @param clientConnection This attribute is the socket that handle the connection
     * @throws IOException This exception is thrown when there is an Input or Output exception
     */
    private void handleConnection(Socket clientConnection) throws IOException {
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
                else {
                    if (msg.equals("SPD-")) {
                        controller.revertSuspension(this);
                    }
                    if (yourTurn) {
                        controller.understandMessage(msg, this);
                    } else {
                        this.print(">>>This isn't your turn, please wait");
                    }
                }
            }
        }
        catch (SocketException disconnectException){
            controller.quit(this);
        }

        finally {

            if ((os != null)&&(is != null)) {
                os.close();
                is.close();
            }
            clientConnection.close();
        }
    }

    /**
     * This method is called every time a client is connected
     */
    @Override
    public void run() {
        try {
            handleConnection(socket);
        } catch (
                IOException e) {
            System.err.println("Problem with " + socket.getLocalAddress() + ": " + e.getMessage());
        }
    }

    /**
     * This method is used for sending messages to the client
     * @param msg This attribute is the message to send
     */
    public synchronized void print(String msg){
        out.println(msg);
    }

    /**
     * This method is used for reading messages from the client
     * @return The message read
     * @throws IOException This exception is thrown when there is an Input or Output exception
     */
    private String read() throws IOException {
        return in.readLine();
    }

    /**
     * This method is used to set the attribute logged
     * @param logged This parameter is the vale that the attribute will assume
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * This method is used to set the attribute disconnect
     * @param disconnect This parameter is the vale that the attribute will assume
     */
    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    /**
     * This method return the value of the attribute logged
     * @return The value of the attribute logged
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * This method return the value of the attribute socket
     * @return The value of the attribute socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * This method return the value of the attribute name
     * @return The value of the attribute name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method is used to set the attribute name
     * @param name This parameter is the vale that the attribute will assume
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method return the value of the attribute yourTurn
     * @return The value of the attribute yourTurn
     */
    public boolean isYourTurn() {
        return yourTurn;
    }

    /**
     * This method is used to set the attribute yourTurn
     * @param yourTurn This parameter is the vale that the attribute will assume
     */
    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    /**
     * This method return the value of the attribute firstSpawn
     * @return The value of the attribute firstSpawn
     */
    public boolean isFirstSpawn() {
        return firstSpawn;
    }

    /**
     * This method is used to set the attribute firstSpawn
     * @param firstSpawn This parameter is the vale that the attribute will assume
     */
    public void setFirstSpawn(boolean firstSpawn) {
        this.firstSpawn = firstSpawn;
    }

    /**
     * This method return the value of the attribute controller
     * @return The value of the attribute controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * This method return the value of the attribute disconnect
     * @return The value of the attribute disconnect
     */
    public boolean isDisconnect() {
        return disconnect;
    }
}
