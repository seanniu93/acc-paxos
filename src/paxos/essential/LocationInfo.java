package paxos.essential;

import java.util.ArrayList;

public class LocationInfo {

	ArrayList<String> hostName;
	ArrayList<Integer> portNumber;

	public LocationInfo(ArrayList<String> hostName, ArrayList<Integer> portNUmber) {
		this.hostName = hostName;
		this.portNumber = portNUmber;
	}

}
