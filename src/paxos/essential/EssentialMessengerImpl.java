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

    public void sendPromise(String proposerUID, String acceptorUID, ProposalID proposalID, ProposalID previousID, Object acceptedValue) {
        int proposerNum = Integer.valueOf(proposerUID);
        PromiseMessage promiseMessage = new PromiseMessage(acceptorUID, proposalID, previousID, acceptedValue);
        messagePool.promPool.get(proposerNum).add(promiseMessage);
    }

    public void sendAccept(String fromUID, ProposalID proposalID, Object proposalValue) {
        for(int acceptorNum=0; acceptorNum < quorumSize; acceptorNum++) {
            AcceptMessage acceptMessage = new AcceptMessage(proposalID, proposalValue);
            messagePool.acceptPool.get(acceptorNum).add(acceptMessage);
        }
    }

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue) {
        for(int learnerNum=0; learnerNum < quorumSize; learnerNum++) {
            AcceptedMessage acceptedMessage = new AcceptedMessage(fromUID, proposalID, acceptedValue);
            messagePool.acceptedPool.get(learnerNum).add(acceptedMessage);
        }
    }

    public PrepareMessage getPrepareMessage(String acceptorUID)
    {
        int uid = Integer.valueOf(acceptorUID);
        if(messagePool.prepPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.prepPool.get(uid).remove(0);
        }
    }


    public PromiseMessage getPromiseMessage(String proposerUID) {
        int uid = Integer.valueOf(proposerUID);
        if(messagePool.promPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.promPool.get(uid).remove(0);
        }
    }

    public AcceptMessage getAcceptMessage(String acceptorUID) {
        int uid = Integer.valueOf(acceptorUID);
        if(messagePool.acceptPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.acceptPool.get(uid).remove(0);
        }
    }

    public AcceptedMessage getAcceptedMessage(String learnerUID) {
        int uid = Integer.valueOf(learnerUID);
        if(messagePool.acceptedPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.acceptedPool.get(uid).remove(0);
        }
    }


    public void onResolution(String learnerUID, ProposalID proposalID, Object value) {
        System.out.println("Learner "+learnerUID+" learned value: "+value+"from proposal "+proposalID+'\n');
    }
}
