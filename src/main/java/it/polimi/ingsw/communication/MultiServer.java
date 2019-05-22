package it.polimi.ingsw.communication;

import it.polimi.ingsw.controller.Controller;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServer implements Closeable {

    private final int port;
    private Controller controller;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public MultiServer(int port, Controller controller) {
        this.port=port;
        pool = Executors.newCachedThreadPool();
        this.controller=controller;
    }

    public void init( int timer) throws IOException {
        serverSocket = new ServerSocket(port);
        print(">>> Listening on port " + port);
        pool.submit(new Gestor(controller, timer));
    }

    public Socket acceptConnection() throws IOException {
        // blocking call
        Socket accepted = serverSocket.accept();
        print("Connection accepted: " + accepted.getRemoteSocketAddress());
        return accepted;
    }

    public void lifeCycle() throws IOException {
        while (true) {
            final Socket socket = acceptConnection();
            pool.submit(new ClientHandler(socket, controller));
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void print(String msg){
        System.out.println(msg);
    }
}