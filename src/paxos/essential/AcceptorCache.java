package paxos.essential;

/**
 * Created by Administrator on 12/18/2015.
 */
public class AcceptorCache {
    final int quorumSize;
    ProposalID[] prevAcceptedID;
    Object[] prevAcceptedValue;

    public AcceptorCache(int quorumSize) {
        this.quorumSize = quorumSize;
        prevAcceptedID = new ProposalID[quorumSize];
        Object value = new Integer[quorumSize];

    }


}
