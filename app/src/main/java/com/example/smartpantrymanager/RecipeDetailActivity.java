package com.example.smartpantrymanager;

// Importing libraries to use in file.
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

// Activity responsible for displaying the details of the selected recipe.
public class RecipeDetailActivity extends AppCompatActivity {
    // Helper used to retrieve recipe ingredients from the SQLite database.
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout for the Recipe Details screen.
        setContentView(R.layout.activity_recipe_details);

        // Initializing the database helper.
        databaseHelper = new DatabaseHelper(this);

        // Connecting the TextViews and ImageView to their layout elements.
        TextView txtName = findViewById(R.id.txt_detail_recipe_name);
        TextView txtIngredients = findViewById(R.id.txt_detail_ingredients);
        TextView txtInstructions = findViewById(R.id.txt_detail_instructions);
        ImageView imgRecipe = findViewById(R.id.img_detail_recipe);

        // Retrieving the selected recipe's information passed through the Intent.
        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeInstructions = getIntent().getStringExtra("recipe_instructions");

        // Closing the activity if the required recipe information is missing.
        if (recipeId == -1 || recipeName == null || recipeInstructions == null) {
            finish();

            return;
        }

        // Displaying the recipe name and preparation instructions.
        txtName.setText(recipeName);
        txtInstructions.setText(recipeInstructions);

        // Displaying the same meal image used in RecipeAdapter.
        imgRecipe.setImageResource(RecipeAdapter.getRecipeImage(recipeName));

        // Retrieving the required ingredients for the selected recipe.
        List<String> ingredients = databaseHelper.getRecipeIngredients(recipeId);

        // Checking whether the selected recipe has any ingredients.
        if (ingredients.isEmpty()) {
            txtIngredients.setText("No ingredients listed.");
        } else {
            // Building a formatted list of the required ingredients.
            StringBuilder ingredientText = new StringBuilder();

            for (String ingredient : ingredients) {
                ingredientText.append("* ")
                        .append(ingredient)
                        .append("\n");
            }

            // Displaying the formatted ingredient list.
            txtIngredients.setText(ingredientText.toString().trim());
        }
    }
}
