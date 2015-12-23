package paxos.essential;

/**
 * Created by Administrator on 12/22/2015.
 */
public class RedirLeader {
    private String hostName;

    RedirLeader (String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {return hostName; }

    public void setHostName(String hostName) {this.hostName = hostName; }
}
