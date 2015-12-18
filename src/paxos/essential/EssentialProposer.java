package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public interface EssentialProposer {
    public void setProposal(Object value);

    public void prepare();

    public void receivePromise(String fromUID, ProposalID proposalID, ProposalID prevAcceptedID, Object prevAcceptedValue);
}
