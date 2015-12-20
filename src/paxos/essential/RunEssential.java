package paxos.essential;


/**
 * Created by Administrator on 12/17/2015.
 */
public class RunEssential {
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
