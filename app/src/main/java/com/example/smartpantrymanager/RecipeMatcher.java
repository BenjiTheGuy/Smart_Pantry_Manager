package com.example.smartpantrymanager;

// Importing libraries.
import java.util.ArrayList;
import java.util.List;

// Checking which recipes can be prepared using the current pantry stock.
public class RecipeMatcher {
    // Declaring the database helper to be used.
    private final DatabaseHelper databaseHelper;

    // Receiving the existing database helper
    public RecipeMatcher(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Returning only recipes for which every required ingredient is available.
    public List<Recipe> getMatchingRecipes() {

        List<Recipe> matchingRecipes = new ArrayList<>();

        // Retrieving all recipes and all pantry ingredients
        List<Recipe> allRecipes = databaseHelper.getAllRecipes();
        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        // Checking every recipe against the pantry.
        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredientDetails(recipe.getId());

            // A recipe must have requirements to qualify as a match.
            if (requiredIngredients.isEmpty()) {
                continue;
            }

            boolean recipeCanBePrepared = true;

            // Every required ingredient must be made available in sufficient quantity.
            for (RecipeIngredient required : requiredIngredients) {
                if (!hasEnoughIngredient(required, pantryItems)) {
                    recipeCanBePrepared = false;

                    break;
                }
            }

            // Adding the recipe only if all its requirements are satisfied.
            if (recipeCanBePrepared) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    // Checking whether the pantry contains enough of a single required ingredient.
    private boolean hasEnoughIngredient(RecipeIngredient required, List<PantryItem> pantryItems) {
        String requiredName = IngredientNormalizer.normalize(required.getIngredientName());

        String requiredUnit = required.getUnit();

        // Rejecting any invalid quantities and measurement units not found.
        if (required.getQuantity() < 0 || requiredUnit == null ||
                UnitConverter.getUnitCategory(requiredUnit).equals("unknown")) {
            return false;
        }

        double availableQuantity = 0.0;

        // Combining stock from matching pantry entries with compatible units.
        for (PantryItem pantryItem : pantryItems) {
            String pantryName = IngredientNormalizer.normalize(pantryItem.getName());

            if (!requiredName.equals(pantryName)) {
                continue;
            }

            // Expired or invalid data of pantry ingredients cannot be used.
            if (ExpiryDateHelper.isExpired(pantryItem.getExpiryDate())) {
                continue;
            }

            String pantryUnit = pantryItem.getUnit();

            // Skipping units that cannot be compared safely.
            if (pantryUnit == null || !pantryUnit.trim().equalsIgnoreCase(requiredUnit.trim())) {
                continue;
            }

            // Ignoring invalid pantry quantities
            if (pantryItem.getQuantity() < 0) {
                continue;
            }

            // Adding the available quantities in the matching units.
            availableQuantity += pantryItem.getQuantity();
        }

        double requiredQuantity = required.getQuantity();

        // Allowing for a small restriction for floating-point calculations
        return availableQuantity + 0.000001 >= requiredQuantity;
    }
}
