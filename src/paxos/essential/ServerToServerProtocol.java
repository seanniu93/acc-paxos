package paxos.essential;


public class ServerToServerProtocol {

	public String processInput(String b) {
		if (b == null) {
			return new String("Greeting from server!\n");
		}
		return b;
	}

}
