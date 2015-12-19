package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class AcceptMessage {
    String fromUID;
    ProposalID proposalID;
    Object value;
    public AcceptMessage(String fromUID, ProposalID proposalID, Object value)
    {
        this.fromUID = fromUID;
        this.proposalID = proposalID;
        this.value = value;
    }
}
