package paxos.essential;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yw486 on 12/21/15.
 */
public class EssentialListenerImpl extends Thread implements EssentialListner{
    int portNumber;
    String hostName;
    EssentialMessengerImpl essentialMessengerImpl;

    EssentialListenerImpl(int portNumber, String hostName, EssentialMessengerImpl essentialMessengerImpl) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.essentialMessengerImpl = essentialMessengerImpl;
    }

    public void run()
    {
        startListening();
    }

    public void startListening() {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e)
        {
            System.out.println("Create serverSocket failed in Node with error "+e+"\n");
            e.printStackTrace();
        }

        while(true)
        {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
                System.out.println("Accept client socket failed in Node with error "+ e+ "\n");
            }
            ClientHandler clientHandler = null;
            try {
                clientHandler = new ClientHandler(clientSocket, essentialMessengerImpl, hostName);
            }
            catch (IOException e)
            {
                System.out.println("");
            }
            if(clientHandler!=null) {
                clientHandler.start();
            }
            else
            {
                System.out.println("ClientHandler is initialized to null\n");
            }
        }
    }

}
