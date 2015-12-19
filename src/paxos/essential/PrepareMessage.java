package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class PrepareMessage {
    String proposerUID;
    ProposalID proposalID;
    PrepareMessage(String proposerUID, ProposalID proposalID)
    {
        this.proposerUID = proposerUID;
        this.proposalID = proposalID;
    }
}
