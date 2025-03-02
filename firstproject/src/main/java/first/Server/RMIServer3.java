package first.Server ; 

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer3 {
    public static void main(String[] args) {
        try {
            // Create registry for the server
            LocateRegistry.createRegistry(1197);

            // Create remote object
            DataStorageServer server = new DataStorageServer();

            // Bind remote object to RMI registry
            Naming.rebind("rmi://localhost/Server3", server);

            System.out.println("Server3 is ready...");
        } catch (Exception e) {
            System.out.println("Server3 exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
