package netcode;

import paxos.essential.*;

import java.io.*;
import java.net.Socket;

/**
 * Created by yw486 on 12/21/15.
 */
public class ClientHandler extends Thread{

    Socket clientSocket;
    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }
    public void run () {
        System.out.println("My recerived from client");
        PrintWriter out;
        BufferedReader in;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String fromClient;
            out.println("I'm the best server!\n");
            while ((fromClient = in.readLine()) != null) {
                System.out.println("Client: " + fromClient);
                if (fromClient.equals("Bye")) {
                    break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("IO Exception in server\n");
        }
    }
}
