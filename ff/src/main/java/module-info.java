module first {
    requires java.rmi ; 
    requires javafx.controls;
    requires javafx.fxml;

    opens first to javafx.fxml;
    exports first;
    
    exports first.Rmiinterface to java.rmi;
    exports first.Server to java.rmi ; 
    exports first.Client to java.rmi ; 
    // exports first.Client.ClientGUI to javafx.graphics; 
}
