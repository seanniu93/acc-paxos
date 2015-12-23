package paxos.essential.message;

class LeadershipAccepted extends Message {

	public LeadershipAccepted(String hostname, int port) {
		super(hostname, port);
	}

}
