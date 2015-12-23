package paxos.essential;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EssentialListenerImpl extends Thread implements EssentialListener {

	int portNumber;
	String hostName;
	private String leaderHost;
	EssentialMessengerImpl essentialMessengerImpl;

	EssentialListenerImpl(int portNumber, String hostName, EssentialMessengerImpl essentialMessengerImpl,
	                      String leaderHost) {
		this.portNumber = portNumber;
		this.hostName = hostName;
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.leaderHost = leaderHost;
	}

	public void run() {
		startListening();
	}

	public String getLeader() {
		return leaderHost;
	}

	public void setLeader(String leaderHost) {
		this.leaderHost = leaderHost;
	}

	public void startListening() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println("Create serverSocket failed in Node with error " + e + "\n");
			e.printStackTrace();
		}
		if (serverSocket == null)
			System.exit(-1);

		while (true) {
			try (Socket clientSocket = serverSocket.accept();
			     ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
			) {
				Object o = null;
				try {
					o = input.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				if (o == null) {
					System.err.println("Object from socket was null!");
					continue;
				}

				ClientHandler clientHandler = null;
				try {
					clientHandler = new ClientHandler(o, essentialMessengerImpl, hostName, leaderHost);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (clientHandler != null) {
					clientHandler.start();
				} else {
					System.out.println("ClientHandler is initialized to null\n");
				}
			} catch (IOException e) {
				System.out.println("Accept client socket failed in Node with error " + e + "\n");
				e.printStackTrace();
			}
		}
	}

}
