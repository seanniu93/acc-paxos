package paxos.essential;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 12/18/2015.
 */
public class EssentialProposerImpl extends Thread implements EssentialProposer {
    protected EssentialMessenger  messenger;
    protected String              proposerHost;
    protected final int           quorumSize;

    protected ProposalID          proposalID;
    protected Object              proposedValue;
    protected ProposalID          lastAcceptedID     = null;
    protected HashSet<String>     promisesReceived   = new HashSet<String>();

    protected LocationInfo        locationInfo;

    protected boolean leader = false;
    protected boolean active = true;

    public EssentialProposerImpl(EssentialMessenger messenger, String proposerHost, int quorumSize, LocationInfo locationInfo) {
        this.messenger   = messenger;
        this.proposerHost = proposerHost;
        this.quorumSize  = quorumSize;
        this.proposalID  = new ProposalID(0, proposerHost);
        this.leader = false;
        this.locationInfo = locationInfo;
    }

    public void receiveFromClients(ClientCommand command)
    {
        if(leader) //respond to the client request
        {
            proposalID.incrementNumber();
            messenger.sendAccept(proposerHost, proposalID, proposedValue, locationInfo);
        }
        else    //forward the message to leader for processing
        {
            //messenger.send_leader(proposedValue);
        }
    }


    @Override
    public void setProposal(Object value) {
        if ( proposedValue == null )
            proposedValue = value;
        if (leader && active)
            messenger.sendAccept(proposerHost, proposalID, proposedValue, locationInfo);

    }

    @Override
    public void prepare() {
        leader = false;

        promisesReceived.clear();

        proposalID.incrementNumber();

        if(active) {
            messenger.broadcastPrepare(proposalID, proposerHost, locationInfo);
        }
    }

    public void resendAccept() {
        if(leader && active && proposedValue != null)
        {
            messenger.sendAccept(proposerHost, proposalID, proposedValue, locationInfo);

        }
    }

    @Override
    public void receivePromise(String acceptorUID, ProposalID proposalID,
                               ProposalID prevAcceptedID, Object prevAcceptedValue) {

        if ( leader || !proposalID.equals(this.proposalID) || promisesReceived.contains(acceptorUID) )
            return;

        promisesReceived.add( acceptorUID );

        if (lastAcceptedID == null || prevAcceptedID.isGreaterThan(lastAcceptedID))
        {
            lastAcceptedID = prevAcceptedID;

            if (prevAcceptedValue != null)
                proposedValue = prevAcceptedValue;
        }

        if (promisesReceived.size() > quorumSize/2) {
            leader = true;
            if (proposedValue != null)
                messenger.sendAccept(this.proposerHost, proposalID, proposedValue, locationInfo);
            else
                messenger.sendAccept(this.proposerHost, proposalID, getRandProposal(), locationInfo);
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

    public String getProposerHost() {
        return proposerHost;
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

    public boolean isActive() { return active;}
    public boolean isLeader() { return leader; }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setLeader(boolean leader)
    {
        this.leader = leader;
    }


    public Integer getRandProposal()
    {
        Integer newValue;
        newValue = new Integer(ThreadLocalRandom.current().nextInt(10, 10 + 10));
        System.out.println("Newly Proposed Value: " + newValue + "\n\n");
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
            if((promiseMessage = messenger.getPromiseMessage(proposerHost))!=null)
            {
                receivePromise(promiseMessage.acceptorHost, promiseMessage.proposalID, promiseMessage.prevAcceptedID, promiseMessage.prevAcceptedValue);
                //System.out.println("Received Promise "+promiseMessage.fromUID+" "+promiseMessage.prevAcceptedValue+" "+ promiseMessage.prevAcceptedID + " "+promiseMessage.proposalID);
            }
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                return;
            }
        }
    }


}

