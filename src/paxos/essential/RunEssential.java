package paxos.essential;

import sun.plugin2.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 12/17/2015.
 */
public class RunEssential{
    /**
     * Created by Administrator on 12/17/2015.
     */
    static int quorumSize = 2;
    static MessagePool messagePool = new MessagePool(quorumSize);

    //add hostname and port number here;



    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String myHostName = null;
        try {
            myHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Integer myport = 3333;

        ArrayList<String> hostName = new ArrayList<String>();
        ArrayList<Integer> portNumber = new ArrayList<Integer>();
        hostName.add("frog.zoo.cs.yale.edu");
        hostName.add("bumblebee.zoo.cs.yale.edu");
        portNumber.add(3333);
        portNumber.add(3333);
        LocationInfo locationInfo = new LocationInfo(hostName, portNumber);

        MessagePool messagePool = new MessagePool(quorumSize);
        EssentialMessengerImpl essentialMessengerImpl = new EssentialMessengerImpl(messagePool, quorumSize, locationInfo);

        Node node = new Node(essentialMessengerImpl, myHostName, quorumSize, myport, locationInfo);
        node.start();
    }
}
