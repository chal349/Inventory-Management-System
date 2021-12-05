/**
 * @author Corey Hall
 */
package model;

/**
 * InHouse Class
 */
public class InHouse extends Part {
    int machineId;

    /**
     * InHouse Constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     *
     * @param machineId the machineID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }
}
