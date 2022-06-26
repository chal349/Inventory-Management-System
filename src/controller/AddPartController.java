/**
 * @author Corey Hall
 */
package controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddPart Controller class - controls logic for the Add Part page of application.
 */
public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;
    
    @FXML private Label MachineCompany;

    @FXML private TextField addPartID;

    @FXML private RadioButton addPartInHouseButton;

    @FXML private TextField addPartInv;

    @FXML private TextField addPartMachine;

    @FXML private TextField addPartMax;

    @FXML private TextField addPartMin;

    @FXML private TextField addPartName;

    @FXML private RadioButton addPartOutsourcedButton;

    @FXML private TextField addPartPrice;
    
   /**
    * This method initializes the Add Part page - it sets the InHouse radio button to selected and auto increments the parts ID field.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // radio button initially set to InHouse
        addPartInHouseButton.setSelected(true);
        // auto generates part ID
        addPartID.setText(Inventory.createPartsId().toString());
    }

    /**
     * This method cancels any data entered to Add Part screen and returns to Main page.
     */
    @FXML
    void onActionAddPartCancel(ActionEvent event) throws IOException {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be lost, do you wish to continue?");
        warning.setTitle("Warning");
        Optional <ButtonType> result = warning.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }

    /**
     * This method is a radio button that changes label text from Outsourced "Company" to InHouse "Machine ID" when clicked.
     */
    @FXML void onActionAddPartInHouse(ActionEvent event) {
        MachineCompany.setText("Machine ID");
        addPartMachine.setPromptText("Machine #");
    }

    /**
     * This method is a radio button that changes label text from InHouse "Machine ID" to Outsourced "Company".
     */
    @FXML void onActionAddPartOutsourced(ActionEvent event) {
        MachineCompany.setText("Company");
        addPartMachine.setPromptText("Company Name");
    }

    /**
     * This method checks for empty or invalid fields before saving Part and then returns to the Main page.
     */
    @FXML void onActionAddPartSave(ActionEvent event) throws IOException{
        try {
            int id = Integer.parseInt(addPartID.getText());
            String name = addPartName.getText();
            int inv = Integer.parseInt(addPartInv.getText());
            double price = Double.parseDouble(addPartPrice.getText());
            int min = Integer.parseInt(addPartMin.getText());
            int max = Integer.parseInt(addPartMax.getText());

            // Checks if name field is left empty
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The name field must completed");
                alert.showAndWait();
            } 
            
            // Checks if Inventory is less than minimum OR Inventory is greater than Maximum
            else if (inv < min || inv > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The Inventory value must be within the minimum and maximum range");
                alert.showAndWait();
            }
            
            // Checks if Minimum is greater than Maximum
            else if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("The minimum # must be less than the maximum");
                alert.showAndWait();
            }
            
            // If InHouse radio button is selected, save info to part. Else Outsourced radio button is selected.
            else {
                if (addPartInHouseButton.isSelected()) {
                    int machineID = Integer.parseInt(addPartMachine.getText());
                    Inventory.addPart(new InHouse(id, name, price, inv, min, max, machineID));
                    Inventory.createPartsId().getAndIncrement();
                }
                else if (addPartOutsourcedButton.isSelected()) {
                    String companyName = addPartMachine.getText();
                    Inventory.addPart(new Outsourced(id, name, price, inv, min, max, companyName));
                    Inventory.createPartsId().getAndIncrement();
                }
                
                // Back to Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Inventory Management System");
                stage.show();
            }
        }
        // Error if text fields are empty or not valid
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please input valid data for all text fields");
            alert.showAndWait();
        }
    }

}
