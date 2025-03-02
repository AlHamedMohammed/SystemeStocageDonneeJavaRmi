package first.Server;
// the implementation of the DataStorageInterface
import first.Rmiinterface.* ;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class DataStorageServer extends UnicastRemoteObject implements DataStorageInterface {

    // The data store
    private Map<String, String> dataStore = new HashMap<>();

    public DataStorageServer() throws RemoteException {
        super();
    }

    @Override
    public void storeData(String key, String value) throws RemoteException {
        // Store the data in the map
        dataStore.put(key, value);
        System.out.println("Stored data: " + key + " = " + value);
    }

    @Override
    public String retrieveData(String key) throws RemoteException {
        // Retrieve data from the map
        return dataStore.get(key);
    }

    @Override
    public Map<String, String>  showData() throws RemoteException {
        return dataStore;

    }

    @Override 
    public void updateData(String key ) throws RemoteException{
        // dataStore.set(key)
        System.out.println("update funtion ");

    }
}


// public class DataStorageServer extends UnicastRemoteObject implements DataStorageInterface {

//     private Map<String, String> dataStore;

//     public DataStorageServer() throws RemoteException {
//         super();
//         dataStore = new HashMap<>();
//     }

//     @Override
//     public void storeData(String key, String value) throws RemoteException {
//         dataStore.put(key, value);
//         System.out.println("Data stored: " + key + " = " + value);
//     }

//     @Override
//     public String retrieveData(String key) throws RemoteException {
//         String value = dataStore.get(key);
//         System.out.println("Data retrieved: " + key + " = " + value);
//         return value;
//     }

//     @Override
//     public void showData() throws RemoteException {
//         if (dataStore.isEmpty()) {
//             System.out.println("No data stored on this server.");
//         } else {
//             System.out.println("Stored data:");
//             for (Map.Entry<String, String> entry : dataStore.entrySet()) {
//                 System.out.println(entry.getKey() + " = " + entry.getValue());
//             }
            
//         }
//     }
// }
