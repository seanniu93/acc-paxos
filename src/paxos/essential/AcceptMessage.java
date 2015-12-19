package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class AcceptMessage {
    ProposalID proposalID;
    Object value;
    public AcceptMessage(ProposalID proposalID, Object value)
    {
        this.proposalID = proposalID;
        this.value = value;
    }
}
