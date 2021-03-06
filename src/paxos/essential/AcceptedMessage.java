package paxos.essential;

import java.io.Serializable;

public class AcceptedMessage implements Serializable {

	String fromUID;
	ProposalID proposalID;
	Object acceptedValue;

	public AcceptedMessage(String fromUID, ProposalID proposalID, Object acceptedValue) {
		this.fromUID = fromUID;
		this.proposalID = proposalID;
		this.acceptedValue = acceptedValue;
	}

}
