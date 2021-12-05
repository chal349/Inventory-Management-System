/**
 * @author Corey Hall
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Inventory Class
 */
public class Inventory {
    // arrays that hold all parts and products
    private static final ObservableList <Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList <Product> allProducts = FXCollections.observableArrayList();
    // auto generates part and product IDs
    private static final AtomicInteger partsId = new AtomicInteger(249);
    private static final AtomicInteger productsId = new AtomicInteger(2249);

    /**
     * @return the partsId
     */
    public static AtomicInteger createPartsId() {
        return partsId;
    }

    /**
     * @return the productsId
     */
    public static AtomicInteger createProductsId() { return productsId; }

    /**
     * @param newPart the part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId NOT USED
     * @return NOT USED
     */
    public static Part lookupPart(int partId) {
        Part numSearchPart = null;
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                numSearchPart = part;
            }
        }
        return numSearchPart;
    }

    /**
     * @param partName NOT USED
     * @return NOT USED
     */
    public static ObservableList <Part> lookupPart (String partName) {
        ObservableList <Part> stringSearchPart = FXCollections.observableArrayList();

        for (Part parts : allParts) {
            if (parts.getName().toLowerCase().contains(partName.toLowerCase())) {
                stringSearchPart.add(parts);
            }
        }
        return stringSearchPart;
    }

    /**
     * @param productId NOT USED
     * @return NOT USED
     */
    public static Product lookupProduct(int productId) {
        Product numSearchProduct = null;
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                numSearchProduct = product;
            }
        }
        return numSearchProduct;
    }

    /**
     * @param productName NOT USED
     * @return NOT USED
     */
    public static ObservableList <Product> lookupProduct(String productName) {
        ObservableList <Product> stringSearchProduct = FXCollections.observableArrayList();
        for (Product products : allProducts) {
            if (products.getName().toLowerCase().contains(productName.toLowerCase())) {
                stringSearchProduct.add(products);
            }
        }
        return stringSearchProduct;
    }

    /**
     * @param index NOT USED
     * @param partSelected NOT USED
     */
    public static void updatePart(int index, Part partSelected) {
        allParts.set(index, partSelected);
    }

    /**
     * @param index NOT USED
     * @param productSelected NOT USED
     */
    public static void updateProduct(int index, Product productSelected) {
        allProducts.set(index, productSelected);
    }

    /**
     * @param selectedPart to delete
     * @return true and remove if part is found, false if no part found
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param selectedProduct to delete
     * @return true and remove if product is found, false if no product found
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Array of all parts
     * @return all parts
     */
    public static ObservableList <Part> getAllParts() {
        return allParts;
    }

    /**
     * array of all products
     * @return all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
