package first.Client ; 
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;
import first.Server.*;
import first.Rmiinterface.*;



public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Create a load balancer
            LoadBalancer loadBalancer = new LoadBalancer();

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Store Data");
                System.out.println("2. Retrieve Data");
                System.out.println("3. Show All Data");
                System.out.println("4. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline character

                switch (choice) {
                    case 1:
                        // Store data
                        System.out.print("Enter the key to store: ");
                        String storeKey = scanner.nextLine();
                        System.out.print("Enter the value to store: ");
                        String storeValue = scanner.nextLine();

                        // Get the server from the load balancer and store data
                        DataStorageInterface server = loadBalancer.getServer();
                        server.storeData(storeKey, storeValue);
                        System.out.println("Data stored successfully!");
                        break;

                    case 2:
                        // Retrieve data from all servers
                        System.out.print("Enter the key to retrieve: ");
                        String retrieveKey = scanner.nextLine();

                        retrieveDataFromAllServers(loadBalancer, retrieveKey);
                        break;

                    case 3:
                        // Show data from all servers
                        showDataFromAllServers(loadBalancer);
                        break;

                    case 4:
                        // Exit
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (RemoteException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Retrieve data from all servers
    private static void retrieveDataFromAllServers(LoadBalancer loadBalancer, String key) throws RemoteException {
        boolean found = false;

        for (String serverURL : loadBalancer.getServerURLs()) {
            try {
                DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURL);
                String value = server.retrieveData(key);
                if (value != null) {
                    System.out.println("Data found on server: " + serverURL + " -> " + key + " = " + value);
                    found = true;
                }
            } catch (Exception e) {
                System.out.println("Error contacting server: " + serverURL);
                e.printStackTrace();
            }
        }

        if (!found) {
            System.out.println("No data found for key: " + key);
        }
    }

    // Show data from all servers
    private static void showDataFromAllServers(LoadBalancer loadBalancer) throws RemoteException {
        for (String serverURL : loadBalancer.getServerURLs()) {
            try {
                DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURL);
                System.out.println("Showing data from server: " + serverURL);
                Map<String, String> data = server.showData();  // Get data from the server
                if (data.isEmpty()) {
                    System.out.println("No data stored on this server.");
                } else {
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        System.out.println(entry.getKey() + " = " + entry.getValue());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error contacting server: " + serverURL);
                e.printStackTrace();
            }
        }
    }
}