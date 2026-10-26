package com.example.smartpantrymanager;

// Importing necessary libraries to implement class.
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Activity responsible for displaying the Recipes Screen to the User.
public class RecipesActivity extends AppCompatActivity {
    // Declaring private variables.
    private DatabaseHelper databaseHelper;
    private RecyclerView rvRecipes;
    private TextView txtNoRecipes;
    private RecipeAdapter recipeAdapter;

    // Overridden Lifecycle method to start up the Recipes Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Recipes Screen.
        setContentView(R.layout.activity_recipes);

        // Initializing database and layout views.
        databaseHelper = new DatabaseHelper(this);
        rvRecipes = findViewById(R.id.rv_recipes);
        txtNoRecipes = findViewById(R.id.txt_no_recipes);

        // Arranging recipe cards vertically.
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

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
        List<Recipe> recipes = databaseHelper.getAllRecipes();

        if (recipes.isEmpty()) {
            // Displaying a message if no recipes were found.
            rvRecipes.setVisibility(View.GONE);
            txtNoRecipes.setVisibility(View.VISIBLE);
        } else {
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
}
