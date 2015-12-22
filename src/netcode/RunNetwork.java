package netcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RunNetwork {

	public static void main(String[] args) throws IOException {
		//Server
		Thread threadServer = new Thread(new Runnable() {
			@Override
			public void run() {
				int portnumber = 4446;

				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(portnumber);
				} catch (IOException e) {
					e.printStackTrace();
				}

				while (true) {
					Socket clientSocket = null;
					try {
						clientSocket = serverSocket.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ClientHandler clientHandler = null;
					try {
						clientHandler = new ClientHandler(clientSocket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					clientHandler.start();
					break;
				}
			}
		});
		Thread threadClient = new Thread(new Runnable() {
			@Override
			public void run() {
				int portnumber = 4446;
				//Client
				String hostName = new String("frog.zoo.cs.yale.edu");
				PrintWriter out;
				BufferedReader in;
				try {
					Socket kkSocket = new Socket(hostName, portnumber);
					out = new PrintWriter(kkSocket.getOutputStream(), true);
					in = new BufferedReader(
							new InputStreamReader(kkSocket.getInputStream()));

					String fromServer;
					while ((fromServer = in.readLine()) != null) {
						System.out.println("Server: " + fromServer);
						if (fromServer.equals("Bye")) {
							break;
						}
						//BufferedReader stdin;
						//stdin = new BufferedReader(new InputStreamReader(System.in));
						String fromUser = "Ahahahahahaha\n";
						if (fromServer != null) {
							//System.out.println("Client: " + fromUser);
							out.println(fromUser);
						}
					}
				} catch (IOException e) {
					System.out.println("IO Exception while creating client socket\n");
				}
			}
		});
		threadServer.start();
		threadClient.start();
	}

}
