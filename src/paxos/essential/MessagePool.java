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
        for(int i=0; i< quorumSize; i++)
        {
            prepPool.add(new ArrayList<>());
        }
        this.promPool = new ArrayList<ArrayList<PromiseMessage>>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            promPool.add(new ArrayList<>());
        }
        this.acceptPool = new ArrayList<ArrayList<AcceptMessage>>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            acceptPool.add(new ArrayList<>());
        }
        this.acceptedPool = new ArrayList<ArrayList<AcceptedMessage>>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            acceptedPool.add(new ArrayList<>());
        }
    }
}
