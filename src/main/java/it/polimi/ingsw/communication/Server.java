package it.polimi.ingsw.communication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Closeable {
    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void init() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println(">>> Listening on " + port);
    }

    public Socket acceptConnection() throws IOException {
        // blocking call
        Socket accepted = serverSocket.accept();
        System.out.println("Connection accepted: " + accepted.getRemoteSocketAddress());
        return accepted;
    }

    public void lifeCycle() throws IOException {
        init();
        try {
            Socket socket = acceptConnection();
            ClientHandler clientHandler= new ClientHandler(socket);
            clientHandler.handleConnection(socket);
        } finally {
            close();
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}