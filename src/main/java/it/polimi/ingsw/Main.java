package it.polimi.ingsw;

import it.polimi.ingsw.communication.MultiServer;
import it.polimi.ingsw.controller.Controller;

import java.io.IOException;

public class Main
{
    public static void main( String[] args ) throws IOException
    {
        Controller controller= new Controller();
        if (args.length == 0) {
            System.out.println("Provide port please");
            return;
        }
        int port = Integer.parseInt(args[0]);
        controller.launchServer(port);
    }
}
