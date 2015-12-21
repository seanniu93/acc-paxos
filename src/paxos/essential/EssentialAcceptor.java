package paxos.essential;

/**
 * Created by Administrator on 12/17/2015.
 */
public interface EssentialAcceptor {
    public void receivePrepare(String proposerHost, ProposalID proposalID);

    public void receiveAcceptRequest(ProposalID proposalID, Object value);
}
