package it.polimi.ingsw.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    static ArrayList<String> nicknameList= new ArrayList<>();
    private Socket socket;
    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                }
                else {
                    if(nicknameList.contains(line)) {
                        out.println(line + " nickname already used, please enter another one");
                        out.flush();
                    }
                    else {
                        out.println(line + " nickname registered");
                        out.flush();
                        nicknameList.add(line);
                        break;
                    }
                }
            }
            while(nicknameList.size()<2){
                out.println("Waiting other players...");
                out.flush();
            }
            out.println("Starting game...");
            out.flush();
            //TODO START THE GAME
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}