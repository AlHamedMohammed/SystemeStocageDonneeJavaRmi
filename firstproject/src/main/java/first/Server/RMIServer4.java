package first.Server;
// import Rmiinterface.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class RMIServer4 {
    public static void main(String[] args) {
        try {
            // Create registry for the server
            LocateRegistry.createRegistry(1196);

            // Create remote object
            DataStorageServer server = new DataStorageServer();

            // Bind remote object to RMI registry
            Naming.rebind("rmi://localhost/Server4", server);

            System.out.println("Server4 is ready...");
        } catch (Exception e) {
            System.out.println("Server1 exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

