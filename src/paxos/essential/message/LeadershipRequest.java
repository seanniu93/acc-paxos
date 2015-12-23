package paxos.essential.message;

public class LeadershipRequest extends Message {

	public LeadershipRequest(String hostname, int port) {
		super(hostname, port);
	}

}
