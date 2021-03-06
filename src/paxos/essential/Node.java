package paxos.essential;

import java.util.ArrayList;

public class Node extends Thread implements EssentialProposer, EssentialAcceptor, EssentialLearner, EssentialListener {

	EssentialProposerImpl essentialProposerImpl;
	EssentialAcceptorImpl essentialAcceptorImpl;
	EssentialLearnerImpl essentialLearnerImpl;
	EssentialListenerImpl essentialListenerImpl;

	String hostName;
	EssentialMessengerImpl essentialMessengerImpl;
	int quorumSize;
	int portNumber;
	ArrayList<LocationInfo> locationInfoList;
	String leaderHost;

	int heartbeatTicks = 0;

	public Node(EssentialMessengerImpl essentialMessengerImpl, String hostName, int quorumSize, int portNumber,
	            ArrayList<LocationInfo> locationInfoList) {
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.hostName = hostName;
		this.quorumSize = quorumSize;
		this.portNumber = portNumber;
		this.locationInfoList = locationInfoList;
		this.leaderHost = locationInfoList.get(0).getHostName();

		this.essentialProposerImpl = new EssentialProposerImpl(essentialMessengerImpl, hostName, quorumSize, locationInfoList, leaderHost);
		this.essentialAcceptorImpl = new EssentialAcceptorImpl(essentialMessengerImpl, hostName, quorumSize, portNumber, locationInfoList);
		this.essentialLearnerImpl = new EssentialLearnerImpl(essentialMessengerImpl, hostName, quorumSize);
		this.essentialListenerImpl = new EssentialListenerImpl(portNumber, hostName, essentialMessengerImpl, leaderHost, this);
	}

	public void run() {
		this.essentialListenerImpl.start();
		this.essentialProposerImpl.start();
		this.essentialAcceptorImpl.start();
		this.essentialLearnerImpl.start();

		// Heartbeat
		startHeartbeatMonitor();
		if (isLeader())
			startHeartbeat();
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

	public void setLeader(String leaderHost) {
		this.leaderHost = leaderHost;
		essentialListenerImpl.setLeader(leaderHost);
		essentialProposerImpl.setLeader(leaderHost);
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

	private void startHeartbeatMonitor() {
		new Thread(() -> {
			while (true) {
				// Check for heartbeats every 10 seconds
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ignored) {
				}
				if (heartbeatTicks > 0) {
					heartbeatTicks = 0;
				} else {
					System.out.println("Leader has failed");
					// TODO leader has failed
				}
			}
		}).start();
	}

	private void startHeartbeat() {
		new Thread(() -> {
			while (true) {
				// Send heartbeat every second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignored) {
				}
				essentialMessengerImpl.broadcastHeartbeat(hostName);
			}
		}).start();
	}

	public void incrementHeartbeat() {
		heartbeatTicks++;
	}

}
