/**
 *@author Corey Hall
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProduct Controller class - controls the logic for the Add Product page of application.
 */
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField addProductID;
    @FXML private TextField addProductInv;
    @FXML private TextField addProductMax;
    @FXML private TextField addProductMin;
    @FXML private TextField addProductName;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductSearch;
    
    @FXML private TableView<Part> addProductTable;
    @FXML private TableColumn<Part, Integer> addProductTableID;
    @FXML private TableColumn<Part, Integer> addProductTableInvLevel;
    @FXML private TableColumn<Part, String> addProductTablePartName;
    @FXML private TableColumn<Part, Double> addProductTablePrice;
    @FXML private TableView<Part> deleteProductTable;
    @FXML private TableColumn<Part, Integer> deleteProductTableInvLevel;
    @FXML private TableColumn<Part, Integer> deleteProductTablePartID;
    @FXML private TableColumn<Part, String> deleteProductTablePartName;
    @FXML private TableColumn<Part, Double> deleteProductTablePrice;

    /**
     * Array for adding parts to product.
     */
    private static ObservableList <Part> addToProduct = FXCollections.observableArrayList();

    /**
     * Initialize the AddProduct page - auto increments the Parts ID field - sets both tables to default values and parts.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addProductID.setText(Inventory.createProductsId().toString());
        addProductTable.setItems(Inventory.getAllParts());
        addProductTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductTableInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        deleteProductTable.setItems(addToProduct);
        deleteProductTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        deleteProductTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteProductTableInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        deleteProductTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    /**
     * Add part to Product - moves selected part from parts table to products table and produces an alert if no part is selected.
     */
    @FXML
    void onActionAddProductAdd(ActionEvent event) {
        Part selectedPart = addProductTable.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No part was selected.");
            alert.showAndWait();
        }
        else {
            addToProduct.add(selectedPart);
            deleteProductTable.setItems(addToProduct);
        }
    }

    /**
     * Cancel Product - cancels any data entered to Add product screen and tables - returns to main page.
     */
    @FXML
    void onActionAddProductCancel(ActionEvent event) throws IOException {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be lost, do you wish to continue?");
        warning.setTitle("Warning");
        Optional<ButtonType> result = warning.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            addToProduct.clear();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }

    /**
     * Delete Product - removes selected part from Product Table.
     */
    @FXML
    void onActionAddProductDelete(ActionEvent event) {
        Part selectedPart = deleteProductTable.getSelectionModel().getSelectedItem();
        
        // If no part selected
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No part was selected.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            // Part is deleted
            if (result.isPresent() && result.get() == ButtonType.OK) {
                addToProduct.remove(selectedPart);
                deleteProductTable.setItems(addToProduct);
            }
        }
    }

    /**
     * Save Product - checks for empty or invalid fields before saving product and returning to the Main page.
     */
    @FXML
    void onActionAddProductSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(addProductID.getText());
            String name = addProductName.getText();
            int inv = Integer.parseInt(addProductInv.getText());
            double price = Double.parseDouble(addProductPrice.getText());
            int min = Integer.parseInt(addProductMin.getText());
            int max = Integer.parseInt(addProductMax.getText());

            // Checks if name field is left blank
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The name field must completed");
                alert.showAndWait();

            // Checks if inventory is less than minimum OR inventory is greater than maximum
            } else if (inv < min || inv > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The Inventory value must be within the minimum and maximum range");
                alert.showAndWait();

            // Checks if minimum is greater than maximum
            } else if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The minimum # must be less than the maximum");
                alert.showAndWait();
            }
            
            // Save new Product
            else {
                Product newProduct = new Product(id, name, price, inv, min, max);
                for (Part part : addToProduct) {
                    newProduct.addAssociatedPart(part);
                }
                
            // auto generate new Product ID
            Inventory.createProductsId().getAndIncrement();
            addToProduct.clear();
            Inventory.addProduct(newProduct);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
            
            // Error if text fields are blank or invalid
        } catch (NumberFormatException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please input valid data for all text fields");
            alert.showAndWait();
        }

    }

    /**
     * Search Parts - searches the Part Table for parts by name or ID# - then highlights found part.
     */
    @FXML
    void onActionAddProductSearch(ActionEvent event) {

        // Checks if search field is not empty
        if (!addProductSearch.getText().trim().isEmpty()) {
            try {
                // search for ID
                int partId = Integer.parseInt(addProductSearch.getText());
                for (Part part : Inventory.getAllParts()) {
                    if (part.getId() == partId) {
                        addProductTable.getSelectionModel().select(part);
                        return;
                    }
                }
                // search for name
            } catch (NumberFormatException e) {
                String partName = addProductSearch.getText();
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().equals(partName)) {
                        addProductTable.getSelectionModel().select(part);
                        return;
                    }
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setContentText("Part not found.");
        alert.showAndWait();
    }

}
