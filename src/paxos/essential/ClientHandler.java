package paxos.essential;

import paxos.essential.message.ClientCommand;
import paxos.essential.message.CommandReceived;
import paxos.essential.message.Heartbeat;
import paxos.essential.message.RedirLeader;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {

	Object obj;
	EssentialMessengerImpl essentialMessengerImpl;
//	ObjectInputStream objectInputStream;
//	ObjectOutputStream objectOutputStream;
	String hostName;
	String leaderHost;
	Node node;

	public ClientHandler(Object obj, EssentialMessengerImpl essentialMessengerImpl, String hostName, String leaderHost,
	                     Node node)
			throws IOException {
		this.obj = obj;
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.hostName = hostName;
		this.leaderHost = leaderHost;
		this.node = node;
	}

	@Override
	public void run() {
		if (obj instanceof PrepareMessage) {
			System.out.println("Received prepare");
			essentialMessengerImpl.addPrepareMessage((PrepareMessage) obj, hostName);
		} else if (obj instanceof PromiseMessage) {
			essentialMessengerImpl.addPromiseMessage((PromiseMessage) obj, hostName);
		} else if (obj instanceof AcceptMessage) {
			essentialMessengerImpl.addAcceptMessage((AcceptMessage) obj, hostName);
		} else if (obj instanceof AcceptedMessage) {
			essentialMessengerImpl.addAcceptedMessage((AcceptedMessage) obj, hostName);
		} else if (obj instanceof ClientCommand) {
			handleClientCommand((ClientCommand) obj);
		} else if (obj instanceof String) {
			System.out.println("Received object from client as a string: " + (String) obj + "\n");
		} else if (obj instanceof Heartbeat) {
			System.out.println("Got heartbeat from leader");
			node.incrementHeartbeat();
		} else {
			System.out.println("Unknown type object sent from client\n");
		}
	}

	private void handleClientCommand(ClientCommand cmd) {
		try (ObjectOutputStream output = new ObjectOutputStream(new Socket(cmd.getHostname(), cmd.getPort()).getOutputStream())) {
			if (isLeader()) {
				try {
					output.writeObject(new CommandReceived(hostName, 3333));
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("I am the leader but I have not implemented multipaxos yet!\n");
				// TODO do stuff with command
			} else {
				System.out.println("I am not the leader, find someone else!\n");
				RedirLeader redirMsg = new RedirLeader(leaderHost, 3333);
				try {
					output.writeObject(redirMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLeader() {
		return leaderHost.equals(hostName);
	}

}
