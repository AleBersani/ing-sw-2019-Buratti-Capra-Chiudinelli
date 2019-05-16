package it.polimi.ingsw.communication;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServer implements Closeable {

    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public MultiServer(int port) {
        this.port=port;
        pool = Executors.newCachedThreadPool();
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
        pool.submit(new Gestor(this));
        while (true) {
            final Socket socket = acceptConnection();
            pool.submit(new ClientHandler(socket,this));
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}