package com.example.smartpantrymanager;

// Importing libraries required to run the class.
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Activity responsible for displaying the Pantry Screen to the User.
public class MainActivity extends AppCompatActivity {
    // Declaring the buttons to be created in this Activity.
    private Button btnAddIngredient;
    private RecyclerView rvPantry;
    private TextView txtEmptyPantry;

    private DatabaseHelper databaseHelper;

    // Overridden Lifecycle method to start up the Pantry Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Pantry Screen.
        setContentView(R.layout.activity_main);

        // Linking the Java Variables to the XML Layout Components.
        btnAddIngredient = findViewById(R.id.add_ingredient);
        rvPantry = findViewById(R.id.rv_pantry);
        txtEmptyPantry = findViewById(R.id.p_empty_pantry);

        // Creating the database helper
        databaseHelper = new DatabaseHelper(this);

        // Setting the RecyclerView to display items vertically
        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        // OnClick Listener used to open up the Activity which is the Add / Edit Screen.
        btnAddIngredient.setOnClickListener(v -> {
            // Intent used to open up the activity.
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);

            // Starting Activity.
            startActivity(intent);
        });

        // Loading the saved pantry ingredients
        loadPantryItems();

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

    // Reloading the pantry whenever this Activity becomes active again.
    @Override
    protected void onResume() {
        super.onResume();

        // Refreshing the list after returning from another Activity.
        if (databaseHelper != null && rvPantry != null) {
            loadPantryItems();
        }
    }

    // Method used to retrieve and display pantry ingredients
    private void loadPantryItems() {
        // Retrieving all saved ingredients from SQLite
        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        // Creating an adapter using the retrieved ingredients.
        PantryAdapter adapter = new PantryAdapter(pantryItems, item -> {
            // Opening the Add/Edit Ingredient Screen.
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);

            // Passing the selected ingredient's database ID.
            intent.putExtra("ingredient_id", item.getId());

            startActivity(intent);
        });

        // Connecting the adapter to the RecyclerView and displaying the pantry ingredients.
        rvPantry.setAdapter(adapter);

        // Checking whether the pantry contains any ingredients
        if (pantryItems.isEmpty()) {
            // Showing the empty pantry message
            txtEmptyPantry.setVisibility(View.VISIBLE);

            // Hiding the empty RecyclerView
            rvPantry.setVisibility(View.GONE);
        } else {
            // Hiding the empty pantry message
            txtEmptyPantry.setVisibility(View.GONE);

            // Showing the ingredient list.
            rvPantry.setVisibility(View.VISIBLE);
        }
    }
}