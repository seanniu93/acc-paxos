package paxos.essential;


/**
 * Created by Administrator on 12/17/2015.
 */
public class RunEssential {
    /**
     * Created by Administrator on 12/17/2015.
     */
    static int quoramSize = 5;
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        for (int machineNum = 0; machineNum < quoramSize ; quoramSize++) {
            EssentialMessenger messenger = new EssentialMessengerImpl();
            String proposerUID = Integer.toString(machineNum);
            EssentialProposerImpl proposer = new EssentialProposerImpl(messenger, proposerUID, quoramSize);
            proposer.start();
        }

        for (int machineNum = 0; machineNum < quoramSize ; quoramSize++) {
            EssentialMessenger messenger = new EssentialMessengerImpl();
            EssentialAcceptorImpl acceptor = new EssentialAcceptorImpl(messenger);
            acceptor.start();
        }

        for (int machineNum = 0; machineNum < quoramSize ; quoramSize++) {
            EssentialMessenger messenger = new EssentialMessengerImpl();
            EssentialLearnerImpl learner = new EssentialLearnerImpl(messenger, quoramSize);
            learner.start();
        }



    }
}
