package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.controller.Controller;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handle the connection server side
 */
public class MultiServer implements Closeable {

    /**
     * This attribute is the port used for the connection
     */
    private final int port;
    /**
     * This attribute is the controller that handle the game
     */
    private Controller controller;
    /**
     * This attribute is the serverSocket
     */
    private ServerSocket serverSocket;
    /**
     * This attribute is a pool of Executors that run Threads
     */
    private ExecutorService pool;

    /**
     * This constructor initialize the MultiServer
     * @param port This parameter is the port used for the connection
     * @param controller This parameter is the controller that handle the game
     */
    public MultiServer(int port, Controller controller) {
        this.port=port;
        pool = Executors.newCachedThreadPool();
        this.controller=controller;
    }

    /**
     * This method set up the connection
     * @param timer This parameter is the duration of the timer
     * @throws IOException This exception is thrown when there are problems with the connection
     */
    public void init( int timer) throws IOException {
        serverSocket = new ServerSocket(port);
        print(">>> Listening on port " + port);
        pool.submit(new Gestor(controller, timer));
    }

    /**
     * This method handle the connection of a new client
     * @return The socket of the new client
     * @throws IOException This exception is thrown when there are problems with the connection
     */
    private Socket acceptConnection() throws IOException {
        // blocking call
        Socket accepted = serverSocket.accept();
        print("Connection accepted: " + accepted.getRemoteSocketAddress());
        return accepted;
    }

    /**
     * This method gives the socket of the clients to the Executors
     * @throws IOException This exception is thrown when there are problems with the connection
     */
    public void lifeCycle() throws IOException {
        while (true) {
            final Socket socket = acceptConnection();
            pool.submit(new ClientHandler(socket, controller));
        }
    }

    /**
     * This method is used for closing the connection with a client
     * @throws IOException This exception is thrown when there are problems with the connection
     */
    public void close() throws IOException {
        serverSocket.close();
    }

    /**
     * This method print a message
     * @param msg This parameter is the message to print
     */
    public void print(String msg){
        System.out.println(msg);
    }
}