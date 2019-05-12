package it.polimi.ingsw.communication;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServer extends Server {

    static ArrayList<String> nicknameList = new ArrayList<>();
    private ExecutorService pool;

    public MultiServer(int port) {
        super(port);
        pool = Executors.newCachedThreadPool();
    }

    @Override
    public void lifeCycle() throws IOException {
        init();
        while (true) {
            final Socket socket = acceptConnection();
            pool.submit(new ClientHandler(socket));
        }
    }
}