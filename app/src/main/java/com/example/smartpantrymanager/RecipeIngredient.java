package com.example.smartpantrymanager;

// Class used to create Recipe Ingredients objects. A single object represents an ingredient
// required by a particular recipe.
public class RecipeIngredient {
    // Declaring the variables to be passed onto the constructor.
    private int id;
    private int recipeId;
    private String ingredientName;
    private double quantity;
    private String unit;

    // Constructor used to initialize a recipe object.
    public RecipeIngredient(
            int id,
            int recipeId,
            String ingredientName,
            double quantity,
            String unit
    ) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getter methods used to retrieve data.
    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
