package paxos.essential;

import java.io.Serializable;

public class PromiseMessage implements Serializable {

	String acceptorHost;
	ProposalID proposalID;
	ProposalID prevAcceptedID;
	Object prevAcceptedValue;

	public PromiseMessage(String acceptorHost, ProposalID proposalID, ProposalID prevAcceptedID,
	                      Object prevAcceptedValue) {
		this.acceptorHost = acceptorHost;
		this.proposalID = proposalID;
		this.prevAcceptedID = prevAcceptedID;
		this.prevAcceptedValue = prevAcceptedValue;
	}

}
