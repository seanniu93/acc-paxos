package paxos.essential;

import java.io.Serializable;

/**
 * Created by Administrator on 12/18/2015.
 */
public class PrepareMessage implements Serializable {
    String proposerHost;
    ProposalID proposalID;
    PrepareMessage(String proposerHost, ProposalID proposalID)
    {
        this.proposerHost = proposerHost;
        this.proposalID = proposalID;
    }
}
