package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Checking if this is the first time the Activity is being created.
        if (savedInstanceState == null) {
            NavigationFragment navigationFragment = NavigationFragment.newInstance("SETTINGS");

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
