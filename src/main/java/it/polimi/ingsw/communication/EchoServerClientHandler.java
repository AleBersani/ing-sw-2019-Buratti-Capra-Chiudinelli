package it.polimi.ingsw.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    private Socket socket;
    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        int i;
        String line;
        boolean timeLimit=false;
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                }
                else {
                    if(MultiEchoServer.getNicknameList().contains(line)) {
                        out.println(line + " nickname already used, please enter another one");
                        out.flush();
                    }
                    else {
                        out.println(line + " nickname registered");
                        out.flush();
                        System.out.println(line + " is logged");
                        MultiEchoServer.getNicknameList().add(line);
                        break;
                    }
                }
            }
            if(!line.equals("quit")) {
                while (MultiEchoServer.getNicknameList().size() < 5 && !timeLimit) {
                    out.println("Waiting other players...");
                    out.flush();
                    if (MultiEchoServer.getNicknameList().size() >= 3)
                        for (i = 0; i < 60; i++) {
                            Thread.sleep(1000);
                            if (MultiEchoServer.getNicknameList().size() != 3)
                                break;
                            if (i == 59)
                                timeLimit = true;
                        }
                }
                out.println("Starting game...");
                out.flush();
            }
            //TODO START THE GAME
            in.close();
            out.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}