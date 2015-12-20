package paxos.essential;

/**
 * Created by yw486 on 12/20/15.
 */
public class ServerToServerProtocol {
    public String processInput(Object b)
    {
        String outputLine;
        if(b.equals(null))
        {
            return new String("Greeting from server!\n");
        }
        if(b instanceof  String && b.equals("Client"))
        {
            outputLine = (String) b;
            return outputLine;
        }
        else if(b instanceof ClientCommand)
        {
            outputLine = "Received client command, key: " + ((ClientCommand) b).key + ", value: " + ((ClientCommand) b).value + "\n";
            return outputLine;
        }
        else
        {
            return new String("Invalid type of instance");
        }
    }
}
