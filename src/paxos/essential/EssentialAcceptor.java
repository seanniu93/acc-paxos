package paxos.essential;

/**
 * Created by Administrator on 12/17/2015.
 */
public interface EssentialAcceptor {
    public void receivePrepare(String fromUID, ProposalID proposalID);

    public void receiveAcceptRequest(String fromUID, ProposalID proposalID, Object value);
}

