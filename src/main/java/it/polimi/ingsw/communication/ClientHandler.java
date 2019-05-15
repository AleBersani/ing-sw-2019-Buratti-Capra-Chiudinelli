package it.polimi.ingsw.communication;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    Socket socket;
    MultiServer server;

    public ClientHandler(Socket socket, MultiServer server) {
        this.socket=socket;
        this.server=server;
    }

    public void handleConnection(Socket clientConnection) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = clientConnection.getInputStream();
            os = clientConnection.getOutputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(os);

        } finally {
            if (is != null && os != null) {
                is.close();
                os.close();
            }
            clientConnection.close();
        }
    }

    public String readString(){
        return null;
    }

    public void sendString(String msg){

    }

    @Override
    public void run() {
        try {
            handleConnection(socket);
        } catch (
                IOException e) {
            System.err.println("Problem with " + socket.getLocalAddress() + ": " + e.getMessage());
        }
    }
}
