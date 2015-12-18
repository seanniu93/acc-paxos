package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public interface EssentialLearner {
    public boolean isComplete();

    public void receiveAccepted(String fromUID, ProposalID proposalID, Object acceptedValue);

    public Object getFinalValue();

    ProposalID getFinalProposalID();
}
