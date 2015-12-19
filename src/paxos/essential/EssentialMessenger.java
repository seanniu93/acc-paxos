package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public interface EssentialMessenger {



    public void broadcastPrepare(ProposalID proposalID, String fromUID);

    public void sendPromise(String proposerUID, String acceptorUID, ProposalID proposalID, ProposalID previousID, Object acceptedValue);

    public void sendAccept(String fromUID, ProposalID proposalID, Object proposalValue);

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue);

    public void onResolution(String learnerUID, ProposalID proposalID, Object value);

    public PrepareMessage getPrepareMessage(String acceptorUID);

    public PromiseMessage getPromiseMessage(String proposerUID);

    public AcceptMessage getAcceptMessage(String acceptorUID);

    public AcceptedMessage getAcceptedMessage(String learnerUID);
}
