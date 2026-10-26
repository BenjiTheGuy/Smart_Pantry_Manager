package com.example.smartpantrymanager;

import java.util.Locale;


// Normalizes ingredient names before comparing them to the pantry and recipe ingredients.
public class IngredientNormalizer {
    // Preventing this class from being instantiated.
    private IngredientNormalizer() {

    }

    // Standardizing the ingredient name to ensure consistent formatting when making comparisons.
    public static String normalize(String ingredientName) {
        if (ingredientName == null) {
            return "";
        }

        // Removing any whitespaces and ignoring the letter casing.
        String normalized = ingredientName.trim().toLowerCase(Locale.ROOT);

        // Removing common punctuation and normalizing repeated spaces.
        normalized = normalized.replaceAll("[^a-z0-9\\s]", " ");
        normalized = normalized.replaceAll("\\s+", " ").trim();

        // Handling selected common singular and plural ingredient names.
        switch(normalized) {
            case "egg":
            case "eggs":
                return "egg";

            case "onion":
            case "onions":
                return "onion";

            case "banana":
            case "bananas":
                return "banana";

            case "tomato":
            case "tomatoes":
                return "tomato";

            case "potato":
            case "potatoes":
                return "potato";

            case "carrot":
            case "carrots":
                return "carrot";

            case "sweet potato":
            case "sweet potatoes":
                return "sweet potato";

            case "sausage":
            case "sausages":
                return "sausage";

            case "peanut":
            case "peanuts":
                return "peanut";

            default:
                return normalized;
        }
    }
}
