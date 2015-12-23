package paxos.essential;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {

	Socket clientSocket;
	EssentialMessengerImpl essentialMessengerImpl;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	String hostName;
	String leaderHost;

	public ClientHandler(Socket clientSocket, EssentialMessengerImpl essentialMessengerImpl, String hostName, String leaderHost)
			throws IOException {
		this.clientSocket = clientSocket;
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.hostName = hostName;
		this.leaderHost = leaderHost;
		objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		objectOutputStream = new ObjectOutputStream((clientSocket.getOutputStream()));
	}

	void handleCommand(ClientCommand cmd) {

	}

	public void run() {
		Object o = null;
		try {
			o = objectInputStream.readObject();
			System.out.println("Read object: " + o);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (o == null) {
			System.out.println("Received Message from client is null");
		} else {
			if (o instanceof PrepareMessage) {
				essentialMessengerImpl.addPrepareMessage((PrepareMessage) o, hostName);
			} else if (o instanceof PromiseMessage) {
				essentialMessengerImpl.addPromiseMessage((PromiseMessage) o, hostName);
			} else if (o instanceof AcceptMessage) {
				essentialMessengerImpl.addAcceptMessage((AcceptMessage) o, hostName);
			} else if (o instanceof AcceptedMessage) {
				essentialMessengerImpl.addAcceptedMessage((AcceptedMessage) o, hostName);
			} else if(o instanceof ClientCommand) {
				if(isLeader()) {
					try {
						objectOutputStream.writeObject(new CommandReceived());
					} catch (IOException e) {
						e.printStackTrace();
					}
					handleCommand((ClientCommand) o);
				}
				else {
					RedirLeader redirMsg = new RedirLeader(leaderHost);
					try {
						objectOutputStream.writeObject(redirMsg);
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
			else if (o instanceof String) {
				System.out.println("Received obkject from client as a string: " + o + "\n");
			} else {
				System.out.println("Unknown type object sent from client from client\n");
			}
		}
	}

	public boolean isLeader() { return leaderHost.equals(hostName); }
}
