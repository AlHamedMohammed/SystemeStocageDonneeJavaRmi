package first.Client;

// public package frist.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import first.Server.LoadBalancer;
import first.Rmiinterface.DataStorageInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Map;

public class ClientGUI extends Application {

    private LoadBalancer loadBalancer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        try {
            // Initialize the load balancer
            loadBalancer = new LoadBalancer();

            // Create the main layout
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20));
            grid.setVgap(10);
            grid.setHgap(10);

            // Add components
            Label keyLabel = new Label("Key:");
            TextField keyField = new TextField();
            Label valueLabel = new Label("Value:");
            TextField valueField = new TextField();

            Button storeButton = new Button("Store Data");
            Button retrieveButton = new Button("Retrieve Data");
            Button showButton = new Button("Show All Data");
            TextArea outputArea = new TextArea();
            outputArea.setEditable(false);

            // Add components to the grid
            grid.add(keyLabel, 0, 0);
            grid.add(keyField, 1, 0);
            grid.add(valueLabel, 0, 1);
            grid.add(valueField, 1, 1);
            grid.add(storeButton, 0, 2);
            grid.add(retrieveButton, 1, 2);
            grid.add(showButton, 2, 2);
            grid.add(outputArea, 0, 3, 3, 1);

            // Set up button actions
            storeButton.setOnAction(e -> {
                String key = keyField.getText();
                String value = valueField.getText();
                if (!key.isEmpty() && !value.isEmpty()) {
                    try {
                        DataStorageInterface server = loadBalancer.getServer();
                        server.storeData(key, value);
                        outputArea.appendText("Data stored: " + key + " = " + value + "\n");
                    } catch (RemoteException ex) {
                        outputArea.appendText("Error storing data: " + ex.getMessage() + "\n");
                    }
                } else {
                    outputArea.appendText("Key and value cannot be empty!\n");
                }
            });

            retrieveButton.setOnAction(e -> {
                String key = keyField.getText();
                if (!key.isEmpty()) {
                    try {
                        DataStorageInterface server = loadBalancer.getServer();
                        String value = server.retrieveData(key);
                        if (value != null) {
                            outputArea.appendText("Retrieved data: " + key + " = " + value + "\n");
                        } else {
                            outputArea.appendText("No data found for key: " + key + "\n");
                        }
                    } catch (RemoteException ex) {
                        outputArea.appendText("Error retrieving data: " + ex.getMessage() + "\n");
                    }
                } else {
                    outputArea.appendText("Key cannot be empty!\n");
                }
            });

            showButton.setOnAction(e -> {
                try {
                    outputArea.appendText("Showing all data:\n");
                    for (String serverURL : loadBalancer.getServerURLs()) {
                        DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURL);
                        outputArea.appendText("Data from server: " + serverURL + "\n");
                        Map<String, String> data = server.showData();
                        if (data.isEmpty()) {
                            outputArea.appendText("No data stored on this server.\n");
                        } else {
                            for (Map.Entry<String, String> entry : data.entrySet()) {
                                outputArea.appendText(entry.getKey() + " = " + entry.getValue() + "\n");
                            }
                        }
                    }
                } catch (Exception ex) {
                    outputArea.appendText("Error showing data: " + ex.getMessage() + "\n");
                }
            });

            // Set up the scene and stage
            Scene scene = new Scene(grid, 500, 400);
            primaryStage.setTitle("Distributed Data Storage Client");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
