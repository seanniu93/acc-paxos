package paxos.essential;
import java.util.*;

/**
 * Created by Administrator on 12/18/2015.
 */
public class MessagePool {
    protected final int quorumSize;

    ArrayList<ArrayList<PrepareMessage>> prepPool;
    ArrayList<ArrayList<PromiseMessage>> promPool;
    ArrayList<ArrayList<AcceptMessage>> acceptPool;
    ArrayList<ArrayList<AcceptedMessage>> acceptedPool;


    MessagePool(int quorumSize) {
        this.quorumSize = quorumSize;
        this.prepPool= new ArrayList<ArrayList<PrepareMessage>>(quorumSize);
    }
}
