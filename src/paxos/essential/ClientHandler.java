package paxos.essential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by yw486 on 12/20/15.
 */
public class ClientHandler extends Thread{

    Socket clientSocket;
    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }
    public void run ()
    {
        try{
            String inputLine, outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            ServerToServerProtocol ssp = new ServerToServerProtocol();
            outputLine = ssp.processInput(null);
            out.println(outputLine);
            while((inputLine = in.readLine())!=null)
            {
                System.out.println("From client: "+inputLine);
                outputLine = ssp.processInput(inputLine);
                out.println(outputLine);
                if(outputLine.equals("Bye"))
                    break;
            }

        }
        catch(IOException e)
        {
            System.out.println("IO Exception in listening to port\n");
        }
    }
}
