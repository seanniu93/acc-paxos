package netcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by yw486 on 12/20/15.
 */
public class RunNetwork {
    public static void main(String[] args) throws IOException {
        //Server
        int portnumber=4444;
/*
        ServerSocket serverSocket=new ServerSocket(portnumber);

        while(true)
        {
            Socket clientSocket=serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
            break;
        }
*/
        //Client
        String hostName = new String("scorpion.zoo.cs.yale.edu");
        PrintWriter out;
        BufferedReader in;
        try {
            Socket kkSocket = new Socket(hostName, portnumber);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));


            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye")) {
                    break;
                }
                try {
                    BufferedReader stdin;
                    stdin = new BufferedReader(new InputStreamReader(System.in));
                    String fromUser = stdin.readLine();
                    if (fromServer != null) {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                    }
                } catch (IOException e) {
                    System.out.println("IO exception in reading from stdin\n");
                }
            }
        }
        catch(IOException e) {
            System.out.println("IO Exception while creating client socket\n");
        }
    }
}
