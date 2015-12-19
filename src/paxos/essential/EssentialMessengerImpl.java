package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */



public class EssentialMessengerImpl implements EssentialMessenger {

    MessagePool messagePool;
    int quorumSize;

    public EssentialMessengerImpl(MessagePool messagePool, int quorumSize)
    {
        this.messagePool = messagePool;
        this.quorumSize = quorumSize;
    }

    public void broadcastPrepare(ProposalID proposalID, String fromUID) {
        for(int acceptorNum=0; acceptorNum< quorumSize; acceptorNum++) {
            PrepareMessage prepareMessage = new PrepareMessage(fromUID, proposalID);
            messagePool.prepPool.get(acceptorNum).add(prepareMessage);
        }
    }

    public void sendPromise(String proposerUID, ProposalID proposalID, ProposalID previousID, Object acceptedValue) {
        int proposerNum = Integer.valueOf(proposerUID);
        PromiseMessage promiseMessage = new PromiseMessage(proposerUID, proposalID, previousID, acceptedValue);
        messagePool.promPool.get(proposerNum).add(promiseMessage);
    }

    public void sendAccept(String fromUID, ProposalID proposalID, Object proposalValue) {
        for(int acceptorNum=0; acceptorNum < quorumSize; acceptorNum++) {
            AcceptMessage acceptMessage = new AcceptMessage(fromUID, proposalID, proposalValue);
            messagePool.acceptPool.get(acceptorNum).add(acceptMessage);
        }
    }

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue) {
        for(int learnerNum=0; learnerNum < quorumSize; learnerNum++) {
            AcceptedMessage acceptedMessage = new AcceptedMessage(fromUID, proposalID, acceptedValue);
            messagePool.acceptedPool.get(learnerNum).add(acceptedMessage);
        }
    }

    public void onResolution(ProposalID proposalID, Object value) {

    }
}
