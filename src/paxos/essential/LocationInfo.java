package paxos.essential;

import java.util.ArrayList;

/**
 * Created by yw486 on 12/21/15.
 */
public class LocationInfo {
    ArrayList<String> hostName;
    ArrayList<Integer> portNumber;

    public LocationInfo(ArrayList<String> hostName, ArrayList<Integer> portNUmber)
    {
        this.hostName = hostName;
        this.portNumber = portNUmber;
    }
}
