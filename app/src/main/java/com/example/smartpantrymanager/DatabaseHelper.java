package com.example.smartpantrymanager;

// Importing libraries to utilize in Database.
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

// Class responsible for creating and managing the SQLite Database.
public class DatabaseHelper extends SQLiteOpenHelper {
    // Declaring the database name and version
    private static final String DATABASE_NAME = "smart_pantry.db";
    // Database version increments as changes are made to it and to preserve any existing data found.
    private static final int DATABASE_VERSION = 5;

    // Declaring the Pantry table name
    public static final String TABLE_PANTRY = "pantry_items";

    // Declaring the column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // Constant SQL Statement used to create the Pantry Table upon creation or loading of the Database.
    private static final String CREATE_PANTRY_TABLE =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY_DATE + " TEXT" +
                    ")";

    // Declaring the Recipes table name.
    public static final String TABLE_RECIPES = "recipes";

    // Declaring the column names.
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_INSTRUCTIONS = "instructions";

    // Constant SQL Statement used to create the Recipes Table upon creation or loading of the Database.
    private static final String CREATE_RECIPES_TABLE =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_INSTRUCTIONS + " TEXT NOT NULL " +
                    ")";

    // Declaring the Recipe Ingredients table name
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Declaring the column names
    public static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    public static final String COLUMN_RECIPE_FOREIGN_ID = "recipe_id";
    public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_REQUIRED_QUANTITY = "quantity";
    public static final String COLUMN_REQUIRED_UNIT = "unit";

    // Constant SQL Statement used to create the Recipe Ingredients Table upon creation or loading
    // of the Database.
    private static final String CREATE_RECIPE_INGREDIENTS_TABLE =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                    COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_FOREIGN_ID + " INTEGER NOT NULL, " +
                    COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_REQUIRED_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_REQUIRED_UNIT + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_RECIPE_FOREIGN_ID + ") " +
                    "REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + ") " +
                    "ON DELETE CASCADE" +
                    ")";

    // Constructor used to initialize the database helper.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method which is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the Pantry Table.
        db.execSQL(CREATE_PANTRY_TABLE);

        // Creating the Recipes Table.
        db.execSQL(CREATE_RECIPES_TABLE);

        // Creating the Recipe Ingredients Table.
        db.execSQL(CREATE_RECIPE_INGREDIENTS_TABLE);

        // Inserting the initial recipes and their ingredients
        seededRecipes(db);
    }

    // Method used to enable the ON DELETE CASCADE rule found within the Recipe Ingredients table to
    // work when a recipe is deleted.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        // Enforcing foreign key relationships between database tables.
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Method called when the database version is increased.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Adding Recipe Tables when upgrading from database version 1.
        if (oldVersion < 2) {
            db.execSQL(CREATE_RECIPES_TABLE);
            db.execSQL(CREATE_RECIPE_INGREDIENTS_TABLE);
        }

        if (oldVersion < 3) {
            seededRecipes(db);
        }

        if (oldVersion < 4) {
            // Removing old recipe data only.
            db.delete("recipe_ingredients", null, null);
            db.delete("recipes", null, null);

            // Inserting the updated recipes using your current seed method.
            seededRecipes(db);
        }

        if (oldVersion < 5) {
            // Removing old recipe data only.
            db.delete("recipe_ingredients", null, null);
            db.delete("recipes", null, null);

            // Inserting the updated recipes using your current seed method.
            seededRecipes(db);
        }
    }

    // Method used to retrieve all ingredients from the database.
    public List<PantryItem> getAllPantryItems() {
        // Creating a list to store the retrieved ingredients.
        List<PantryItem> pantryItems = new ArrayList<>();

        // Opening the database for reading
        SQLiteDatabase db = getReadableDatabase();

        // Querying the pantry table and retrieving all columns.
        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        // Reading each row returned by the query.
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );

                // Creating a PantryItem object from the database row
                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                // Adding the ingredient to the list.
                pantryItems.add(item);
            }
        } finally {
            // Closing the cursor after reading the results.
            cursor.close();
        }

        // Returning the retrieved ingredients.
        return pantryItems;
    }

    // Updating an existing pantry ingredient using its database ID
    public int updatePantryItem(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);

        if (expiryDate == null || expiryDate.isEmpty()) {
            values.putNull(COLUMN_EXPIRY_DATE);
        } else {
            values.put(COLUMN_EXPIRY_DATE, expiryDate);
        }

        // Updating only the pantry item matching the supplied ID.
        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID +  " = ?",
                new String[]{String.valueOf(id)}
        );
    };

    // Deleting an ingredient from the Pantry by using its database ID.
    public int deletePantryItem(int id) {
        // Opening the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // Deleting only the ingredient matching the supplied ID.
        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Method used to insert a single recipe along with all the required ingredients.
    private void insertRecipe(
            SQLiteDatabase db,
            String name,
            String instructions,
            String[][] ingredients) {
        // Storing the recipe's details.
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COLUMN_RECIPE_NAME, name);
        recipeValues.put(COLUMN_INSTRUCTIONS, instructions);

        // Inserting the recipe and retrieving the database ID associated with the recipe.
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        // Stopping the method if the recipe couldn't be inserted.
        if (recipeId == -1) {
            return;
        }

        // Inserting each ingredient associated with this recipe.
        for (String[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();

            ingredientValues.put(COLUMN_RECIPE_FOREIGN_ID, recipeId);
            ingredientValues.put(COLUMN_INGREDIENT_NAME, ingredient[0]);
            ingredientValues.put(COLUMN_REQUIRED_QUANTITY, Double.parseDouble(ingredient[1]));
            ingredientValues.put(COLUMN_REQUIRED_UNIT, ingredient[2]);

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }

    // Inserting the initial recipe collection into the database.
    private void seededRecipes(SQLiteDatabase db) {
        // Preventing duplicate seeding if this method is called again.
        if (db.compileStatement("SELECT COUNT(*) FROM " + TABLE_RECIPES)
                .simpleQueryForLong() > 0) {
            return;
        }

        // Recipe 1
        insertRecipe(db, "Pancakes",
                "Mix the flour, milk, eggs and sugar. Pour pancake mix as portions" +
                        " onto a hot pan and cook both sides.",
                new String[][]{
                        {"Flour", "50", "g"},
                        {"Milk", "240", "ml"},
                        {"Eggs", "3", "pieces"},
                        {"Sugar", "25", "g"}
                });

        // Recipe 2
        insertRecipe(db, "Banana Pancakes",
                "Mash the bananas, mix the flour mix, milk, eggs and sugar. Pour " +
                        "pancake mix as portions onto a hot pan and cook both sides.",
                new String[][]{
                        {"Bananas", "2", "pieces"},
                        {"Flour", "120", "g"},
                        {"Milk", "250", "ml"},
                        {"Eggs", "3", "pieces"},
                        {"Sugar", "25", "g"}
                });

        // Recipe 3
        insertRecipe(db, "Scrambled Eggs on Toast",
                "Toast the bread slices. Mix eggs with milk and salt. Melt butter in a " +
                        "pan, add the egg mix, and stir gently until fully cooked. Serve with " +
                        "the toast.",
                new String[][]{
                        {"Bread", "2", "pieces"},
                        {"Eggs", "2", "pieces"},
                        {"Milk", "30", "ml"},
                        {"Butter", "50", "g"},
                        {"Salt", "6", "g"}
                });

        // Recipe 4
        insertRecipe(db, "Cheese Pasta",
                "Boil the pasta, drain it, and mix it with milk and grated cheese until" +
                        " made creamy.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Milk", "100", "ml"},
                        {"Cheese", "50", "g"},
                        {"Salt", "6", "g"}
                });

        // Recipe 5
        insertRecipe(db, "Cheese Toast",
                "Place cheese on bread and butter both sides. Toast the sandwich until" +
                        " both sides are golden brown.",
                new String[][]{
                        {"Bread", "2", "pieces"},
                        {"Cheese", "40", "g"},
                        {"Butter", "5", "g"}
                });

        // Recipe 6
        insertRecipe(db, "Full English Breakfast",
                "Prepare bread slices for toasting, cut onion slices and grill until " +
                        "golden brown, boil sweet potatoes, fry eggs and sausages with oil.",
                new String[][]{
                        {"Bread", "2", "pieces"},
                        {"Onion", "150", "g"},
                        {"Oil", "15", "ml"},
                        {"Eggs", "2", "pieces"},
                        {"Sweet Potatoes", "600", "g"},
                        {"Sausages", "2", "pieces"}
                });

        // Recipe 7
        insertRecipe(db, "Tomato Cheese Sandwich",
                "Layer onto bread slices, slices of tomato and cheese. Toast if wanted.",
                new String[][]{
                        {"Bread", "2", "pieces"},
                        {"Tomato", "150", "g"},
                        {"Cheese", "40", "g"}
                });

        // Recipe 8
        insertRecipe(db, "Egg Fried Rice",
                "Cook the rice, scramble the eggs, and then stir-fry the mix with salt " +
                        "and oil until golden brown.",
                new String[][]{
                        {"Rice", "307", "g"},
                        {"Eggs", "2", "pieces"},
                        {"Oil", "30", "ml"},
                        {"Salt", "12", "g"}
                });

        // Recipe 9
        insertRecipe(db, "Mince Pasta",
                "Prepare mince portions by cooking it onto a separate hot pan with " +
                        "salt and oil, boil the pasta and drain it. Add salt and cooked mince " +
                        "together and mix them.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Mince", "200", "g"},
                        {"Salt", "12", "g"},
                        {"Oil", "30", "ml"}
                });

        // Recipe 10
        insertRecipe(db, "Chicken Pasta",
                "Prepare chicken portions by cooking it onto a separate hot pan with " +
                        "salt and oil, boil the pasta and drain it. Add salt and cooked chicken " +
                        "together and mix them.",
                new String[][]{
                        {"Chicken", "0.5", "kg"},
                        {"Pasta", "200", "g"},
                        {"Salt", "12", "g"},
                        {"Oil", "30", "ml"}
                });

        // Recipe 11
        insertRecipe(db, "Chicken Rice with Sweet Potatoes",
                "Cook chicken portions with salt and oil, boil rice, drain water " +
                        "from rice, boil sweet potatoes. Add chicken portions, sweet potatoes, " +
                        "and rice together. Mix well",
                new String[][]{
                        {"Chicken", "0.5", "kg"},
                        {"Rice", "370", "g"},
                        {"Salt", "12", "g"},
                        {"Oil", "30", "ml"},
                        {"Sweet Potatoes", "600", "g"}
                });

        // Recipe 12
        insertRecipe(db, "Egg Toast",
                "Mix eggs onto a bowl, add milk and salt. Prepare bread slices to be " +
                        "added onto the egg mix. Cook on both sides, until the egg mix is soaked " +
                        "by the bread and turned golden brown. Remove from the pan.",
                new String[][]{
                        {"Eggs", "3", "pieces"},
                        {"Milk", "100", "ml"},
                        {"Salt", "12", "g"},
                        {"Bread", "5", "pieces"}
                });

        // Recipe 13
        insertRecipe(db, "Basil Pesto",
                "Blend fresh basil, garlic, Parmesan cheese, olive oil, and peanuts " +
                        "until a smooth pesto is formed. Add this pesto onto cooked pasta.",
                new String[][]{
                        {"Basil", "40", "g"},
                        {"Garlic", "2", "pieces"},
                        {"Parmesan Cheese", "50", "g"},
                        {"Olive Oil", "120", "ml"},
                        {"Peanuts", "25", "g"}
                });

        // Recipe 14
        insertRecipe(db, "Beef Stew",
                "Cook beef in a pot until brown, add chopped up vegetables such as " +
                        " potatoes, carrots, tomatoes and water. Simmer until beef and vegetables" +
                        " are firm and tender.",
                new String[][]{
                        {"Beef", "5", "kg"},
                        {"Potatoes", "300", "g"},
                        {"Carrots", "300", "g"},
                        {"Tomatoes", "300", "g"},
                        {"Salt", "6", "g"}
                });

        // Recipe 15
        insertRecipe(db, "Roasted Chicken and Vegetables",
                "Season the chicken with salt and oil. Chop up potatoes and carrots " +
                        "then roast them together with the chicken until fully cooked.",
                new String[][]{
                        {"Chicken", "1", "kg"},
                        {"Potatoes", "500", "g"},
                        {"Carrots", "250", "g"},
                        {"Oil", "30", "ml"},
                        {"Salt", "6", "g"}
                });
    }
    // Retrieving all recipes stored in the database.
    public List<Recipe> getAllRecipes() {
        // Creating a list to store the retrieved recipes.
        List<Recipe> recipes = new ArrayList<>();

        // Opening the database for reading
        SQLiteDatabase db = getReadableDatabase();

        // Retrieving all recipes, sorted alphabetically by name.
        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME + " ASC"
        );

        try {
            // Reading each recipe returned by the query.
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)
                );

                // Creating a Recipe object from the database row.
                Recipe recipe = new Recipe(
                        id,
                        name,
                        instructions
                );

                // Adding the recipe to the list.
                recipes.add(recipe);
            }
        } finally {
            // Closing the cursor after reading the results.
            cursor.close();
        }

        // Returning the complete recipe list.
        return recipes;
    };

    // Retrieving all the required ingredients for a specific recipe.
    public List<String> getRecipeIngredients(int recipeId) {
        // Creating a list to store the retrieved ingredients.
        List<String> ingredients = new ArrayList<>();

        // Opening the database for reading
        SQLiteDatabase db = getReadableDatabase();

        // Retrieving all ingredients, sorted alphabetically by name.
        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                new String[] {
                        COLUMN_INGREDIENT_NAME,
                        COLUMN_REQUIRED_QUANTITY,
                        COLUMN_REQUIRED_UNIT
                },
                COLUMN_RECIPE_FOREIGN_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                COLUMN_INGREDIENT_NAME + " ASC"
        );

        try {
            // Reading each recipe ingredients returned by the query.
            while (cursor.moveToNext()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_UNIT)
                );

                ingredients.add(name + " - " + quantity + " " + unit);
            }
        } finally {
             // Closing the cursor after reading the results.
             cursor.close();
        }
        // Returning the complete ingredients list.
        return ingredients;
    }

    // Retrieving structured ingredient objects for recipe matching.
    public List<RecipeIngredient> getRecipeIngredientDetails(int recipeId) {
        // Creating a list to hold the recipe's ingredient objects.
        List<RecipeIngredient> ingredients = new ArrayList<>();

        // Opening the database for reading
        SQLiteDatabase db = getReadableDatabase();

        // Retrieving each ingredient's ID, recipe ID, name, quantity and unit.
        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                new String[] {
                        COLUMN_RECIPE_INGREDIENT_ID,
                        COLUMN_RECIPE_FOREIGN_ID,
                        COLUMN_INGREDIENT_NAME,
                        COLUMN_REQUIRED_QUANTITY,
                        COLUMN_REQUIRED_UNIT
                },
                COLUMN_RECIPE_FOREIGN_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                COLUMN_INGREDIENT_NAME + " ASC"
        );

        try {
            // Reading each ingredient returned by the database query.
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENT_ID)
                );

                int storedRecipeId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_FOREIGN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_UNIT)
                );

                // Creating a Recipe Ingredient object from the database row
                RecipeIngredient ingredient = new RecipeIngredient(
                        id,
                        storedRecipeId,
                        name,
                        quantity,
                        unit
                );

                // Adding the object to the result list.
                ingredients.add(ingredient);
            }
        } finally {
            // Closing the cursor to release database resources.
            cursor.close();
        }

        // Returning the structured recipe ingredients.
        return ingredients;
    }
}
