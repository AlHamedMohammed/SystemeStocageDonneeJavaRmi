package first.Rmiinterface ; 

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface DataStorageInterface extends Remote {

    void storeData(String key, String value) throws RemoteException;
    String retrieveData(String key) throws RemoteException;
    Map<String, String> showData() throws RemoteException;
    void deleteData(String key) throws RemoteException; 

}
