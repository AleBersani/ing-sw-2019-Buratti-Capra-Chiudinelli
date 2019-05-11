package it.polimi.ingsw.communication;

import java.io.*;
import java.net.Socket;

import static it.polimi.ingsw.communication.MultiServer.nicknameList;

public class ClientHandler implements Runnable{

    Socket socket;

    public ClientHandler(Socket socket) {
        this.socket=socket;
    }

    public void handleConnection(Socket clientConnection) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            // getting the streams for communication
            is = clientConnection.getInputStream();
            os = clientConnection.getOutputStream();

            // Decorator pattern ;)
            // BufferedReader is useful because it offers readLine
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(os);

            String msg;

            do {
                msg = in.readLine();

                if (msg != null && !msg.startsWith("quit"))
                    if(!nicknameList.contains(msg)) {
                        nicknameList.add(msg);
                        System.out.println("<<< " + clientConnection.getRemoteSocketAddress() + " is logged as: " + msg);
                        out.println(">>> logged as: " + msg);
                        // when you call flush you really send what
                        // you added to the buffer with println.
                        out.flush();
                    }
                    else{
                        out.println(">>> " + msg + " is already use, choose another nickname");
                        out.flush();
                    }
            } while (msg != null && !msg.startsWith("quit"));

        } finally {
            if (is != null && os != null) {
                is.close();
                os.close();
            }
            clientConnection.close();
        }
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
