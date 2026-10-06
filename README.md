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

## Planned Features

### Pantry Management

Users will be able to:
- Add ingredients to their pantry.
- Edit existing ingredients.
- Delete ingredients.
- View all current pantry items.
- Record ingredient quantities and units.
- Record expiry dates.

### Recipe Suggestions

The application will enable for users to compare their pantry against the recipe database.

A recipe will only be suggested to the user if the following conditions are met:

1. Every required ingredient is made available in the pantry.
2. The available quantity is sufficient.
3. Ingredient names can be matched despite simple differences in wording.

Recipes that are missing required ingredients are immediately not suggested to the user.

### Recipe Details

User will be able to select a suggested recipe and view:

- Recipe Name
- Required Ingredients
- Required Quantities
- Preparation Instructions

### Settings

The application will include a settings/profile screen for application preferences such as expiry alerts.

## Technology Stack

- **Language:** Java
- **Platform:** Android
- **IDE:** Android Studio
- **Database:** SQLite
- **UI:** Android XML Layouts
- **Lists:** RecyclerView
- **Navigation:** Android Activities, Intents, and a reusable NavigationFragment
- **Version Control:** Git and GitHub

## Database

SQLite is used as the application's local database.

The database will provide persistent storage for pantry items and recipes, allowing for information to remain available even after the application is closed and reopened.

The Database Structure will contain the following:
- `pantry_items` - Stores the ingredients currently available to the user.
- `recipes` - Stores recipe information.
- `recipe_ingredients` - Stores the ingredients and quantities required by each recipe.

The database will also support all CRUD operations needed for management of the pantry.

## Application Screens

The application is being developed incrementally. It currently contains the following screens which have been created:

1. **Welcome Screen**
2. **Pantry Screen**
3. **Recipes Screen**
4. **Settings Screen**

A reusable `NavigationFragment` has been implemented to provide navigation between the Pantry, Recipes, and Settings Screens.

The following screens will be implemented as development progresses.

5. **Add/Edit Ingredient Screen**
6. **Recipe Detail Screen**

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
- `MainActivity` - Main Pantry Screen
- `RecipesActivity` - Recipes Screen
- `SettingsActivity` - Settings Screen

Current Fragment includes:

- `NavigationFragment` - Reusable navigation bar used by the Pantry, Recipes, and Settings Activity Screens.

Additional Activities will be added as development progresses.

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

Setup instructions will be updated if additional configuration is required during development.

## Version Control

Git is used throughout the development of Smart Pantry Manager

The project is hosted on GitHub and development will be tracked through incremental commits.

## GitHub Repository
**Repository**
[Smart Pantry Manager GitHub Repository](https://github.com/BenjiTheGuy/Smart_Pantry_Manager)

## Author

**Benjamin Montague**

Mobile App Development 700