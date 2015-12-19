package paxos.essential;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 12/18/2015.
 */
public class EssentialProposerImpl extends Thread implements EssentialProposer {
    protected EssentialMessenger  messenger;
    protected String              proposerUID;
    protected final int           quorumSize;

    protected ProposalID          proposalID;
    protected Object              proposedValue      = null;
    protected ProposalID          lastAcceptedID     = null;
    protected HashSet<String>     promisesReceived   = new HashSet<String>();

    public EssentialProposerImpl(EssentialMessenger messenger, String proposerUID, int quorumSize) {
        this.messenger   = messenger;
        this.proposerUID = proposerUID;
        this.quorumSize  = quorumSize;
        this.proposalID  = new ProposalID(0, proposerUID);
    }

    @Override
    public void setProposal(Object value) {
        if ( proposedValue == null )
            proposedValue = value;
    }

    @Override
    public void prepare() {
        promisesReceived.clear();

        proposalID.incrementNumber();

        messenger.broadcastPrepare(proposalID, proposerUID);
    }

    @Override
    public void receivePromise(String acceptorUID, ProposalID proposalID,
                               ProposalID prevAcceptedID, Object prevAcceptedValue) {

        if ( !proposalID.equals(this.proposalID) || promisesReceived.contains(acceptorUID) )
            return;

        promisesReceived.add( acceptorUID );

        if (lastAcceptedID == null || prevAcceptedID.isGreaterThan(lastAcceptedID))
        {
            lastAcceptedID = prevAcceptedID;

            if (prevAcceptedValue != null)
                proposedValue = prevAcceptedValue;
        }

        if (promisesReceived.size() == quorumSize) {
            if (proposedValue != null)
                messenger.sendAccept(this.proposerUID, proposalID, proposedValue);
            else
                messenger.sendAccept(this.proposerUID, proposalID, getRandProposal());
        }
    }
/*
    public int prepare_promise(ProposalID proposalID, ProposalID prevAcceptedID, Object prevAcceptedValue) {
        int numPromise = 0;
        String fromUID;
        prepare();
        while(true) {
            for (int machineNum = 0; machineNum < quorumSize; machineNum++) {
                receivePromise(Integer.toString(machineNum), proposalID, prevAcceptedID, prevAcceptedValue);
            }
        }
    }
*/
    public EssentialMessenger getMessenger() {
        return messenger;
    }

    public String getProposerUID() {
        return proposerUID;
    }

    public int getQuorumSize() {
        return quorumSize;
    }

    public ProposalID getProposalID() {
        return proposalID;
    }

    public Object getProposedValue() {
        return proposedValue;
    }

    public ProposalID getLastAcceptedID() {
        return lastAcceptedID;
    }

    public int numPromises() {
        return promisesReceived.size();
    }

    public Integer getRandProposal()
    {
        Integer newValue;
        newValue = new Integer(ThreadLocalRandom.current().nextInt(10, 10 + 1));
        return newValue;
    }
/*
    Object getBestVal(ArrayList<PromiseMessage> promises) {
        ProposalID maxproposal = new ProposalID(0, new Integer(0).toString());
        Object bestVal = null;
        for(int i = 0; i < promises.size(); i++)
        {
            if(promises.get(i).proposalID.isGreaterThan(maxproposal))
            {
                maxproposal = promises.get(i).prevAcceptedID;
                bestVal = promises.get(i).prevAcceptedValue;
            }
        }
        return bestVal;
    }
*/
    public void run() {
        //ArrayList<PromiseMessage> promises = new ArrayList<PromiseMessage>();
        prepare();
        long endTimeMillis = System.currentTimeMillis() + 10000;
        PromiseMessage promiseMessage;
        while(true)
        {
            if((promiseMessage = messenger.getPromiseMessage(proposerUID))!=null)
            {
                receivePromise(promiseMessage.fromUID, promiseMessage.proposalID, promiseMessage.prevAcceptedID, promiseMessage.prevAcceptedValue);
                System.out.println("Received Promise "+promiseMessage.fromUID+" "+promiseMessage.prevAcceptedValue+" "+ promiseMessage.prevAcceptedID + " "+promiseMessage.proposalID);
            }
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                return;
            }
        }
    }


}
