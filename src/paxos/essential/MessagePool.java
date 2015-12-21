package paxos.essential;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 12/18/2015.
 */
public class MessagePool {
    protected final int quorumSize;

    ArrayList<CopyOnWriteArrayList<PrepareMessage>> prepPool;
    ArrayList<CopyOnWriteArrayList<PromiseMessage>> promPool;
    ArrayList<CopyOnWriteArrayList<AcceptMessage>> acceptPool;
    ArrayList<CopyOnWriteArrayList<AcceptedMessage>> acceptedPool;


    public MessagePool(int quorumSize) {
        this.quorumSize = quorumSize;
        this.prepPool= new ArrayList<>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            prepPool.add(new CopyOnWriteArrayList<>());
        }
        this.promPool = new ArrayList<>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            promPool.add(new CopyOnWriteArrayList<>());
        }
        this.acceptPool = new ArrayList<>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            acceptPool.add(new CopyOnWriteArrayList<>());
        }
        this.acceptedPool = new ArrayList<>(quorumSize);
        for(int i=0; i< quorumSize; i++)
        {
            acceptedPool.add(new CopyOnWriteArrayList<>());
        }
    }
}
