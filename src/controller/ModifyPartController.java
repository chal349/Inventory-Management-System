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
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyPart Controller Class - controls logic for Modify Part page of application.
 */
public class ModifyPartController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private Label machineAndCompany;
    @FXML private TextField modifyPartID;
    @FXML private RadioButton modifyPartInHouseButton;
    @FXML private TextField modifyPartInv;
    @FXML private TextField modifyPartMachine;
    @FXML private TextField modifyPartMax;
    @FXML private TextField modifyPartMin;
    @FXML private TextField modifyPartName;
    @FXML private RadioButton modifyPartOutsourcedButton;
    @FXML private TextField modifyPartPrice;

    private Part partSelected;

   /**
    * Initializes the Modify Part page - sets both tables to default values and parts.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Loads the part selected from the main screen to the modify part screen. Checks if its an InHouse or Outsourced part and selects corresponding radio button
        partSelected = MainFormController.getPartModify();
        if (partSelected instanceof InHouse) {
            modifyPartInHouseButton.setSelected(true);
            modifyPartMachine.setText(String.valueOf(((InHouse) partSelected).getMachineId()));
        }
        if (partSelected instanceof Outsourced) {
            modifyPartOutsourcedButton.setSelected(true);
            modifyPartMachine.setText(((Outsourced) partSelected).getCompanyName());
        }

        modifyPartID.setText(String.valueOf(partSelected.getId()));
        modifyPartName.setText(partSelected.getName());
        modifyPartInv.setText(String.valueOf(partSelected.getStock()));
        modifyPartPrice.setText(String.valueOf(partSelected.getPrice()));
        modifyPartMax.setText(String.valueOf(partSelected.getMax()));
        modifyPartMin.setText(String.valueOf(partSelected.getMin()));
    }
    
    /**
     * Cancel Part - cancels any data entered to Modify Part screen and tables, returns to main menu screen.
     */
    @FXML
    void onActionModifyPartCancel(ActionEvent event) throws IOException {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION, "All changes will be lost, do you wish to continue?");
        warning.setTitle("Warning");
        Optional<ButtonType> result = warning.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }

    /**
     * Inhouse Radio Button - changes label text from Outsourced "Company" to Inhouse "Machine ID" when clicked.
     */
    @FXML
    void onActionModifyPartInhouse(ActionEvent event) {
        machineAndCompany.setText("Machine ID");
        modifyPartMachine.setPromptText("Machine #");
    }

    /**
     * Outsourced Radio Button - changes label text from Inhouse "Machine ID" to Outsourced "Company" when clicked.
     */
    @FXML
    void onActionModifyPartOutsourced(ActionEvent event) {
        machineAndCompany.setText("Company");
        modifyPartMachine.setPromptText("Company Name");
    }

    /**
     * Save Updated Part - checks for empty or invalid fields before saving part and returning to the Main page.
     */
    @FXML
    void onActionModifyPartSave(ActionEvent event)throws IOException{
        try {
            int id = Integer.parseInt(modifyPartID.getText());
            String name = modifyPartName.getText();
            int inv = Integer.parseInt(modifyPartInv.getText());
            double price = Double.parseDouble(modifyPartPrice.getText());
            int min = Integer.parseInt(modifyPartMin.getText());
            int max = Integer.parseInt(modifyPartMax.getText());

            // Checks if name field is empty
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
            else {
                // If InHouse radio button is selected save to Part info, Else Outsourced radio button is selected
                if (modifyPartInHouseButton.isSelected()) {
                    int machineID = Integer.parseInt(modifyPartMachine.getText());
                    Inventory.addPart(new InHouse(id, name, price, inv, min, max, machineID));
                    Inventory.deletePart(partSelected);
                }
                else if (modifyPartOutsourcedButton.isSelected()) {
                    String companyName = modifyPartMachine.getText();
                    Inventory.addPart(new Outsourced(id, name, price, inv, min, max, companyName));
                    Inventory.deletePart(partSelected);
                }
                // Open Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Inventory Management System");
                stage.show();
            }
        }
        //Error if any blank or invalid fields
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please input valid data for all text fields");
            alert.showAndWait();
        }
    }

}
