package com.example.smartpantrymanager;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    // SharedPreferences stores small pieces of data locally on the device.
    private static final String PREFS_NAME = "SmartPantrySettings";

    // Keys used to store and retrieve profile information.
    private static final String KEY_PROFILE_NAME = "profile_name";
    private static final String KEY_PROFILE_EMAIL = "profile_email";

    // Creating Java variables used to connect to the resource layout file.
    private EditText editProfileName;
    private EditText editProfileEmail;
    private TextView txtProfileStatus;
    private Button btnSaveProfile;
    private SharedPreferences preferences;

    // Loading the previously saved profile details into the input fields.
    private void loadProfile() {
        String savedName = preferences.getString(KEY_PROFILE_NAME, "");
        String savedEmail = preferences.getString(KEY_PROFILE_EMAIL, "");

        editProfileName.setText(savedName);
        editProfileEmail.setText(savedEmail);
    }

    // Validates the profile information and saves it locally.
    private void saveProfile() {
        String name = editProfileName.getText().toString().trim();
        String email = editProfileEmail.getText().toString().trim();

        // A display name is required.
        if (name.isEmpty()) {
            editProfileName.setError("Please enter your name.");

            editProfileName.requestFocus();

            return;
        }

        // Only validating the email when user has entered an email address.
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editProfileName.setError("Please enter a valid email address.");

            editProfileName.requestFocus();

            return;
        }

        // Saving both values to local storage
        preferences.edit()
                .putString(KEY_PROFILE_NAME, name)
                .putString(KEY_PROFILE_EMAIL, email)
                .apply();

        // Show confirmation to the user.
        txtProfileStatus.setText("Profile saved successfully.");
        txtProfileStatus.setVisibility(View.VISIBLE);

        Toast.makeText(
                this,
                "Profile Saved Successfully.",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the Settings screen layout.
        setContentView(R.layout.activity_settings);

        // Linking Java variables to the layout components.
        editProfileName = findViewById(R.id.editProfileName);
        editProfileEmail = findViewById(R.id.editProfileEmail);
        txtProfileStatus = findViewById(R.id.txtProfileStatus);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Opening the preferences file.
        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        // Restoring the profile information saved during an earlier visit.
        loadProfile();

        // Saving the profile when the user taps the button.
        btnSaveProfile.setOnClickListener(view -> {
            saveProfile();
        });

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

        // Hiding the previous success message when the user edits either field again.
        editProfileName.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                txtProfileStatus.setVisibility(View.GONE);
            }
        });

        editProfileEmail.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                txtProfileStatus.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Hiding the on-screen confirmation when loading this Activity.
        if (txtProfileStatus != null) {
            txtProfileStatus.setVisibility(View.GONE);
        }
    }
}
