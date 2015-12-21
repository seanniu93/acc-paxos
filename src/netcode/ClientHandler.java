package netcode;

import paxos.essential.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by yw486 on 12/21/15.
 */
public class ClientHandler extends Thread{

    Socket clientSocket;
    EssentialMessengerImpl essentialMessengerImpl;
    ObjectInputStream objectInputStream;
    String hostName;
    public ClientHandler(Socket clientSocket, EssentialMessengerImpl essentialMessengerImpl, String hostName) throws IOException {
        this.clientSocket = clientSocket;
        this.essentialMessengerImpl = essentialMessengerImpl;
        this.hostName = hostName;
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }
    public void run ()
    {
        Object o = null;
        try {
            o = objectInputStream.readObject();
            System.out.println("Read object: "+o);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(o == null)
        {
            System.out.println("Received Message from client is null");
        }
        else
        {
            if(o instanceof PrepareMessage)
            {
                essentialMessengerImpl.addPrepareMessage((PrepareMessage)o, hostName);
            }
            else if(o instanceof PromiseMessage)
            {

            }
            else if(o instanceof AcceptMessage)
            {

            }
            else if(o instanceof AcceptedMessage)
            {

            }
            else if(o instanceof String)
            {
                System.out.println("Received obkject from client as a string: "+o+"\n");
            }
            else
            {
                System.out.println("Unknown type object sent from client from client\n");
            }
        }
    }
}
