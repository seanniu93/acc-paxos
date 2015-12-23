package paxos.essential.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private String hostname;
	private int port;

	public Message(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

}
