package first.Rmiinterface ; 

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface DataStorageInterface extends Remote {
    // Method to store data on the server
    public void storeData(String key, String value) throws RemoteException;

    // Method to retrieve data from the server
    public String retrieveData(String key) throws RemoteException;

    public Map<String,String> showData() throws RemoteException ; 
    public void updateData(String key ) throws RemoteException ; 
}

