import java.io.IOException;
import java.net.*;
import java.util.Hashtable;
import java.util.jar.Pack200;

/**
 * Created by Shobhit on 10/11/2016.
 */
/*
This class implements RIP2 to replicate Routing functionality.
 */
public class RIP extends Thread{

    static Hashtable<String, CompositeObj> routingTable;
    static Hashtable<InetAddress, Integer> neighbor;
    static DatagramSocket socket;
    static int port = 9500;
    String type;
    Object object1;
    static String hostNetPrefix;
    static String dataReceived;
     InetAddress receivePacketAddress;
    static String subnet = "255.255.255.0";
    public RIP(String type)    {
        this.type = type;
    }

    /**
     * main() takes the command line arguments and fills in the neighbor and routing table.
     * The appropriate format for input is: number of neighbors each neighbor IP Cost
     * @param args  command line arguments
     */
    public static void main(String args[])  {

        // checking for appropriate command line args
        if(args.length<3)
            System.out.println("Invalid arguments.! try again");
        else {
            int noOfNeighbors = Integer.parseInt(args[0]);
            if(args.length!= (noOfNeighbors*2 + 1)) {
                System.out.println("Invalid arguments.! try again");
                System.exit(0);
            }
            try {
                hostNetPrefix = getNetworkPrefix(InetAddress.getLocalHost());
            }
            catch (UnknownHostException e)  {
                System.err.println("Error in getting local address");
            }
                routingTable = new Hashtable<>();

            neighbor = new Hashtable<>();
            for(int i = 1; i<= noOfNeighbors*2; i += 2)    {
                String neighborAddrStr = args[i];
                int cost = Integer.parseInt(args[i+1]);
                try {
                    InetAddress neighborAddr = InetAddress.getByName(neighborAddrStr);
                    neighbor.put(neighborAddr, cost);
                    String neighborNetPrefix = getNetworkPrefix(neighborAddr);
                    CompositeObj obj = new CompositeObj(neighborAddr, cost, subnet);
                    routingTable.put(neighborNetPrefix, obj);
                } catch (UnknownHostException e) {
                    System.err.println("Error while finding neighbor: " + neighborAddrStr + "\n Skipping this neighbor");
                }// end of catch

            } // end of for loop for reading neighbors
            // creating three different threads.
            RIP listenThread = new RIP("listen");
            RIP sendThread = new RIP("send");
            Object object1 = new Object();
            listenThread.object1 = object1;
            sendThread.object1 = object1;
            sendThread.openPort();
            sendThread.start();
            listenThread.start();



        } // end of else if input args are correct

    }

    /**
     * Run method handles the threads.
     *It encounters two different threads, one for sending the data and other for receiving and
     * updating the routing table.
     */
    public void run() {
        while (true)    {
        if (this.type == "listen") {
            boolean change = receiveUpdates();
            if(change)  {
                synchronized (object1)  {
                    object1.notify();
                }
            }

        } else if (this.type == "send") {
            broadcastUpdates();
            System.out.println("PRINTING ROUTING TABLE\n");
            System.out.println("Destination \t Subnet Mask \t Next Hop   \t Cost ");
            for(String x: routingTable.keySet()) {
                System.out.println(x + "\t " +routingTable.get(x).subnet + "\t "+ routingTable.get(x).nextHopAddress.toString() + "\t  " + routingTable.get(x).cost);
            }
            System.out.println();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("Printing thread awakened");
            }
            try {
                synchronized (object1) {
                    sleep(1000);
                }


            } catch (InterruptedException e) {
                System.out.println("Send thread interrupted");
            }
        }
    }
    }


    /**
     * This function is used for opening the port
      */
    public void openPort() {
    try  {
        socket = new DatagramSocket(port);
    } catch (SocketException e) {
        System.out.println("Error in opening socket");
        socket.close();
    }
}

    /**
     * for getting the network prefix of an address based on subnet 255.255.255.0
     * @param adr   the address for which the network prefix is to be determined
     * @return  the network prefix of requested address
     * @throws UnknownHostException
     */
    public static String getNetworkPrefix(InetAddress adr) throws UnknownHostException {
        //InetAddress adr = InetAddress.getByName(address);
        byte[] add_byte = adr.getAddress();
        StringBuilder sb = new StringBuilder();
        byte[] subnet = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0};
        for (int j = 0; j < add_byte.length; j++) {
            add_byte[j] = (byte) (add_byte[j] & subnet[j]);
            sb.append(String.valueOf(Byte.toUnsignedInt(add_byte[j])) + ".");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * This function is used to broadcast the routing table to neighbors
     */
    public void broadcastUpdates()   {
        for(InetAddress neighborAddr : neighbor.keySet())   {
            try {
                String nextHopNetPrefix = getNetworkPrefix(neighborAddr);
                String dataToSend = getDataToSend(nextHopNetPrefix);
                byte [] sendBuffer = dataToSend.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, neighborAddr, port);
                try {
                    socket.send(sendPacket);
                }
                catch (IOException e)   {
                    System.err.println("Issue in sending data to neighbor: " + neighborAddr.toString());
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This function is used to receive updates from neighbors
     * @return  boolean which specifies if there has been any change in the routing table due to received updates
     */
    public boolean receiveUpdates()  {
        boolean change;
        byte[] receiveBuffer = new byte[2048];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            socket.receive(receivePacket);
            dataReceived = new String(receivePacket.getData()).trim();
            receivePacketAddress = receivePacket.getAddress();
        }
        catch (IOException e)   {
            System.out.println("Error in receiving packet from: " + receivePacket.toString());
        }
        change = checkForUpdates(dataReceived);
        return change;
    }

    /**
     * this function checks if there are any updates in the routing table due to the received routing table from neighbor
     * It uses Distance Vector routing to calculate the cheapest path to a node.
     * @param dataReceived  the routing table received from neighbor
     * @return  boolean which specifies if there has been any change in the routing table due to received updates
     */
    public boolean checkForUpdates(String dataReceived) {
       boolean change = false;
        // breaking the input into different hashtable entries
        String[] lines = dataReceived.split("\n");
        //iterating over each entry, skipping the header
        for (int i = 1; i < lines.length; i++) {
            // splitting a line into its different components
            String[] lineData = lines[i].split(" ");
            // picking up the address key for routing table
            String addressKey = lineData[2];
            if(!addressKey.equals(hostNetPrefix))   {
            // taking cost, here current next hop in the received data doesn't matter
            int cost = Integer.parseInt(lineData[5]);
            // prefix of ip from where this packet was received
                String neighborNetPrefix = null;
            try {
                // get the prefix of neighbor to search inside own routing table
                neighborNetPrefix = getNetworkPrefix(receivePacketAddress);
                int costToNeighbor = routingTable.get(neighborNetPrefix).cost;
                // if the entry is not present
                if (!routingTable.containsKey(addressKey)) {
                    routingTable.put(addressKey, new CompositeObj(receivePacketAddress, cost + costToNeighbor, subnet));
                    change = true;
                }
                // if entry is present
                else {
                    int currCost = routingTable.get(addressKey).cost;
                    // if the new entry has a lower cost that old entry.
                    if (cost + costToNeighbor < currCost) {
                        change = true;
                        routingTable.get(addressKey).cost = cost + costToNeighbor;
                        routingTable.get(addressKey).nextHopAddress = routingTable.get(neighborNetPrefix).nextHopAddress;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        }
        return change;
    }

    /**
     * This function creates the data to be sent to the neighbor. It implements reverse poison to prevent count to infinity
     * problem
     * @param nextHopNetPrefix      the neighbor to which data is to be sent
     * @return      data to be sent
     */
    public String getDataToSend(String nextHopNetPrefix)   {
        StringBuilder sb = new StringBuilder();
        // appending the packet header
        // appending the command
        sb.append("2 ");
        // appending version
        sb.append("2 ");
        // appending 2 zeros
        sb.append("00\n");
        String subnet = "255.255.255.0";
        for(String addr: routingTable.keySet())   {
            // appending address family identifier: 2 for IP
            sb.append("2 ");
            // appending the routing tag
            sb.append("00 ");

            sb.append(addr);
            sb.append(" ");
            CompositeObj obj = routingTable.get(addr);
            try {
                sb.append(obj.subnet + " ");
                String nextHopInTable = getNetworkPrefix(obj.nextHopAddress);
                sb.append(obj.nextHopAddress.toString());
                sb.append(" ");
                if((nextHopInTable.equalsIgnoreCase(nextHopNetPrefix)) && !nextHopNetPrefix.equalsIgnoreCase(addr))
                    sb.append(9999);
                else
                    sb.append(obj.cost);
                sb.append("\n");

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

}
