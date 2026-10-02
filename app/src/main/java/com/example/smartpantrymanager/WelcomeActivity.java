package com.example.smartpantrymanager;

// Importing libraries required to run the class.
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for displaying the Welcome Screen to the User.
public class WelcomeActivity extends AppCompatActivity {
    // Declaring the Get Started Button
    private Button btnGetStarted;

    // Overridden Lifecycle Method used to start up the Welcome Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Welcome Screen.
        setContentView(R.layout.activity_welcome);

        // Initializing the Get Started Button by linking it to the variable defined in the XML layout.
        btnGetStarted = findViewById(R.id.get_started);

        // Creating an Action Event Listener on the Button to respond when clicked.
        btnGetStarted.setOnClickListener(view -> {
            // Explicit Intent used to open up the next Activity, which is the Pantry Screen.
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);

            // Starting the Pantry Screen using the Intent.
            startActivity(intent);
        });
    }
}