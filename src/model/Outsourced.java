/**
 * @author Corey Hall
 */
package model;

/**
 * Outsourced Class
 */
public class Outsourced extends Part {
    String companyName;

    /**
     * Outsourced Constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /**
     * @param companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
