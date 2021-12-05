package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.*;

/**
 * FUTURE ENHANCEMENTS -- Include a database that collects and saves data while also tracking inventory so that parts and products used and added would be reflected in stock inventory numbers.
 * RUNTIME ERROR - located in MainForm Controller Delete Product method.
 * JAVADOCS - located in CHprojectMain/javadoc.
 *
 * @author Corey Hall
 *
 * Main Class application
 */
public class Main extends Application {

    @Override
    // Loads Main Screen
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1000, 400));
        primaryStage.show();
    }

    /**
     * Method called when program is opened / loads default parts and products for testing.
     * @param args launch
     */
    public static void main(String[] args) {

        Part taco = new InHouse(Inventory.createPartsId().getAndIncrement(),"Taco",1.99,35,20,120,1000);
        Inventory.addPart(taco);

        Part nachoChips = new InHouse(Inventory.createPartsId().getAndIncrement(),"Nacho Chips",1.99,42,15,175,1000);
        Inventory.addPart(nachoChips);

        Part hamburger = new InHouse(Inventory.createPartsId().getAndIncrement(),"Hamburger",3.99,27,15,150,1000);
        Inventory.addPart(hamburger);

        Part frenchFries = new Outsourced(Inventory.createPartsId().getAndIncrement(),"French Fries",2.45,35,15,200,"Ore-Ida");
        Inventory.addPart(frenchFries);

        Part chickenWings = new InHouse(Inventory.createPartsId().getAndIncrement(),"Chicken Wings",6.78,25,15,95,1000);
        Inventory.addPart(chickenWings);

        Part hotSauce = new Outsourced(Inventory.createPartsId().getAndIncrement(),"Hot Sauce",.79,575,100,1000,"Texas Pete");
        Inventory.addPart(hotSauce);

        Part soda = (new Outsourced(Inventory.createPartsId().getAndIncrement(),"Soda",1.79,156,5,500,"Coca-Cola"));
        Inventory.addPart(soda);

        Product tacoCombo = new Product(Inventory.createProductsId().getAndIncrement(),"Taco Combo",6.99,20,10,100);
        Inventory.addProduct(tacoCombo);
        tacoCombo.addAssociatedPart(taco);
        tacoCombo.addAssociatedPart(taco);
        tacoCombo.addAssociatedPart(nachoChips);
        tacoCombo.addAssociatedPart(soda);

        Product hamburgerMeal = new Product(Inventory.createProductsId().getAndIncrement(),"Hamburger Meal",7.49,50,15,75);
        Inventory.addProduct(hamburgerMeal);
        hamburgerMeal.addAssociatedPart(hamburger);
        hamburgerMeal.addAssociatedPart(frenchFries);
        hamburgerMeal.addAssociatedPart(soda);

        Product wingCombo = new Product(Inventory.createProductsId().getAndIncrement(),"Chicken Wing Combo",8.99,20,15,60);
        Inventory.addProduct(wingCombo);
        wingCombo.addAssociatedPart(chickenWings);
        wingCombo.addAssociatedPart(hotSauce);
        wingCombo.addAssociatedPart(soda);


        launch(args);
    }
}
