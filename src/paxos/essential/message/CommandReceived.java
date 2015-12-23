package paxos.essential.message;

public class CommandReceived extends Message {

	public CommandReceived(String hostname, int port) {
		super(hostname, port);
	}
}
