package paxos.essential;

/**
 * Created by yw486 on 12/20/15.
 */
public class ServerToServerProtocol {
    public String processInput(String b)
    {
        if(b == null)
        {
            return new String("Greeting from server!\n");
        }
        return b;
    }
}
