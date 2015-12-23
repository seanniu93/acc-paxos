package paxos.essential;

import java.util.ArrayList;
import java.util.HashSet;
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

    protected ArrayList<LocationInfo> locationInfoList;

    protected String leaderHost;
    protected boolean active = true;

    public EssentialProposerImpl(EssentialMessenger messenger, String proposerHost, int quorumSize,
                                 ArrayList<LocationInfo> locationInfoList, String leaderHost) {
        this.messenger   = messenger;
        this.proposerHost = proposerHost;
        this.quorumSize  = quorumSize;
        this.proposalID  = new ProposalID(0, proposerHost);
        this.leaderHost = leaderHost;
        this.locationInfoList = locationInfoList;
    }

    public void receiveFromClients(ClientCommand command) {
        if (leaderHost.equals(proposerHost)) //respond to the client request
        {
            proposalID.incrementNumber();
            messenger.sendAccept(proposerHost, proposalID, proposedValue);
        } else    //forward the message to leader for processing
        {
            //messenger.send_leader(proposedValue);
        }
    }

    @Override
    public void setProposal(Object value) {
        if (proposedValue == null)
            proposedValue = value;
        if (isLeader() && active)
            messenger.sendAccept(proposerHost, proposalID, proposedValue);

    }

    @Override
    public void prepare() {

        promisesReceived.clear();

        proposalID.incrementNumber();

        if (active) {
            messenger.broadcastPrepare(proposalID, proposerHost);
        }
    }

    public void resendAccept() {
        if (isLeader() && active && proposedValue != null) {
            messenger.sendAccept(proposerHost, proposalID, proposedValue);
        }
    }

    @Override
    public void receivePromise(String acceptorUID, ProposalID proposalID,
                               ProposalID prevAcceptedID, Object prevAcceptedValue) {

        if (isLeader() || !proposalID.equals(this.proposalID) || promisesReceived.contains(acceptorUID))
            return;

        promisesReceived.add(acceptorUID);

        if (lastAcceptedID == null || prevAcceptedID.isGreaterThan(lastAcceptedID)) {
            lastAcceptedID = prevAcceptedID;

            if (prevAcceptedValue != null)
                proposedValue = prevAcceptedValue;
        }

        if (promisesReceived.size() > quorumSize / 2) {
            leaderHost = proposerHost;
            if (proposedValue != null)
                messenger.sendAccept(this.proposerHost, proposalID, proposedValue);
            else {
                if(messenger.getClientCommand() == null) {
                    System.out.println("The client command has not arrived yet\n");
                }
                else {
                    messenger.sendAccept(this.proposerHost, proposalID, messenger.getClientCommand());
                }
            }
        }
    }

//    public int prepare_promise(ProposalID proposalID, ProposalID prevAcceptedID, Object prevAcceptedValue) {
//        int numPromise = 0;
//        String fromUID;
//        prepare();
//        while (true) {
//            for (int machineNum = 0; machineNum < quorumSize; machineNum++) {
//                receivePromise(Integer.toString(machineNum), proposalID, prevAcceptedID, prevAcceptedValue);
//            }
//        }
//    }

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

	public boolean isActive() {
		return active;
	}

	public boolean isLeader() {
		return leaderHost.equals(proposerHost);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setLeader(String leaderHost) {
		this.leaderHost = leaderHost;
	}

	public Integer getRandProposal() {
		Integer newValue;
		newValue = new Integer(ThreadLocalRandom.current().nextInt(10, 10 + 10));
		System.out.println("Newly Proposed Value: " + newValue + "\n\n");
		return newValue;
	}

//	Object getBestVal(ArrayList<PromiseMessage> promises) {
//		ProposalID maxproposal = new ProposalID(0, new Integer(0).toString());
//		Object bestVal = null;
//		for (int i = 0; i < promises.size(); i++) {
//			if (promises.get(i).proposalID.isGreaterThan(maxproposal)) {
//				maxproposal = promises.get(i).prevAcceptedID;
//				bestVal = promises.get(i).prevAcceptedValue;
//			}
//		}
//		return bestVal;
//	}

	public void run() {
//		ArrayList<PromiseMessage> promises = new ArrayList<PromiseMessage>();
		prepare();
//		long endTimeMillis = System.currentTimeMillis() + 10000;
		PromiseMessage promiseMessage;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
			if ((promiseMessage = messenger.getPromiseMessage(proposerHost)) != null) {
				receivePromise(promiseMessage.acceptorHost, promiseMessage.proposalID, promiseMessage.prevAcceptedID,
						promiseMessage.prevAcceptedValue);
			}
		}
	}

}
