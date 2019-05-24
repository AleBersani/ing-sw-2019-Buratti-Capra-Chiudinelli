package it.polimi.ingsw.communication;

import it.polimi.ingsw.view.Form;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class Client extends Thread implements Closeable {
    private static String host;
    private static int port;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private Form form;

    public Client(Form form) {
        this.form = form;
    }

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

        try {
            form.stopView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            String received = null;
            do {
                received = this.receive();
                System.out.println(received);
            } while (received != null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.close();
            } catch (Exception e) {
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