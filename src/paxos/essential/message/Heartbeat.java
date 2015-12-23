package paxos.essential.message;

public class Heartbeat extends Message {

	public Heartbeat(String hostname, int port) {
		super(hostname, port);
	}

}
