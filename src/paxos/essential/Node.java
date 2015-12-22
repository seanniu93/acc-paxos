package paxos.essential;

public class Node extends Thread implements EssentialProposer, EssentialAcceptor, EssentialLearner, EssentialListener {

	EssentialProposerImpl essentialProposerImpl;
	EssentialAcceptorImpl essentialAcceptorImpl;
	EssentialLearnerImpl essentialLearnerImpl;
	EssentialListenerImpl essentialListenerImpl;

	String hostName;
	EssentialMessengerImpl essentialMessengerImpl;
	int quorumSize;
	int portNumber;
	LocationInfo locationInfo;

	public Node(EssentialMessengerImpl essentialMessengerImpl, String hostName, int quorumSize, int portNumber,
	            LocationInfo locationInfo) {
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.hostName = hostName;
		this.quorumSize = quorumSize;
		this.portNumber = portNumber;
		this.locationInfo = locationInfo;
		this.essentialProposerImpl = new EssentialProposerImpl(essentialMessengerImpl, hostName, quorumSize, locationInfo);
		this.essentialAcceptorImpl = new EssentialAcceptorImpl(essentialMessengerImpl, hostName, quorumSize, portNumber, locationInfo);
		this.essentialLearnerImpl = new EssentialLearnerImpl(essentialMessengerImpl, hostName, quorumSize);
		this.essentialListenerImpl = new EssentialListenerImpl(portNumber, hostName, essentialMessengerImpl);
	}

	public void run() {
		this.essentialListenerImpl.start();
		this.essentialProposerImpl.start();
		this.essentialAcceptorImpl.start();
		this.essentialLearnerImpl.start();
	}

	public boolean isActive() {
		return essentialProposerImpl.isActive();
	}

	public void setActive(boolean active) {
		essentialProposerImpl.setActive(active);
		essentialAcceptorImpl.setActive(active);
	}

	public void startListening() {
		essentialListenerImpl.startListening();
	}

	//Proposer Part
	@Override
	public void prepare() {
		essentialProposerImpl.prepare();
	}

	@Override
	public void receivePromise(String fromUID, ProposalID proposalID, ProposalID prevAcceptedID,
	                           Object prevAcceptedValue) {
		essentialProposerImpl.receivePromise(fromUID, proposalID, prevAcceptedID, prevAcceptedValue);
	}

	@Override
	public void setProposal(Object value) {
		essentialProposerImpl.setProposal(value);
	}

	public void resendAccept() {
		essentialProposerImpl.resendAccept();
	}

	public boolean isLeader() {
		return essentialProposerImpl.isLeader();
	}

	public void setLeader(boolean leader) {
		essentialProposerImpl.setLeader(leader);
	}

	//Acceptor Part
	@Override
	public void receivePrepare(String proposerHost, ProposalID proposalID) {
		essentialAcceptorImpl.receivePrepare(proposerHost, proposalID);
	}

	@Override
	public void receiveAcceptRequest(ProposalID proposalID, Object value) {
		essentialAcceptorImpl.receiveAcceptRequest(proposalID, value);
	}

	public ProposalID getPromisedID() {
		return essentialAcceptorImpl.getPromisedID();
	}

	public ProposalID getAcceptedID() {
		return essentialAcceptorImpl.getAcceptedID();
	}

	public Object getAcceptedValue() {
		return essentialAcceptorImpl.getAcceptedValue();
	}

	//learner

	@Override
	public boolean isComplete() {
		return essentialLearnerImpl.isComplete();
	}

	@Override
	public void receiveAccepted(String acceptorHost, ProposalID proposalID, Object acceptedValue) {
		essentialLearnerImpl.receiveAccepted(acceptorHost, proposalID, acceptedValue);
	}

	@Override
	public Object getFinalValue() {
		return essentialLearnerImpl.getFinalValue();
	}

	@Override
	public ProposalID getFinalProposalID() {
		return essentialLearnerImpl.getFinalProposalID();
	}

}
