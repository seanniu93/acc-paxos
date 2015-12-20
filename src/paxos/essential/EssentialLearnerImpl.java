package paxos.essential;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by Administrator on 12/18/2015.
 */
public class EssentialLearnerImpl extends Thread implements EssentialLearner {
    class Proposal {
        int    acceptCount;
        int    retentionCount;
        Object value;

        Proposal(int acceptCount, int retentionCount, Object value) {
            this.acceptCount    = acceptCount;
            this.retentionCount = retentionCount;
            this.value          = value;
        }
    }

    private final EssentialMessenger      messenger;
    private final int                     quorumSize;
    private HashMap<ProposalID, Proposal> proposals       = new HashMap<ProposalID, Proposal>();
    private HashMap<String,  ProposalID>  acceptors       = new HashMap<String, ProposalID>();
    private Object                        finalValue      = null;
    private ProposalID                    finalProposalID = null;
    private String                        learnerUID;
    private Hashtable                     disk;

    public EssentialLearnerImpl( EssentialMessenger messenger, String learnerUID , int quorumSize) {
        this.messenger  = messenger;
        this.quorumSize = quorumSize;
        this.learnerUID = learnerUID;
        this.disk = new Hashtable();
    }

    @Override
    public boolean isComplete() {
        return finalValue != null;
    }

    @Override
    public void receiveAccepted(String fromUID, ProposalID proposalID,
                                Object acceptedValue) {

        if (isComplete())
            return;

        ProposalID oldPID = acceptors.get(fromUID);

        if (oldPID != null && !proposalID.isGreaterThan(oldPID))
            return;

        acceptors.put(fromUID, proposalID);

        if (oldPID != null) {
            Proposal oldProposal = proposals.get(oldPID);
            oldProposal.retentionCount -= 1;
            if (oldProposal.retentionCount == 0)
                proposals.remove(oldPID);
        }

        if (!proposals.containsKey(proposalID))
            proposals.put(proposalID, new Proposal(0, 0, acceptedValue));

        Proposal thisProposal = proposals.get(proposalID);

        thisProposal.acceptCount    += 1;
        thisProposal.retentionCount += 1;

        if (thisProposal.acceptCount > quorumSize/2) {
            finalProposalID = proposalID;
            finalValue      = acceptedValue;
            proposals.clear();
            acceptors.clear();
            //ClientCommand command = ClientCommand.class.cast(acceptedValue);
            //disk.put(command.key, command.value);
            messenger.onResolution(learnerUID, proposalID, acceptedValue);
        }
    }

    public int getQuorumSize() {
        return quorumSize;
    }

    @Override
    public Object getFinalValue() {
        return finalValue;
    }

    @Override
    public ProposalID getFinalProposalID() {
        return finalProposalID;
    }

    public void run() {
        long endTimeMillis = System.currentTimeMillis() + 10000;
        AcceptedMessage acceptedMessage;
        while(true)
        {
            acceptedMessage = messenger.getAcceptedMessage(learnerUID);
            if(acceptedMessage!=null)
            {
                receiveAccepted(acceptedMessage.fromUID, acceptedMessage.proposalID, acceptedMessage.acceptedValue);
            }
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                return;
            }
        }
    }
}
