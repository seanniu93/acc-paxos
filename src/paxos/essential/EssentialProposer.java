package paxos.essential;

public interface EssentialProposer {

	void setProposal(Object value);

	void prepare();

	void receivePromise(String fromUID, ProposalID proposalID, ProposalID prevAcceptedID, Object prevAcceptedValue);

	boolean isLeader();

	void setLeader(String leader);

}
