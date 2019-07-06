package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;

/**
 * class of the Client
 */
public class Client extends Thread implements Closeable {
    /**
     * This attribute is the IP of the server
     */
    private static String host;
    /**
     * This attribute is the port of the server
     */
    private static int port;
    /**
     * This attribute is the socket that is used for the connection
     */
    private Socket connection;
    /**
     * This attribute is the buffered reader that is used for reading the input
     */
    private BufferedReader in;
    /**
     * This attribute is the print writer that is used to write the output
     */
    private PrintWriter out;
    /**
     * This attribute is the Interface of the view
     */
    private ViewInterface view;
    /**
     * This attribute is the reference to the Message Handler
     */
    private MessageHandler messageHandler;

    /**
     * Constructor method of the Client
     * @param view the view
     */
    public Client(ViewInterface view) {
        this.view = view;
    }

    /**
     * Main method of the client that launch the GUI
     * @param args parameters from command line
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Provide host:port please");
            return;
        }

        String[] tokens = args[0].split(":");

        if (tokens.length < 2) {
            throw new IllegalArgumentException("Bad formatting: " + args[0]);
        }

        host = tokens[0];
        port = Integer.parseInt(tokens[1]);

        Application.launch(GUI.class,args);
    }

    /**
     * This run constantly receive messages from the server and if the Server send null this method request the close process of the Client
     */
    @Override
    public void run(){
        String received;
        try {
            do {
                received = in.readLine();
                if(received != null) {
                    System.out.println(received);
                    messageHandler.understandMessage(received);
                }
                else{
                    view.stopView();
                }
            } while (received != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method send messages to the Server
     * @param message message to send
     */
    public synchronized void send(String message) {
        System.out.println("+++++"+message);
        out.println(message);
    }

    /**
     * This method close the Client and send a message of quit to the Server
     * @throws IOException if fails the closing process of the socket connection
     */
    public void close() throws IOException {
        this.send("quit");
        in.close();
        out.close();
        connection.close();
        try {
            view.stopView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializer method of the socket connection
     * @throws IOException if the server is not already open
     */
    public void init() {
        boolean serverOn;
         do {
             serverOn = true;
             try {
                 connection = new Socket(host, port);
                 in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 out = new PrintWriter(connection.getOutputStream(), true);
             } catch (IOException e) {
                 serverOn= false;
                 try {
                     sleep(10000);
                 }
                 catch (InterruptedException ignored) { }
             }
         }while (!serverOn);
    }

    /**
     * Setter method of MessageHandler
     * @param messageHandler the MessageHandler to set
     */
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

}