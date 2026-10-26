package com.example.smartpantrymanager;

// Class used to create Recipe objects.
public class Recipe {
    // Declaring the variables to be passed onto the constructor.
    private int id;
    private String name;
    private String instructions;

    // Constructor used to initialize a recipe object.
    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    // Getter methods used to retrieve data.
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    // Setter methods used to set variables and update them.
    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
