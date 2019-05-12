package it.polimi.ingsw.communication;

import java.io.IOException;

public class LaunchServer {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Provide port please");
            return;
        }
        int port = Integer.parseInt(args[0]);
        MultiServer server = new MultiServer(port);
        server.lifeCycle();
    }
}