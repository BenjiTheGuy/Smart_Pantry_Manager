package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Fragment responsible for the application's reusable navigation bar.
public class NavigationFragment extends Fragment {
    // Declaring the Navigation Buttons
    private Button navPantry, navRecipes, navSettings;
    private String currentActivity;

    // Method to tell the Fragment which Activity is currently active.
    public static NavigationFragment newInstance(String currentActivity) {
        // Creating a new instance of the Navigation Fragment.
        NavigationFragment fragment = new NavigationFragment();

        // Creating a Bundle to store data that will be passed to the Fragment.
        Bundle args = new Bundle();

        // Storing the Current Activity name inside the Bundle.
        args.putString("current_activity", currentActivity);

        // Attaching the Bundle to the Fragment.
        fragment.setArguments(args);

        // Returning the changed Fragment.
        return fragment;
    }

    // Overridden Lifecycle method which is used to create the Fragment's view.
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // Inflating the Fragment's XML Layout.
        return inflater.inflate(
                R.layout.fragment_navigation,
                container,
                false
        );
    }

    // Overridden Lifecycle method called after the Fragment's view has been created.
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        // Creating view variable to represent the Fragment's inflated layout.
        super.onViewCreated(view, savedInstanceState);

        // Checking whether the arguments were passed to the Fragment.
        if (getArguments() != null) {
            // Retrieving the current Activity name from the Bundle.
            currentActivity = getArguments().getString("current_activity");
        }

        // Linking the Java button variables to the buttons defined in the Fragment XML.
        navPantry = view.findViewById(R.id.nav_pantry);
        navRecipes = view.findViewById(R.id.nav_recipes);
        navSettings = view.findViewById(R.id.nav_settings);

        // Creating an Action Event Listener for the Pantry Navigation Buttons.
        navPantry.setOnClickListener(v -> {
            // If else statements that are executed depending on the Current Activity open.
            if ("PANTRY".equals(currentActivity)) {
                // Toast Message to display to the user that they are currently on the page.
                Toast.makeText(
                        requireContext(),
                        "You are currently on the Pantry Page.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // Creating an Explicit Intent to open the Pantry Screen.
                Intent intent = new Intent(requireActivity(), MainActivity.class);

                // Starting the Pantry Activity.
                startActivity(intent);
            }
        });

        // Creating an Action Event Listener for the Pantry Recipes Button.
        navRecipes.setOnClickListener(v -> {
            // If else statements that are executed depending on the Current Activity open.
            if ("RECIPES".equals(currentActivity)) {
                // Toast Message to display to the user that they are currently on the page.
                Toast.makeText(
                        requireContext(),
                        "You are currently on the Recipes Page.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // Explicit Intent used to open up the next Activity, which is the Recipes Screen.
                Intent intent = new Intent(requireActivity(), RecipesActivity.class);

                // Starting the Recipes Screen using the Intent.
                startActivity(intent);
            }
        });

        // Creating an Action Event Listener for the Pantry Settings Button.
        navSettings.setOnClickListener(v -> {
            // If else statements that are executed depending on the Current Activity open.
            if ("SETTINGS".equals(currentActivity)) {
                // Toast Message to display to the user that they are currently on the page.
                Toast.makeText(
                        requireContext(),
                        "You are currently on the Recipes Page.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // Explicit Intent used to open up the next Activity, which is the Settings Screen.
                Intent intent = new Intent(requireActivity(), SettingsActivity.class);

                // Starting the Settings Screen using the Intent.
                startActivity(intent);
            }
        });
    }
}
