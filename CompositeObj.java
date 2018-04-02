import java.net.InetAddress;

/**
 * Created by Shobhit on 10/7/2016.
 */

/**
 * This class is used as a value in the key value pair for routing table.
 */
public class CompositeObj {
    //cost
    int cost;
    //next hop
    InetAddress nextHopAddress;
    // subnet
    String subnet;
    public CompositeObj(InetAddress addr, int cost, String subnet)   {
        nextHopAddress = addr;
        this.cost = cost;
        this.subnet = subnet;
    }

}
