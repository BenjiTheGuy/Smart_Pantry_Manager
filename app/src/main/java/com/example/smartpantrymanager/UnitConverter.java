package com.example.smartpantrymanager;

import java.util.Locale;

// Converting compatible measurement units into a common unit so pantry quantities can be compared
// with recipe requirements.
public class UnitConverter {
    // Preventing this class from being instantiated.
    private UnitConverter() {

    }

    // Standardizing unit names before performing conversions
    public static String normalizeUnit(String unit) {
        if (unit == null) {
            return " ";
        }

        String normalized = unit.trim().toLowerCase(Locale.ROOT);

        switch (normalized) {
            case "kilogram":
            case "kilograms":
                return "kg";

            case "gram":
            case "grams":
                return "g";

            case "egg":
            case "eggs":
                return "egg";

            // Standardizing liquid volume units
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "l";

            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
                return "ml";

            // Standardizing cooking measurements
            case "cup":
                return "cups";

            case "tablespoon":
            case "tablespoons":
                return "tbsp";

            case "teaspoon":
            case "teaspoons":
                return "tsp";

            // Treating singular and plural piece units equally.
            case "piece":
            case "pieces":
                return "pieces";

            default:
                return normalized;
        }
    }

    // Identifying the measurement category of a unit.
    public static String getUnitCategory(String unit) {
        String normalized = normalizeUnit(unit);

        switch (normalized) {
            case "kg":
            case "g":
                return "mass";

            case "l":
            case "ml":
                return "volume_liquids";

            case "cups":
            case "tbsp":
            case "tsp":
                return "volume_cooking";

            case "pieces":
                return "count";

            default:
                return "unknown";
        }
    }

    // Converting a quantity into the standard unit for its category. The Mass uses grams, liquid
    // volume uses millilitres, cooking volume uses teaspoons, and counts uses pieces.
    public static double convertToStandardUnit(double quantity, String unit) {
        String normalized = normalizeUnit(unit);

        switch (normalized) {
            // Mass Conversion
            case "kg":
                return quantity * 1000.0;

            case "g":
                return quantity;

            // Liquid Volume Conversions
            case "l":
                return quantity * 1000.0;

            case "ml":
                return quantity;

            // Cooking Volume Conversions
            // Uses 1 cup to represents 16 tablespoons and 1 tablespoon representing 3 teaspoons.
            case "cups":
                return quantity * 240.0;

            case "tbsp":
                return quantity * 15.0;

            case "tsp":
                return quantity * 5.0;

            // Count Conversions
            case "pieces":
                return quantity;

            default:
                throw new IllegalArgumentException("Unsupported Measurement Unit: " + unit);
        }
    }

    // Checking whether two units can be compared directly after conversion to their standard units.
    public static boolean areCompatible(String firstUnit, String secondUnit) {
        String firstCategory = getUnitCategory(firstUnit);
        String secondCategory = getUnitCategory(secondUnit);

        return !firstCategory.equals("unknown") && firstCategory.equals(secondCategory);
    }
}
