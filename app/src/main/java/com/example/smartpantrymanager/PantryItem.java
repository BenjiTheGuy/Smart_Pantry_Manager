package com.example.smartpantrymanager;

// Class representing a single ingredient that is stored in the pantry.
public class PantryItem {
    // Declaring the Ingredient's attributes
    private int id;
    private String name, unit, expiryDate;
    private double quantity;

    // Constructor used to create the ingredient found in the pantry.
    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Get Methods used to retrieve the attributes associated with the ingredient.
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    // Set Methods used to set the values of the attributes associated with an ingredient.
    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
