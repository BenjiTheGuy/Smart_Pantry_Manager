package com.example.smartpantrymanager;

// Importing necessary libraries to implement class.
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Activity responsible for displaying the Recipes Screen to the User.
public class RecipesActivity extends AppCompatActivity {
    // Declaring private variables for the database and layout components.
    private DatabaseHelper databaseHelper;
    private RecipeMatcher recipeMatcher;
    private RecyclerView rvRecipes;
    private TextView txtNoRecipes;
    private Button btnAllRecipes;
    private Button btnSuggestedRecipes;

    // Adapter responsible for displaying recipe cards.
    private RecipeAdapter recipeAdapter;

    // Tracks which recipe view the user has selected
    private boolean showingSuggestedRecipes = false;

    // Overridden Lifecycle method to start up the Recipes Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Recipes Screen.
        setContentView(R.layout.activity_recipes);

        // Initializing database and layout views.
        databaseHelper = new DatabaseHelper(this);
        recipeMatcher = new RecipeMatcher(databaseHelper);

        // Connecting Java variables to the layout views.
        rvRecipes = findViewById(R.id.rv_recipes);
        txtNoRecipes = findViewById(R.id.txt_no_recipes);
        btnAllRecipes = findViewById(R.id.btn_all_recipes);
        btnSuggestedRecipes = findViewById(R.id.btn_suggested_recipes);

        // Arranging recipe cards vertically and displaying them.
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        // Showing all recipes when the screen first opens.
        btnAllRecipes.setOnClickListener(view -> {
            showingSuggestedRecipes = false;

            loadRecipes();
        });

        // Showing only the recipes that can be prepared from the pantry.
        btnSuggestedRecipes.setOnClickListener(view -> {
            showingSuggestedRecipes = true;

            loadRecipes();
        });

        // Loading recipes from the database
        loadRecipes();

        // Checking if this is the first time the Activity is being created.
        if (savedInstanceState == null) {
            NavigationFragment navigationFragment = NavigationFragment.newInstance("RECIPES");

            // Adding the Navigation Fragment to the Fragment Container
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragment_navigation,
                            navigationFragment
                    )
                    .commit();
        }
    }

    // Retrieving recipes and displaying them in the RecyclerView.
    private void loadRecipes() {
        List<Recipe> recipes;

        if (showingSuggestedRecipes) {
            // The matcher searches based on ingredient name, unit and quantity checks strictly.
            recipes = recipeMatcher.getMatchingRecipes();
        } else {
            // The All Recipes view displays every saved recipe.
            recipes = databaseHelper.getAllRecipes();
        }

        // Displaying a message if there aren't any results.
        if (recipes.isEmpty()) {
            rvRecipes.setVisibility(View.GONE);
            txtNoRecipes.setVisibility(View.VISIBLE);

            if (showingSuggestedRecipes) {
                // Message to display stating that no recipes meet the pantry requirements.
                txtNoRecipes.setText(R.string.suggested_recipes_empty);
            } else {
                // Message to display stating that the database has saved no recipes.
                txtNoRecipes.setText(R.string.recipes_database_empty);
            }

            return;
        }
        // Displaying the available recipes
        rvRecipes.setVisibility(View.VISIBLE);
        txtNoRecipes.setVisibility(View.GONE);

        // Creating the RecipeAdapter and defining what happens when a recipe is selected.
        recipeAdapter = new RecipeAdapter(recipes, recipe -> {
            // Creating an Intent to open up the Recipe Details screen.
            Intent intent = new Intent(
                    RecipesActivity.this,
                    RecipeDetailActivity.class
            );

            // Passing the selected recipe details to retrieve the recipe's information.
            intent.putExtra("recipe_id", recipe.getId());
            intent.putExtra("recipe_name", recipe.getName());
            intent.putExtra("recipe_instructions", recipe.getInstructions());

            // Opening the Recipe Details screen.
            startActivity(intent);
        });

        // Connecting the adapter to the RecycleView to display the recipes.
        rvRecipes.setAdapter(recipeAdapter);
    }
}
