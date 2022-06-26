/**
 * @author Corey Hall
 */
package controller;

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
 * Main Controller class - controls logic for the Main menu of the application.
 */
public class MainFormController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partsInvLevel;
    @FXML private TableColumn<Part, Double> partsPrice;
    @FXML private TableView<Part> partsTable;
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> productID;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Integer> productsInvLevel;
    @FXML private TableColumn<Product, Double> productsPrice;
    @FXML private TextField productsSearch;
    @FXML private TextField searchParts;


    private static Part partModify;
    private static Product productModify;

    public static Part getPartModify() {
        return partModify;
    }

    public static Product getProductModify() {
        return productModify;
    }
    
   /**
    *  Initialize Main screen and loads the Part and Products Tables with sample data
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Add part button- Loads add part screen
     */
    @FXML
    void onActionMainAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Add Part");
        stage.show();
    }

    /**
     * Add product button- Loads add product screen
     */
    @FXML
    void onActionMainAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Add Product");
        stage.show();
    }

    /**
     * Delete part - removes part from Parts table 
     */
    @FXML
    void onActionMainDeletePart(ActionEvent event) {
        Part selection = partsTable.getSelectionModel().getSelectedItem();

        // If no part selected
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No part was selected.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

        // Delete part
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selection);
            }
        }
    }

    /** 
     * Delete Product - Checks that product contains no parts. Removes product from products table 
     */
    @FXML
    void onActionMainDeleteProduct(ActionEvent event) {
        Product selection = productsTable.getSelectionModel().getSelectedItem();

        // Error if no selection
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No product was selected.");
            alert.showAndWait();
        }
        // Checks to see if product contains parts. If so cannot be deleted
        else if (!selection.getAllAssociatedParts().isEmpty()) {
            Alert assocAlert = new Alert(Alert.AlertType.ERROR);
            assocAlert.setTitle("Error");
            assocAlert.setContentText("Product contains part(s). Cannot be deleted.");
            assocAlert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            // Delete selection
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(selection);
            }
        }
    }

    /**
     * Update Part - Checks if a part has been selected and then sends part information to Update Part screen
     */
    @FXML
    void onActionMainModifyPart(ActionEvent event)  throws IOException{
        partModify = partsTable.getSelectionModel().getSelectedItem();

        //Error if no part selected
        if(partModify == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No part was selected.");
            alert.showAndWait();
        }
        else {
            // Changes window to Update Part screen
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modify Part");
            stage.show();
        }
    }

    /**
     * Update Product - Checks if a product has been selected and then sends product information to Update Product screen
     */
    @FXML
    void onActionMainModifyProduct(ActionEvent event) throws IOException {

        productModify = productsTable.getSelectionModel().getSelectedItem();

        // Error if no product is selected
        if (productModify == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Error. No product was selected.");
            alert.showAndWait();
        }
        // Changes window to Modify Product screen
        else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modify Product");
            stage.show();
        }
    }

    /**
     * Search Part - searches the Part Table for parts by name or ID# then highlights found part
     */
    @FXML
    private void onActionMainSearchParts(ActionEvent actionEvent) {

        // If search field is not empty
        if (!searchParts.getText().trim().isEmpty()) {
            try {
                // search for ID
                int partId = Integer.parseInt(searchParts.getText());
                for (Part part : Inventory.getAllParts()) {
                    if (part.getId() == partId) {
                        partsTable.getSelectionModel().select(part);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                // search for Name
                String partName = searchParts.getText();
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().equals(partName)) {
                        partsTable.getSelectionModel().select(part);
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

    /**
     * Search Product - searches the Products Table for products by name or ID# - then highlights found product
     */
    @FXML
    void onActionMainSearchProducts(ActionEvent event) {

        // If search field is not empty
        if (!productsSearch.getText().trim().isEmpty()) {
            try {
                // search for ID
                int partId = Integer.parseInt(productsSearch.getText());
                for (Product product : Inventory.getAllProducts()) {
                    if (product.getId() == partId) {
                        productsTable.getSelectionModel().select(product);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                // search for name
                String partName = productsSearch.getText();
                for (Product product : Inventory.getAllProducts()) {
                    if (product.getName().toLowerCase().equals(partName)) {
                        productsTable.getSelectionModel().select(product);
                        return;
                    }
                }
            }
        }
        // Error if no product found
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setContentText("Product not found.");
        alert.showAndWait();
    }
    
   /**
    * Exit Application
    */
    @FXML void onActionExitMain() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to close Application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}
