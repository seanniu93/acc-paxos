package paxos.essential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 12/18/2015.
 */



public class EssentialMessengerImpl implements EssentialMessenger {

    MessagePool messagePool;
    int quorumSize;
    LocationInfo locationInfo;

    public EssentialMessengerImpl(MessagePool messagePool, int quorumSize, LocationInfo locationInfo) {
        this.messagePool = messagePool;
        this.quorumSize = quorumSize;
        this.locationInfo = locationInfo;
    }

    public void broadcastPrepare(ProposalID proposalID, String proposerHost) {
        PrepareMessage prepareMessage = new PrepareMessage(proposerHost, proposalID);
        for(int i = 0; i < locationInfo.portNumber.size(); i++)
        {
            synchronized(locationInfo.hostName.get(i)) {
                try (Socket socketToServer = new Socket(locationInfo.hostName.get(i), locationInfo.portNumber.get(i));
                    ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
                    outputStream.writeObject(prepareMessage);
                }
                catch(IOException e) {
                    System.out.println("IO Exception while broadcasting prepare: " + e + "\n");
                }
            }
            System.out.println("Broadcasting Prepare Message to host: " + locationInfo.hostName.get(i) + ", port:" + locationInfo.portNumber.get(i) + "\n");
        }
    }

    public void sendPromise(String acceptorHost, ProposalID proposalID, ProposalID previousID, Object acceptedValue, String proposerHost, int portNumber) {
        //int proposerNum = Integer.valueOf(proposerUID);
        PromiseMessage promiseMessage = new PromiseMessage(acceptorHost, proposalID, previousID, acceptedValue);
        int index = locationInfo.hostName.indexOf(proposerHost);
        synchronized(locationInfo.hostName.get(index)) {
            try (Socket socketToServer = new Socket(proposerHost, portNumber);
                 ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
                outputStream.writeObject(promiseMessage);
            } catch (IOException e) {
                System.out.println("IO Exception while sending promise: " + e + "\n");
            }
        }
        System.out.println("Sending Promise Message to host: " + proposerHost + ", port:" + portNumber + "\n");
    }

    public void sendAccept(String proposerHost, ProposalID proposalID, Object proposalValue) {
        AcceptMessage acceptMessage = new AcceptMessage(proposalID, proposalValue);
        for(int i = 0; i < locationInfo.portNumber.size(); i++) {
            synchronized(locationInfo.hostName.get(i)) {
                try (Socket socketToServer = new Socket(locationInfo.hostName.get(i), locationInfo.portNumber.get(i));
                     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
                    outputStream.writeObject(acceptMessage);
                } catch (IOException e) {
                    System.out.println("IO Exception while sending accept: " + e + "\n");
                }
            }
            System.out.println("Sending Promise Message to host: " + locationInfo.hostName.get(i) + ", port: " +locationInfo.portNumber.get(i) + "\n");
        }
    }

    public void sendAccepted(String fromUID, ProposalID proposalID, Object acceptedValue) {
        AcceptedMessage acceptedMessage = new AcceptedMessage(fromUID, proposalID, acceptedValue);
        for(int i=0; i < quorumSize; i++) {
            synchronized(locationInfo.hostName.get(i)) {
                try (Socket socketToServer = new Socket(locationInfo.hostName.get(i), locationInfo.portNumber.get(i));
                     ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream())) {
                    outputStream.writeObject(acceptedMessage);
                } catch (IOException e) {
                    System.out.println("IO Exception while sending accepted: " + e + "\n");
                }
            }
            System.out.println("Sending Promise Message to host: " + locationInfo.hostName.get(i) + ", port: " +locationInfo.portNumber.get(i) + "\n");
        }
    }

    public PrepareMessage getPrepareMessage(String acceptorHost)
    {
        int uid = locationInfo.hostName.indexOf(acceptorHost);
        if(messagePool.prepPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.prepPool.get(uid).remove(0);
        }
    }

    public void addPrepareMessage(PrepareMessage prepareMessage, String acceptorHost)
    {
        int uid = locationInfo.hostName.indexOf(acceptorHost);
        messagePool.prepPool.get(uid).add(prepareMessage);
    }


    public PromiseMessage getPromiseMessage(String proposerHost) {
        //System.out.println("@@@@@@@@@@@ " + proposerHost);
        int uid = locationInfo.hostName.indexOf(proposerHost);
        if(messagePool.promPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.promPool.get(uid).remove(0);
        }
    }

    public void addPromiseMessage(PromiseMessage promiseMessage, String proposerHost)
    {
        int uid = locationInfo.hostName.indexOf(proposerHost);
        messagePool.promPool.get(uid).add(promiseMessage);
    }

    public AcceptMessage getAcceptMessage(String acceptorHost) {
        int uid = locationInfo.hostName.indexOf(acceptorHost);
        if(messagePool.acceptPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.acceptPool.get(uid).remove(0);
        }
    }
    public void addAcceptMessage(AcceptMessage acceptMessage, String acceptorHost)
    {
        int uid = locationInfo.hostName.indexOf(acceptorHost);
        messagePool.acceptPool.get(uid).add(acceptMessage);
    }

    public AcceptedMessage getAcceptedMessage(String learnerHost) {
        int uid = locationInfo.hostName.indexOf(learnerHost);
        if(messagePool.acceptedPool.get(uid).isEmpty())
        {
            return null;
        }
        else
        {
            return messagePool.acceptedPool.get(uid).remove(0);
        }
    }

    public void addAcceptedMessage(AcceptedMessage acceptedMessage, String learnerHost)
    {
        int uid = locationInfo.hostName.indexOf(learnerHost);
        messagePool.acceptedPool.get(uid).add(acceptedMessage);
    }


    public void onResolution(String learnerUID, ProposalID proposalID, Object value) {
        System.out.println("Learner "+learnerUID+" learned value: "+value+" from machine: "+proposalID.getUID()+ "proposal: "+ proposalID.getNumber()+'\n');

    }
}
