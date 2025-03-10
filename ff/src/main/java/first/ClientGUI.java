


package first;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import first.Server.LoadBalancer;
import first.Rmiinterface.DataStorageInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientGUI extends Application {

    private LoadBalancer loadBalancer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        try {
            loadBalancer = new LoadBalancer();

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20));
            grid.setVgap(10);
            grid.setHgap(10);

            Label keyLabel = new Label("Key:");
            TextField keyField = new TextField();
            Label valueLabel = new Label("Value:");
            TextField valueField = new TextField();

            Button storeButton = new Button("Store Data");
            Button retrieveButton = new Button("Retrieve Data");
            Button showButton = new Button("Show All Data");
            Button clearButton = new Button("Clear Window");
            TextArea outputArea = new TextArea();
            outputArea.setEditable(false);

            grid.add(keyLabel, 0, 0);
            grid.add(keyField, 1, 0);
            grid.add(valueLabel, 0, 1);
            grid.add(valueField, 1, 1);
            grid.add(storeButton, 0, 2);
            grid.add(retrieveButton, 1, 2);
            grid.add(showButton, 2, 2);
            grid.add(clearButton, 3, 2);
            grid.add(outputArea, 0, 3, 4, 1);
          
          
            storeButton.setOnAction(e -> {
                String key = keyField.getText();
                String value = valueField.getText();
                if (!key.isEmpty() && !value.isEmpty()) {
                    try {
                        DataStorageInterface server = loadBalancer.getServer();
                        server.storeData(key, value);
                        outputArea.appendText("Data stored: " + key + " = " + value + "\n");
                        keyField.clear();
                        valueField.clear();
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
                    boolean found = false; // Track if the key exists in any server
            
                    try {
                        for (String serverURL : loadBalancer.getServerURLs()) {
                            DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURL);
                            String value = server.retrieveData(key);
                            if (value != null) {
                                outputArea.appendText("Retrieved and deleted data: " + key + " = " + value + "\n");
                                found = true;
                                break; // Stop searching once found
                            }
                        }
            
                        if (!found) {
                            outputArea.appendText("No data found for key: " + key + "\n");
                        }
                    } catch (Exception ex) {
                        outputArea.appendText("Error retrieving data: " + ex.getMessage() + "\n");
                    }
                } else {
                    outputArea.appendText("Key cannot be empty!\n");
                }
            });
            
            showButton.setOnAction(e -> showAllData());
            clearButton.setOnAction(e -> outputArea.clear());

            Scene scene = new Scene(grid, 600, 400);
            primaryStage.setTitle("Distributed Data Storage Client");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAllData() {
        Stage tableStage = new Stage();
        tableStage.setTitle("All Stored Data");

        TableView<Map.Entry<String, String>> tableView = new TableView<>();
        ObservableList<Map.Entry<String, String>> dataList = FXCollections.observableArrayList();
        Map<String, Map<String, String>> serverData = new LinkedHashMap<>();

        try {
            for (String serverURL : loadBalancer.getServerURLs()) {
                DataStorageInterface server = (DataStorageInterface) Naming.lookup(serverURL);
                serverData.put(serverURL, server.showData());
            }

            TableColumn<Map.Entry<String, String>, String> keyColumn = new TableColumn<>("Key");
            keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
            tableView.getColumns().add(keyColumn);

            for (Map.Entry<String, Map<String, String>> serverEntry : serverData.entrySet()) {
                TableColumn<Map.Entry<String, String>, String> serverColumn = new TableColumn<>(serverEntry.getKey());
                serverColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                        serverEntry.getValue().getOrDefault(cellData.getValue().getKey(), "-")));
                tableView.getColumns().add(serverColumn);
            }

            for (Map<String, String> data : serverData.values()) {
                dataList.addAll(data.entrySet());
            }

            tableView.setItems(dataList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox, 800, 400);
        tableStage.setScene(scene);
        tableStage.show();
    }
}
