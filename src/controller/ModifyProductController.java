/**
 * @author Corey Hall
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
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyProduct Controller class - controls logic for Modify Product page of the application.
 */
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TableView<Part> modifyProductAddTable;
    @FXML private TableColumn<Part, Integer> modifyProductAddTableID;
    @FXML private TableColumn<Part, Integer> modifyProductAddTableInv;
    @FXML private TableColumn<Part, String> modifyProductAddTablePartName;
    @FXML private TableColumn<Part, Double> modifyProductAddTablePrice;
    @FXML private TableView<Part> modifyProductDeleteTable;
    @FXML private TableColumn<Part, Integer> modifyProductDeleteTableID;
    @FXML private TableColumn<Part, Integer> modifyProductDeleteTableInv;
    @FXML private TableColumn<Part, String> modifyProductDeleteTablePartName;
    @FXML private TableColumn<Part, Double> modifyProductDeleteTablePrice;
    @FXML private TextField modifyProductID;
    @FXML private TextField modifyProductInv;
    @FXML private TextField modifyProductMax;
    @FXML private TextField modifyProductMin;
    @FXML private TextField modifyProductName;
    @FXML private TextField modifyProductPrice;
    @FXML private TextField modifyProductSearch;

    Product selectedProduct;

    /**
     * Array of associatedParts
     */
    private ObservableList <Part> associatedParts = FXCollections.observableArrayList();
    
   /**
    * Initialize the ModifyProduct page - brings selected product data from Main Screen - sets both tables to default values and parts.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedProduct = MainFormController.getProductModify();
        
        // temporarily adds a selected part to the Associated parts table/array. Removes part if product is cancelled/not saved.
        associatedParts.setAll(selectedProduct.getAllAssociatedParts());

        modifyProductID.setText(String.valueOf(selectedProduct.getId()));
        modifyProductName.setText(selectedProduct.getName());
        modifyProductInv.setText(String.valueOf(selectedProduct.getStock()));
        modifyProductPrice.setText(String.valueOf(selectedProduct.getPrice()));
        modifyProductMax.setText(String.valueOf(selectedProduct.getMax()));
        modifyProductMin.setText(String.valueOf(selectedProduct.getMin()));

        modifyProductAddTable.setItems(Inventory.getAllParts());
        modifyProductAddTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAddTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAddTableInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductAddTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductDeleteTable.setItems(associatedParts);
        modifyProductDeleteTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductDeleteTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductDeleteTableInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductDeleteTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Add Part to Product - moves selected part from parts table to products table and produces an alert if no part is selected.
     */
    @FXML
    void onActionModifyProductAdd(ActionEvent event) {
        Part selectedPart = modifyProductAddTable.getSelectionModel().getSelectedItem();

        // Error if no selection
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No part was selected.");
            alert.showAndWait();
        } else {
           associatedParts.add(selectedPart);
        }
    }

    /**
     * Cancel Product - cancels any data entered to Modify Product screen and tables - returns to main page.
     */
    @FXML
    void onActionModifyProductCancel(ActionEvent event) throws IOException {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be lost, do you wish to continue?");
        warning.setTitle("Warning");
        Optional<ButtonType> result = warning.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Back to main form if user clicks OK
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }

    /**
     * Delete Part from Product - removes selected part from Product Table.
     */
    @FXML
    void onActionModifyProductDelete(ActionEvent event) {

        Part selectedPart = modifyProductDeleteTable.getSelectionModel().getSelectedItem();
        // Error if no part selected
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

            // If user clicks OK, part is deleted from associated parts and table
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                modifyProductDeleteTable.setItems(associatedParts);
            }
        }
    }

    /**
     * Save Product - checks for empty or invalid fields before saving part and returning to the Main page.
     */
    @FXML
    void onActionModifyProductSave(ActionEvent event)throws IOException {
        try {
            int id = Integer.parseInt(modifyProductID.getText());
            String name = modifyProductName.getText();
            int inv = Integer.parseInt(modifyProductInv.getText());
            double price = Double.parseDouble(modifyProductPrice.getText());
            int min = Integer.parseInt(modifyProductMin.getText());
            int max = Integer.parseInt(modifyProductMax.getText());

            // Checks if name field is blank
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The name field must completed");
                alert.showAndWait();
                
            // Checks if Inventory is less than Minimum OR Inventory is greater than maximum
            } else if (inv < min || inv > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The Inventory value must be within the minimum and maximum range");
                alert.showAndWait();
                
            // Checks if Minimum is greater than Maximum
            } else if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The minimum # must be less than the maximum");
                alert.showAndWait();
            }
            else {
                Product newProduct = new Product(id, name, price, inv, min, max);
                for (Part part : associatedParts) {
                    newProduct.addAssociatedPart(part);
                }
                // adds modified product and deletes old product
                Inventory.addProduct(newProduct);
                Inventory.deleteProduct(selectedProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Inventory Management System");
                stage.show();
            }
            
         // Error if blank or invalid text fields
        } catch (NumberFormatException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please input valid data for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * Search Parts Table - searches for parts by name or ID# - then highlights found part.
     */
    @FXML
    void onActionModifyProductSearch(ActionEvent event) {

        // If search field is not empty
        if (!modifyProductSearch.getText().trim().isEmpty()) {
            try {
                // search for ID
                int partId = Integer.parseInt(modifyProductSearch.getText());
                for (Part part : Inventory.getAllParts()) {
                    if (part.getId() == partId) {
                        modifyProductAddTable.getSelectionModel().select(part);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                // search for Name
                String partName = modifyProductSearch.getText();
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().equals(partName)) {
                        modifyProductAddTable.getSelectionModel().select(part);
                        return;
                    }
                }
            }
        }
        // Error if no part found
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setContentText("Part not found.");
        alert.showAndWait();
    }

}
