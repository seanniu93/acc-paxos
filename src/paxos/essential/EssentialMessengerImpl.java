package paxos.essential;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class EssentialMessengerImpl implements EssentialMessenger {

	MessagePool messagePool;
	int quorumSize;
	ArrayList<LocationInfo> locationInfoList;
	int cid;

	public EssentialMessengerImpl(MessagePool messagePool, int quorumSize, ArrayList<LocationInfo> locationInfoList) {
		this.messagePool = messagePool;
		this.quorumSize = quorumSize;
		this.locationInfoList = locationInfoList;
		cid = 0;
	}

	public void broadcastPrepare(ProposalID proposalID, String proposerHost) {
		PrepareMessage prepareMessage = new PrepareMessage(proposerHost, proposalID);
		for (LocationInfo locationInfo : locationInfoList) {
			synchronized (locationInfo.getHostName()) {
				try (Socket socketToServer = new Socket(locationInfo.getHostName(), locationInfo.getPortNumber());
				     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
					outputStream.writeObject(prepareMessage);
				} catch (IOException e) {
					System.out.println("IO Exception while broadcasting prepare: " + e + "\n");
					e.printStackTrace();
				}
			}
			System.out.println("Broadcasting Prepare Message to host: " + locationInfo.getHostName() + ", port:" +
					locationInfo.getPortNumber() + "\n");
		}
	}

	public void sendPromise(String acceptorHost, ProposalID proposalID, ProposalID previousID, Object acceptedValue,
	                        String proposerHost, int portNumber) {
		//int proposerNum = Integer.valueOf(proposerUID);
		PromiseMessage promiseMessage = new PromiseMessage(acceptorHost, proposalID, previousID, acceptedValue);

		int index = findLocationInfoIndex(proposerHost);
		if (index == -1) {
			System.err.println("Invalid proposerHost in sendPromise()");
			return;
		}

		LocationInfo locationInfo = locationInfoList.get(index);
		synchronized (locationInfo) {
			try (Socket socketToServer = new Socket(proposerHost, portNumber);
			     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
				outputStream.writeObject(promiseMessage);
			} catch (IOException e) {
				System.out.println("IO Exception while sending promise: " + e + "\n");
			}
		}
		System.out.println("Sending Promise Message to host: " + proposerHost + ", port:" + portNumber + "\n");
	}

	public void sendAccept(String proposerHost, ProposalID proposalID, Object proposalValue) {
		AcceptMessage acceptMessage = new AcceptMessage(proposalID, proposalValue);
		for (LocationInfo locationInfo : locationInfoList) {
			synchronized (locationInfo.getHostName()) {
				try (Socket socketToServer = new Socket(locationInfo.getHostName(), locationInfo.getPortNumber());
				     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
					outputStream.writeObject(acceptMessage);
				} catch (IOException e) {
					System.out.println("IO Exception while sending accept: " + e + "\n");
				}
			}
			System.out.println("Sending Accept Message to host: " + locationInfo.getHostName() + ", port: " +
					locationInfo.getPortNumber() + "\n");
		}
	}

	public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue) {
		AcceptedMessage acceptedMessage = new AcceptedMessage(fromUID, proposalID, acceptedValue);
		for (LocationInfo locationInfo : locationInfoList) {
			synchronized (locationInfo.getHostName()) {
				try (Socket socketToServer = new Socket(locationInfo.getHostName(), locationInfo.getPortNumber());
				     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
					outputStream.writeObject(acceptedMessage);
				} catch (IOException e) {
					System.out.println("IO Exception while sending accepted: " + e + "\n");
				}
			}
			System.out.println("Sending Accepted Message to host: " + locationInfo.getHostName() +
					", port: " + locationInfo.getPortNumber() + "\n");
		}
	}

	public void addClientCommand(ClientCommand cmd)
	{
		messagePool.clientCommands.add(cmd);
		System.out.println("Client commands added to command queue in message pool");
	}

	public ClientCommand getClientCommand()
	{
		if(messagePool.clientCommands.size() == 0) {
			return null;
		}
		else {
			return messagePool.clientCommands.remove(0);
		}
	}

	public PrepareMessage getPrepareMessage(String acceptorHost) {
		int uid = findLocationInfoIndex(acceptorHost);
		if (messagePool.prepPool.get(uid).isEmpty()) {
			return null;
		} else {
			return messagePool.prepPool.get(uid).remove(0);
		}
	}

	public void addPrepareMessage(PrepareMessage prepareMessage, String acceptorHost) {
		int uid = findLocationInfoIndex(acceptorHost);
		messagePool.prepPool.get(uid).add(prepareMessage);
	}

	public PromiseMessage getPromiseMessage(String proposerHost) {
		//System.out.println("@@@@@@@@@@@ " + proposerHost);
		int uid = findLocationInfoIndex(proposerHost);
		if (messagePool.promPool.get(uid).isEmpty()) {
			return null;
		} else {
			return messagePool.promPool.get(uid).remove(0);
		}
	}

	public void addPromiseMessage(PromiseMessage promiseMessage, String proposerHost) {
		int uid = findLocationInfoIndex(proposerHost);
		messagePool.promPool.get(uid).add(promiseMessage);
	}

	public AcceptMessage getAcceptMessage(String acceptorHost) {
		int uid = findLocationInfoIndex(acceptorHost);
		if (messagePool.acceptPool.get(uid).isEmpty()) {
			return null;
		} else {
			return messagePool.acceptPool.get(uid).remove(0);
		}
	}

	public void addAcceptMessage(AcceptMessage acceptMessage, String acceptorHost) {
		int uid = findLocationInfoIndex(acceptorHost);
		messagePool.acceptPool.get(uid).add(acceptMessage);
	}

	public AcceptedMessage getAcceptedMessage(String learnerHost) {
		int uid = findLocationInfoIndex(learnerHost);
		if (messagePool.acceptedPool.get(uid).isEmpty()) {
			return null;
		} else {
			return messagePool.acceptedPool.get(uid).remove(0);
		}
	}

	public void addAcceptedMessage(AcceptedMessage acceptedMessage, String learnerHost) {
		int uid = findLocationInfoIndex(learnerHost);
		messagePool.acceptedPool.get(uid).add(acceptedMessage);
	}


	public void onResolution(String learnerUID, ProposalID proposalID, Object value) {
		if(value instanceof ClientCommand)
		{
			ClientCommand cmd = (ClientCommand) value;
			System.out.println("Learner " + learnerUID +
					" learned value: rorw: " + cmd.getRorw()+ " key: "+cmd.getKey() +" value: "+cmd.getValue()+
					" from machine: " + proposalID.getUID() +
					" proposal: " + proposalID.getNumber() + '\n');
		}

	}

	private int findLocationInfoIndex(String hostname) {
		if (hostname != null) {
			for (int i = 0; i < locationInfoList.size(); i++) {
				if (locationInfoList.get(i).getHostName().equals(hostname)) {
					return i;
				}
			}
		}
		return -1;
	}
}
