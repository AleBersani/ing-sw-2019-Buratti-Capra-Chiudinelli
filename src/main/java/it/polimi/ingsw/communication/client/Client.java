package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;

public class Client extends Thread implements Closeable {
    private static String host;
    private static int port;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private ViewInterface view;
    private MessageHandler messageHandler;

    public Client(ViewInterface view) {
        this.view = view;
    }

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

    @Override
    public void run(){
        String received;
        try {
            do {
                received = in.readLine();
                System.out.println(received); //sarà da togliere
                if(received != null) {
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

    public synchronized void send(String message) {
        out.println(message);
        System.out.println("+++++++++".concat(message));
    }

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

    public void init() throws IOException {
        connection = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), true);
    }


    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

}