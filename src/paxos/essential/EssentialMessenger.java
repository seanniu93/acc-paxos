package paxos.essential;

import paxos.essential.message.ClientCommand;

public interface EssentialMessenger {

	void broadcastPrepare(ProposalID proposalID, String proposerHost);

	void sendPromise(String acceptorHost, ProposalID proposalID, ProposalID previousID, Object acceptedValue,
	                 String proposerHost, int portNumber);

	void sendAccept(String proposerHost, ProposalID proposalID, Object proposalValue);

	void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue);

	void broadcastHeartbeat(String hostname);

	void onResolution(String learnerUID, ProposalID proposalID, Object value);

	PrepareMessage getPrepareMessage(String acceptorUID);

	PromiseMessage getPromiseMessage(String proposerUID);

	AcceptMessage getAcceptMessage(String acceptorUID);

	AcceptedMessage getAcceptedMessage(String learnerUID);

	void addPrepareMessage(PrepareMessage prepareMessage, String acceptorHost);

	void addPromiseMessage(PromiseMessage promiseMessage, String proposerHost);

	void addAcceptMessage(AcceptMessage acceptMessage, String acceptorHost);

	void addAcceptedMessage(AcceptedMessage acceptedMessage, String learnerHost);

	public void addClientCommand(ClientCommand cmd);

	public ClientCommand getClientCommand();

}
