package server;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

    public static ArrayList<Socket> connectionArray = new ArrayList<>();
    public static ArrayList<String> currentUsers = new ArrayList<>();

    // logger
    private static final Logger log = Logger.getLogger(ChatServer.class);

    public static void main(String[] args) {

        try {
            // Initialize Server socket with port = 8085
            ServerSocket server = new ServerSocket(8085);
            log.info("Waiting for a clients...");

            while(true){
                Socket socket = server.accept();  // wait for a client's connection
                connectionArray.add(socket);      // adding a client socket to arrayList

                log.info("Client connected from: " + socket.getLocalAddress().getHostName());

                addUserName(socket);

                ChatServerReturn CHAT = new ChatServerReturn(socket);
                Thread X = new Thread(CHAT);
                X.start();
            }
        } catch (IOException e) {
            log.error("IOException" + e.getMessage(), e);
        }
    }

    /**
     * Send to the UI list of users who is currently online in chat room
     * @param socket it's a current user socket, who will take a list
     * @throws IOException
     */
    public static void addUserName(Socket socket) throws IOException {

        Scanner in = new Scanner(socket.getInputStream());
        String userName = in.nextLine();
        currentUsers.add(userName);

        for (Socket s: connectionArray) {
            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.println("#?!"+ currentUsers);
            out.flush();
        }
    }

}
