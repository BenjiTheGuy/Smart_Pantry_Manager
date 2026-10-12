package com.example.smartpantrymanager;

// Importing necessary libraries to implement class.
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for displaying the Recipes Screen to the User.
public class RecipesActivity extends AppCompatActivity {
    // Overridden Lifecycle method to start up the Recipes Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Recipes Screen.
        setContentView(R.layout.activity_recipes);

        // String variable used to hold the recipe name when
        String recipeName = getIntent().getStringExtra("recipe_name");

        if (recipeName.isEmpty()) {
            // txtIngredientName.setError("Ingredient Name is Required!);

            return;
        }

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
}
