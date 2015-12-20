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
}
