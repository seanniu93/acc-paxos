package paxos.essential;

import com.sun.deploy.util.SessionState;

/**
 * Created by Administrator on 12/18/2015.
 */
public interface EssentialMessenger {



    public void broadcastPrepare(ProposalID proposalID, String proposerHost, LocationInfo locationInfo);

    public void sendPromise(String acceptorHost, ProposalID proposalID, ProposalID previousID, Object acceptedValue, String proposerHost, int portNumber);

    public void sendAccept(String proposerHost, ProposalID proposalID, Object proposalValue, LocationInfo locationInfo);

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue, LocationInfo locationInfo);

    public void onResolution(String learnerUID, ProposalID proposalID, Object value);

    public PrepareMessage getPrepareMessage(String acceptorUID);

    public PromiseMessage getPromiseMessage(String proposerUID);

    public AcceptMessage getAcceptMessage(String acceptorUID);

    public AcceptedMessage getAcceptedMessage(String learnerUID);

    public void addPrepareMessage(PrepareMessage prepareMessage, String acceptorHost);

    public void addPromiseMessage(PromiseMessage promiseMessage, String proposerHost);

    public void addAcceptMessage(AcceptMessage acceptMessage, String acceptorHost);

    public void addAcceptedMessage(AcceptedMessage acceptedMessage, String learnerHost);

}
