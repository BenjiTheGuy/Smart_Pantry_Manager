package com.example.smartpantrymanager;

// Importing libraries required to run the class.
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Activity responsible for displaying the Pantry Screen to the User.
public class MainActivity extends AppCompatActivity {
    // Declaring the buttons to be created in this Activity.
    private Button btnAddIngredient;
    private RecyclerView rvPantry;
    private TextView txtEmptyPantry;
    private TextView txtWelcomeBack;

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
        txtWelcomeBack = findViewById(R.id.p_welcome_back);

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

        // Reading the same preferences file used by SettingsActivity
        SharedPreferences preferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        // Retrieving the user's saved display name.
        String userName = preferences.getString("profile_name", "").trim();

        // Displaying a personalized greeting if a name has been saved.
        if (userName.isEmpty()) {
            txtWelcomeBack.setText("Welcome Back!");
        } else {
            txtWelcomeBack.setText("Welcome back, " + userName + "!");
        }

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
        PantryAdapter adapter = new PantryAdapter(pantryItems,
                item -> {
                    // Opening the Add/Edit Ingredient Screen.
                    Intent intent = new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class);

                    // Passing the selected ingredient's database ID.
                    intent.putExtra("ingredient_id", item.getId());

                    // Starting the Add/Edit Screen.
                    startActivity(intent);
                },
                // Long pressing asks the user if they want to confirm deletion of the ingredient.
                item -> showDeleteConfirmation(item)
        );

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

    // Displaying a confirmation dialog before deleting an ingredient.
    private void showDeleteConfirmation(PantryItem item) {
        // Creating the builder object.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Setting the parameters of the builder dialog.
        builder.setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Deleting the selected ingredient from SQLite.
                        int rowsDeleted = databaseHelper.deletePantryItem(item.getId());

                        if (rowsDeleted > 0) {
                            Toast.makeText(
                                    MainActivity.this,
                                    "Ingredient Deleted Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Closing the Confirmation dialog
                            dialogInterface.dismiss();

                            // Refreshing the pantry list.
                            loadPantryItems();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    "Failed to Delete Ingredient",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        // Creating and displaying the Dialog.
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}