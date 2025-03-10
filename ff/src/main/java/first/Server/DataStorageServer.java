package first.Server ; 
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import first.Rmiinterface.DataStorageInterface;

public class DataStorageServer extends UnicastRemoteObject implements DataStorageInterface {
    private final Map<String, String> dataStorage;

    public DataStorageServer() throws RemoteException {
        dataStorage = new HashMap<>();
    }

    @Override
    public void storeData(String key, String value) throws RemoteException {
        dataStorage.put(key, value);
        System.out.println("Stored data: " + key + " = " + value);
    }

    @Override
    public String retrieveData(String key) throws RemoteException {
        System.out.println("delete data: " + key );

        return dataStorage.remove(key); // Now it actually deletes the data
    }

    @Override
    public Map<String, String> showData() throws RemoteException {
        return new HashMap<>(dataStorage);
    }

    @Override
    public void deleteData(String key) throws RemoteException {
        System.out.println("delete data: " + key );

        dataStorage.remove(key); // Explicit delete method if needed
    }


}

