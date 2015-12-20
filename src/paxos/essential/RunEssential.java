package paxos.essential;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 12/17/2015.
 */
public class RunEssential{
    /**
     * Created by Administrator on 12/17/2015.
     */
    /*
    static int quorumSize = 11;
    static MessagePool messagePool = new MessagePool(quorumSize);

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        for (int machineNum = 0; machineNum < quorumSize ; machineNum++) {
            EssentialMessenger messenger = new EssentialMessengerImpl(messagePool, quorumSize);
            EssentialProposerImpl proposer = new EssentialProposerImpl(messenger, Integer.toString(machineNum), quorumSize);
            proposer.start();
        }

        for (int machineNum = 0; machineNum < quorumSize ; machineNum++) {
            EssentialMessenger messenger = new EssentialMessengerImpl(messagePool, quorumSize);
            EssentialAcceptorImpl acceptor = new EssentialAcceptorImpl(messenger, Integer.toString(machineNum), quorumSize);
            acceptor.start();
        }

        for (int machineNum = 0; machineNum < quorumSize ; machineNum++) {
            EssentialMessenger messenger = new EssentialMessengerImpl(messagePool, quorumSize);
            EssentialLearnerImpl learner = new EssentialLearnerImpl( messenger,Integer.toString(machineNum), quorumSize);
            learner.start();
        }
    }
    */
    public static void main(String[] args) throws IOException{
        //Server
        int portnumber=4444;
        ServerSocket serverSocket=new ServerSocket(portnumber);

        while(true)
        {
            Socket clientSocket=serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
            break;
        }

        //Client
        String hostName = new String("rattlesnake.zoo.cs.yale.edu");
        PrintWriter out;
        BufferedReader in;
        try
        {
            Socket kkSocket = new Socket(hostName, portnumber);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));


            String fromServer;
            while((fromServer = in.readLine())!=null)
            {
                System.out.println("Server: "+ fromServer);
                if(fromServer.equals("Bye"))
                {
                    break;
                }
                try{
                    BufferedReader stdin;
                    stdin = new BufferedReader(new InputStreamReader(System.in));
                    String fromUser = stdin.readLine();
                    if(fromServer != null)
                    {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                    }
                }
                catch(IOException e)
                {
                    System.out.println("IO exception in reading from stdin\n");
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("IO Exception while creating client socket\n");
        }


    }





}
