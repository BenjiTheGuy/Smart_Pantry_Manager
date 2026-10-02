package com.example.smartpantrymanager;

// Importing libraries required to run the class.
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for displaying the Pantry Screen to the User.
public class MainActivity extends AppCompatActivity {
    // Declaring the buttons to be created in this Activity.
    private Button btnAddIngredient, btnNavPantry, btnNavRecipes, btnNavSettings;

    // Overridden Lifecycle method to start up the Pantry Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Pantry Screen.
        setContentView(R.layout.activity_main);

        // Linking the Java Button Variables to the buttons defined in the XML Layout.
        btnAddIngredient = findViewById(R.id.add_ingredient);
        btnNavPantry = findViewById(R.id.nav_pantry);
        btnNavRecipes = findViewById(R.id.nav_recipes);
        btnNavSettings = findViewById(R.id.nav_settings);

        // Creating an Action Event Listener for the Pantry Navigation Button.
        btnNavPantry.setOnClickListener(view -> {
            // Toast Message to display to the user that they are currently on the page.
            Toast.makeText(
                    MainActivity.this,
                    "You are currently on the Pantry Page.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Creating an Action Event Listener for the Pantry Recipes Button.
        btnNavRecipes.setOnClickListener(view -> {
            // Explicit Intent used to open up the next Activity, which is the Recipes Screen.
            Intent intent = new Intent(MainActivity.this, RecipesActivity.class);

            // Starting the Recipes Screen using the Intent.
            startActivity(intent);
        });

        // Creating an Action Event Listener for the Pantry Settings Button.
        btnNavSettings.setOnClickListener(view -> {
            // Explicit Intent used to open up the next Activity, which is the Settings Screen.
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            // Starting the Settings Screen using the Intent.
            startActivity(intent);
        });
    }
}