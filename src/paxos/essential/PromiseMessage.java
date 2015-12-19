package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class PromiseMessage {
    String fromUID;
    ProposalID proposalID;
    ProposalID prevAcceptedID;
    Object prevAcceptedValue;
    public PromiseMessage(String fromUID, ProposalID proposalID, ProposalID prevAcceptedID, Object prevAcceptedValue)
    {
        this.fromUID = fromUID;
        this.proposalID = proposalID;
        this.prevAcceptedID = prevAcceptedID;
        this.prevAcceptedValue = prevAcceptedValue;
    }
}
