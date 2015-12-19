package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public interface EssentialMessenger {



    public void broadcastPrepare(ProposalID proposalID, String fromUID);

    public void sendPromise(String proposerUID, ProposalID proposalID, ProposalID previousID, Object acceptedValue);

    public void sendAccept(String fromUID, ProposalID proposalID, Object proposalValue);

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue);

    public void onResolution(ProposalID proposalID, Object value);
}
