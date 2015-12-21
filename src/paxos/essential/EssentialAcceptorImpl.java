package paxos.essential;

/**
 * Created by Administrator on 12/17/2015.
 */
public class EssentialAcceptorImpl extends Thread implements EssentialAcceptor {

    protected EssentialMessenger messenger;
    protected ProposalID         promisedID;
    protected ProposalID         acceptedID;
    protected Object             acceptedValue;
    protected String             acceptorHost;
    protected int                portNumber;
    protected int                quorumSize;
    protected LocationInfo       locationInfo;
    boolean                      active;

    public EssentialAcceptorImpl( EssentialMessenger messenger, String acceptorHost, int quorumSize, int portNumber, LocationInfo locationInfo) {
        this.messenger = messenger;
        this.acceptorHost = acceptorHost;
        this.quorumSize = quorumSize;
        this.portNumber = portNumber;
        this.locationInfo = locationInfo;
    }

    @Override
    public void receivePrepare(String proposerHost, ProposalID proposalID) {

        if (this.promisedID != null && proposalID.equals(promisedID)) { // duplicate message
            messenger.sendPromise(acceptorHost, proposalID, acceptedID, acceptedValue, proposerHost, portNumber);
        }
        else if (this.promisedID == null || proposalID.isGreaterThan(promisedID)) {
            promisedID = proposalID;
            messenger.sendPromise(acceptorHost, proposalID, acceptedID, acceptedValue, proposerHost, portNumber);
        }
    }

    @Override
    public void receiveAcceptRequest( ProposalID proposalID,
                                      Object value) {
        if (promisedID == null || proposalID.isGreaterThan(promisedID) || proposalID.equals(promisedID)) {
            promisedID    = proposalID;
            acceptedID    = proposalID;
            acceptedValue = value;

            messenger.sendAccepted(acceptorHost, acceptedID, acceptedValue, locationInfo);
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

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void run() {
        long endTimeMillis = System.currentTimeMillis() + 10000;
        PrepareMessage prepareMessage;
        AcceptMessage acceptMessage;
        while(true)
        {
            if((prepareMessage = messenger.getPrepareMessage(acceptorHost))!=null) {
                //System.out.println("Received Prepare: \n");
                //System.out.println("prepare proposerUID: " + prepareMessage.proposerUID + "\n");
                //System.out.println("prepare proposalID: " + prepareMessage.proposalID + "\n");
                receivePrepare(prepareMessage.proposerHost, prepareMessage.proposalID);
            }
            if((acceptMessage = messenger.getAcceptMessage(acceptorHost))!=null) {
                receiveAcceptRequest(acceptMessage.proposalID, acceptMessage.value);
            }
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                return;
            }
        }


    }
}
