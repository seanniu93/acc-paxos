package netcode;

import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import paxos.essential.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RunNetwork {


	public static void main(String[] args) throws IOException {
		//Server
		ArrayList<LocationInfo> locationInfoList = new ArrayList<>();
		locationInfoList.add(new LocationInfo("frog.zoo.cs.yale.edu", 3333));
		//locationInfoList.add(new LocationInfo("bumblebee.zoo.cs.yale.edu", 3333));

		int targetServerIndex = ThreadLocalRandom.current().nextInt(0, locationInfoList.size());
		String hostName = locationInfoList.get(targetServerIndex).getHostName();
		int portNumber = locationInfoList.get(targetServerIndex).getPortNumber();

		//Client
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			try {
				//Read command from client
				System.out.println("Read or Write: ");
				String rorw = cin.readLine();
				if(!rorw.equals("read") && !rorw.equals("write")) {
					System.out.println("Illigal command\n");
					continue;
				}
				System.out.println("Enter Key: ");
				String key = cin.readLine();
				System.out.println("Enter Value: ");
				String value = cin.readLine();
				ClientCommand cmd = new ClientCommand(rorw, key, value);



				//Sending request to server
				Socket socketToServer = new Socket(hostName, portNumber);
				ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream());
				ObjectInputStream objectInputStream = new ObjectInputStream(socketToServer.getInputStream());

				outputStream.writeObject((cmd));



				Object fromServer = null;
				long startTime = System.currentTimeMillis();
				while (true) {
					try{
						fromServer = objectInputStream.readObject();
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					if(fromServer instanceof CommandReceived) {
						System.out.println("command received by server");
					}
					else if(fromServer instanceof RedirLeader) {
						hostName = ((RedirLeader) fromServer).getHostName();
						break;
					}
					else if(fromServer instanceof CommandAccepted) {
						System.out.println("command accepted by server");
						break;
					}
					else {
						System.out.println("Unknown message from server");
					}

					if((System.currentTimeMillis()-startTime)>10000) {
						System.out.println("Connecting to server TLE");
					}
				}
			} catch (IOException e) {
				System.out.println("IO Exception while creating client socket\n");
			}
		}
	}

}
