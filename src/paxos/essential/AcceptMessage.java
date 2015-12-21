package paxos.essential;

import java.io.Serializable;

/**
 * Created by Administrator on 12/18/2015.
 */
public class AcceptMessage implements Serializable {
    ProposalID proposalID;
    Object value;
    public AcceptMessage(ProposalID proposalID, Object value)
    {
        this.proposalID = proposalID;
        this.value = value;
    }
}
