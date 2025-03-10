package first.Server;
import first.Rmiinterface.* ;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;



public class LoadBalancer {

    private List<String> serverURLs;

    public LoadBalancer() {
        serverURLs = new ArrayList<>();
        serverURLs.add("rmi://localhost/Server1");
        serverURLs.add("rmi://localhost/Server2");
        serverURLs.add("rmi://localhost/Server3");
        serverURLs.add("rmi://localhost/Server4");  
        // serverURLs.add("rmi://localhost/Server5");
    
    }

    public DataStorageInterface getServer() throws RemoteException {
        try {
            // Use round-robin load balancing 
            int index = (int) (Math.random() * serverURLs.size());
            DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURLs.get(index));
            return server;
        } catch (Exception e) {
            throw new RemoteException("Error getting server: " + e.getMessage(), e);
        }
    }

    // Get all server URLs (used for querying all servers)
    public List<String> getServerURLs() {
        return serverURLs;
    }
}
