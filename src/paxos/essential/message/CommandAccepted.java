package paxos.essential.message;

public class CommandAccepted extends Message {

	public CommandAccepted(String hostname, int port) {
		super(hostname, port);
	}

}
