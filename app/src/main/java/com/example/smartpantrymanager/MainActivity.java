package com.example.smartpantrymanager;

// Importing libraries required to run the class.
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for displaying the Pantry Screen to the User.
public class MainActivity extends AppCompatActivity {
    // Declaring the buttons to be created in this Activity.
    private Button btnAddIngredient;

    // Overridden Lifecycle method to start up the Pantry Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Pantry Screen.
        setContentView(R.layout.activity_main);

        // Linking the Java Button Variables to the buttons defined in the XML Layout.
        btnAddIngredient = findViewById(R.id.add_ingredient);

        // Checking if this is the first time the Activity is being created.
        if (savedInstanceState == null) {
            NavigationFragment navigationFragment = NavigationFragment.newInstance("PANTRY");

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