package paxos.essential;

import java.io.Serializable;

public class AcceptMessage implements Serializable {

	ProposalID proposalID;
	Object value;

	public AcceptMessage(ProposalID proposalID, Object value) {
		this.proposalID = proposalID;
		this.value = value;
	}

}
