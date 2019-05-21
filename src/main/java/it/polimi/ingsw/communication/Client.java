package it.polimi.ingsw.communication;

import it.polimi.ingsw.view.Form;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Closeable {
    private static String host;
    private static int port;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;


    public void init() throws IOException {
        connection = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), true);
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void send(String message) {
        out.println(message);
    }

    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    public void lyfeCycle(Client client){
        try {
            String received = null;
            do {
                received = client.receive();
                System.out.println(received);
            } while (received != null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //platform.rundate

    public static void main(String[] args) throws IOException {
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

        Application.launch(Form.class,args);
    }
}