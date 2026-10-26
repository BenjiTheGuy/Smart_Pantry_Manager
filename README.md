# Smart Pantry Manager

## Overview
Smart Pantry Manager is a Java-Based Android Application designed to help reduce food waste by allowing for users to keep track of the ingredients they currently have available.

The application heavily involves suggesting recipes based strictly on the ingredients made available within the user's pantry. A recipe is only suggested to the user if they have sufficient ingredients in the required quantities needed to make the recipe.

This ensures that the user doesn't have to buy additional ingredients and will only make use of what ingredients are available.

## Problem
The main objectives of the application are to address the following:
- Allow for users to add, edit, and delete pantry ingredients. (Essentially covering CRUD Operations)
- Store the relative information for an ingredient, such as its name, quantity, units, and expiry dates.
- Displaying the user's current pantry items.
- Storing a collection of recipes within the application database.
- Suggesting recipes based strictly on the ingredients currently made available.
- Preventing recipes from being suggested when even one required ingredient is missing.
- Allowing for users to view completed recipe ingredients and preparation instructions.
- Providing a settings/profile screen for additional application preference.

## Implemented Features

### Pantry Management

Users can:
- Add ingredients to their pantry.
- Edit existing ingredients.
- Delete ingredients with a confirmation dialog.
- View all current pantry items using a RecyclerView with a custom Adapter.
- Record ingredient names, quantities and measurement units and mandatory expiry dates.
- Store pantry information persistently using SQLite.
- Validate ingredient inputs before saving changes.
- Identify expired and soon to expire ingredients through color indicators.

The pantry supports the core CRUD Operations through the application's database layer.

### Recipe Matching & Suggestions

The application allows for users to compare their pantry against the recipe database.

The Recipes Screen provides two viewing options:
- **All Recipes:** Displays the recipes available in the application.
- **Suggested Recipes:** Displays recipes that can be prepared using the available, non-expired pantry ingredients.

A recipe will only be suggested to the user if the following conditions are met:

1. Every required ingredient is made available in the pantry.
2. The available quantity is sufficient.
3. Ingredient names can be matched despite simple differences in wording through the IngredientNormalizer class which handles singular and plural forms of ingredients.
4. Pantry and Recipe units must match, whilst also ignoring whitespaces and capitalization.
5. Expired pantry stock is excluded from the available quantity.
6. Quantities from multiple pantry entries which match in ingredient names and quantities are combined and are not expired.
7. Every required ingredient must pass the checks before the recipe can be suggested.

Recipes that are missing required ingredients are immediately not suggested to the user and feedback is provided.

### Expiry Management

This section helps users monitor the freshness of their pantry ingredients and prevents expired stock from being used for recipe suggestions.

Each pantry ingredient requires an expiry date.

The application utilizes the following rules:

- `Expired`: Expiry Date is before the current date and the Ingredient name is flagged as a red color and excluded from the suggested recipes.
- `Expiring Soon`: Expiry Date is on the current date or after 3 days and the Ingredient name is flagged as a yellow color and still remains include with the suggested recipes.
- `Fresh`: Expiry Date is more than 4 days and the Ingredient name is kept with the same color and remains included with the suggested recipes.

### Recipe Details

User can select a suggested recipe and view:

- Recipe Name
- Meal Image
- Required Ingredients
- Required Quantities and measurement units
- Preparation Instructions

### Settings

The application includes a settings/profile screen for application preferences and managing user information.

## Technology Stack

- **Language:** Java
- **Platform:** Android
- **IDE:** Android Studio
- **Database:** SQLite
- **UI:** Android XML Layouts
- **Lists:** RecyclerView with a custom Adapter
- **Navigation:** Android Activities, Intents, and a reusable NavigationFragment
- **Version Control:** Git and GitHub

## Database

SQLite is used as the application's local database.

The database provide persistent storage for pantry items and recipes, allowing for information to remain available even after the application is closed and reopened.

The Database Structure contains the following:
- `pantry_items` - Stores the ingredients currently available to the user.
- `recipes` - Stores recipe information.
- `recipe_ingredients` - Stores the ingredients and quantities required by each recipe.

The database is managed through a custom DatabaseHelper class that extends SQLiteOpenHelper. It helps to manage the local storage of pantry ingredients and recipes, and even provides the data retrieval required by pantry management and recipe matching.

The current pantry implementation supports inserting ingredients and retrieving stored pantry items.

SQLite was selected because it provides local database storage without requiring an external database server or an internet connection.

## Application Screens

The application contains the following screens which have been created:

1. **Welcome Screen**
2. **Pantry Screen**
3. **Add/Edit Ingredient Screen**
4. **Recipes Screen**
5. **Recipe Details Screen**
6. **Settings Screen**

A reusable `NavigationFragment` has been implemented to provide navigation between the Pantry, Recipes, and Settings Screens.

## Project Structure

The project structure will be developed incrementally throughout the project.

The main components will include:

- Activities for Application Screens
- Fragments for reusable UI Components
- Model classes for Application Data
- SQLite Database Classes
- RecyclerView Adapters
- Recipe Matching and Ingredient Normalization Utilities
- XML Layouts and Android Resources

Current Activities include:

- `WelcomeActivity` - Initial Application Screen
- `MainActivity` - Main Pantry Screen, responsible for displaying stored ingredients
- `AddEditIngredientActivity` - Handles ingredient input and validation
- `RecipesActivity` - Recipes Screen
- `RecipeDetailActivity` - Displays the selected recipe's ingredients, meal image, and preparation instructions.
- `SettingsActivity` - Settings Screen

Current Fragment includes:

- `NavigationFragment` - Reusable navigation bar used by the Pantry, Recipes, and Settings Activity Screens.

Current Database and Model Components

- `DatabaseHelper` - Creates and manages the SQLite database and provides methods for storing and retrieving pantry ingredients
- `PantryItem` - Model class representing a pantry ingredient
- `PantryAdapter` - Custom RecyclerView adapter used to display pantry ingredients
- `Recipe` - Model class to represent a recipe
- `RecipeIngredient` - Represents an ingredient and quantity required by a recipe
- `RecipeAdapter` - Custom RecyclerView adapter to displaying recipes
- `RecipeMatcher` - Checks whether recipes can be prepared using sufficient, non expired pantry stock
- `IngredientNormalizer` - Normalizes ingredient names for matching purposes
- `UnitConverter` - Provides measurement unit methods
- `ExpiryDateHelper` - Parses Expiry Dates and checks whether ingredients have expired or are expiring soon
- `item_pantry.xml` - Layout defining how an individual pantry ingredient is displayed
- `item_recipe.xml` - Layout defining how an individual recipe card is displayed.

## Installation and Setup

### Requirements

- Android Studio
- Java Development Kit (JDK)
- Android Emulator or Compatible Android Device 
- Git

### Running the Application

1. Clone the repository
2. Open the project in Android Studio
3. Allow Android Studio to synchronize the Gradle Project.
4. Create or Start an Android Emulator, or Connect a Compatible Android Device.
5. Run the Application from Android Studio

## Version Control

Git is used throughout the development of Smart Pantry Manager

The project is hosted on GitHub and development is tracked through incremental commits documenting the implmentation of individual features.

## GitHub Repository
**Repository**
[Smart Pantry Manager GitHub Repository](https://github.com/BenjiTheGuy/Smart_Pantry_Manager)

## Author

**Benjamin Montague**

Mobile App Development 700