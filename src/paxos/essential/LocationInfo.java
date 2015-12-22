package paxos.essential;

public class LocationInfo {

	private String hostName;

	private int portNumber;

	public LocationInfo(String hostName, int portNUmber) {
		this.hostName = hostName;
		this.portNumber = portNUmber;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public String getHostName() {
		return hostName;
	}

}
