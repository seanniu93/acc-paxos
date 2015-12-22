package paxos.essential;

public interface EssentialLearner {

	boolean isComplete();

	void receiveAccepted(String fromUID, ProposalID proposalID, Object acceptedValue);

	Object getFinalValue();

	ProposalID getFinalProposalID();

}
