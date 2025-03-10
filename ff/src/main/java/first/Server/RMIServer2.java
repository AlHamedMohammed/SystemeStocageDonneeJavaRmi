package first.Server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer2 {
    public static void main(String[] args) {
        try {
            // Create registry for the server
            LocateRegistry.createRegistry(1098);

            // Create remote object
            DataStorageServer server = new DataStorageServer();

            // Bind remote object to RMI registry
            Naming.rebind("rmi://localhost/Server2", server);

            System.out.println("Server2 is ready...");
        } catch (Exception e) {
            System.out.println("Server2 exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
