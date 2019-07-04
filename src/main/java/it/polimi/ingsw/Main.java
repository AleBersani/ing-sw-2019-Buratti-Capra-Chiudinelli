package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;

/**
 * Main class of the server
 */
public class Main {
    /**
     * Main method of the server that launch the server
     * @param args parameters from command line
     * @throws IOException is launch if the launch of the server fail
     */
    public static void main( String[] args ) throws IOException
    {
        Controller controller= new Controller(args);
        if (args.length == 0) {
            System.out.println("Provide port please");
            return;
        }
        int port = Integer.parseInt(args[0]);
        controller.launchServer(port);
    }
}
