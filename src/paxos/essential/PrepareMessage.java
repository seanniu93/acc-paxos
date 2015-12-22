package paxos.essential;

import java.io.Serializable;

public class PrepareMessage implements Serializable {

	String proposerHost;
	ProposalID proposalID;

	PrepareMessage(String proposerHost, ProposalID proposalID) {
		this.proposerHost = proposerHost;
		this.proposalID = proposalID;
	}

}
