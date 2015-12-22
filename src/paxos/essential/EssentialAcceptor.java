package paxos.essential;

public interface EssentialAcceptor {

	void receivePrepare(String proposerHost, ProposalID proposalID);

	void receiveAcceptRequest(ProposalID proposalID, Object value);

}
