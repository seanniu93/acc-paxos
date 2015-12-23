package paxos.essential;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EssentialListenerImpl extends Thread implements EssentialListener {

	int portNumber;
	String hostName;
	private String leaderHost;
	EssentialMessengerImpl essentialMessengerImpl;

	EssentialListenerImpl(int portNumber, String hostName, EssentialMessengerImpl essentialMessengerImpl, String leaderHost) {
		this.portNumber = portNumber;
		this.hostName = hostName;
		this.essentialMessengerImpl = essentialMessengerImpl;
		this.leaderHost = leaderHost;
	}

	public void run() {
		startListening();
	}
	public String getLeader() { return leaderHost; }
	public void setLeader(String leaderHost) { this.leaderHost = leaderHost; }

	public void startListening() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println("Create serverSocket failed in Node with error " + e + "\n");
			e.printStackTrace();
		}

		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("Accept client socket failed in Node with error " + e + "\n");
				e.printStackTrace();
			}

			ClientHandler clientHandler = null;
			try {
				clientHandler = new ClientHandler(clientSocket, essentialMessengerImpl, hostName, leaderHost);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (clientHandler != null) {
				clientHandler.start();
			} else {
				System.out.println("ClientHandler is initialized to null\n");
			}
		}
	}

}
