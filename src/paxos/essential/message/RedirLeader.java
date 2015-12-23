package paxos.essential.message;

public class RedirLeader extends Message {

	public RedirLeader(String hostname, int port) {
		super(hostname, port);
	}

}
