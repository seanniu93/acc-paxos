package paxos.essential.message;

public class ClientCommand extends Message {

	private int cid;
	private String rorw;
	private String key;
	private String value;

	public ClientCommand(String hostname, int port, String rorw, String key, String value) {
		super(hostname, port);
		this.rorw = rorw;
		this.key = key;
		this.value = value;
	}

	public void setCid(Integer cid) { this.cid = cid; }

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public  String getRorw() { return rorw; }

	public Integer getCid() { return cid; }

}
