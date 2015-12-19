package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class PrepareMessage {
    String fromUID;
    ProposalID proposalID;
    PrepareMessage(String fromUID, ProposalID proposalID)
    {
        this.fromUID = fromUID;
        this.proposalID = proposalID;
    }
}
