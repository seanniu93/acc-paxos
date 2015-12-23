package paxos.essential;

import java.io.Serializable;

public class RedirLeader implements Serializable{
    private String hostName;

    RedirLeader (String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {return hostName; }

    public void setHostName(String hostName) {this.hostName = hostName; }
}
