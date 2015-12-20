package paxos.essential;

/**
 * Created by Administrator on 12/17/2015.
 */
public class EssentialAcceptorImpl extends Thread implements EssentialAcceptor {

    protected EssentialMessenger messenger;
    protected ProposalID         promisedID;
    protected ProposalID         acceptedID;
    protected Object             acceptedValue;
    protected String             acceptorUID;
    protected int                quorumSize;

    public EssentialAcceptorImpl( EssentialMessenger messenger, String acceptorUID, int quorumSize ) {
        this.messenger = messenger;
        this.acceptorUID = acceptorUID;
        this.quorumSize = quorumSize;
    }

    @Override
    public void receivePrepare(String proposerUID, ProposalID proposalID) {

        if (this.promisedID != null && proposalID.equals(promisedID)) { // duplicate message
            messenger.sendPromise(proposerUID, acceptorUID, proposalID, acceptedID, acceptedValue);
        }
        else if (this.promisedID == null || proposalID.isGreaterThan(promisedID)) {
            promisedID = proposalID;
            messenger.sendPromise(proposerUID, acceptorUID, proposalID, acceptedID, acceptedValue);
        }
    }

    @Override
    public void receiveAcceptRequest( ProposalID proposalID,
                                     Object value) {
        if (promisedID == null || proposalID.isGreaterThan(promisedID) || proposalID.equals(promisedID)) {
            promisedID    = proposalID;
            acceptedID    = proposalID;
            acceptedValue = value;

            messenger.sendAccepted(acceptorUID, acceptedID, acceptedValue);
        }
    }

    public EssentialMessenger getMessenger() {
        return messenger;
    }

    public ProposalID getPromisedID() {
        return promisedID;
    }

    public ProposalID getAcceptedID() {
        return acceptedID;
    }

    public Object getAcceptedValue() {
        return acceptedValue;
    }

    public void run() {
        long endTimeMillis = System.currentTimeMillis() + 10000;
        PrepareMessage prepareMessage;
        AcceptMessage acceptMessage;
        while(true)
        {
            if((prepareMessage = messenger.getPrepareMessage(acceptorUID))!=null) {
                //System.out.println("Received Prepare: \n");
                //System.out.println("prepare proposerUID: " + prepareMessage.proposerUID + "\n");
                //System.out.println("prepare proposalID: " + prepareMessage.proposalID + "\n");
                receivePrepare(prepareMessage.proposerUID, prepareMessage.proposalID);
            }
            if((acceptMessage = messenger.getAcceptMessage(acceptorUID))!=null) {
                receiveAcceptRequest(acceptMessage.proposalID, acceptMessage.value);
            }
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                return;
            }
        }


    }
}
