package paxos.essential;

public class ClientCommand {

	private String key;
	private String value;

	public ClientCommand(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
