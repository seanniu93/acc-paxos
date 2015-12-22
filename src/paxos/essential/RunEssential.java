package paxos.essential;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RunEssential {

	static int quorumSize = 2;
	static MessagePool messagePool = new MessagePool(quorumSize);

	//add hostname and port number here;

	public static void main(String[] args) {
		System.out.println("Hello, World!");
		String myHostName = null;
		try {
			myHostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Integer myport = 3333;

		ArrayList<LocationInfo> locationInfoList = new ArrayList<>();
		locationInfoList.add(new LocationInfo("frog.zoo.cs.yale.edu", 3333));
		locationInfoList.add(new LocationInfo("bumblebee.zoo.cs.yale.edu", 3333));
		MessagePool messagePool = new MessagePool(quorumSize);
		EssentialMessengerImpl essentialMessengerImpl = new EssentialMessengerImpl(messagePool, quorumSize,
				locationInfoList);

		Node node = new Node(essentialMessengerImpl, myHostName, quorumSize, myport, locationInfoList);
		node.start();
	}
}
