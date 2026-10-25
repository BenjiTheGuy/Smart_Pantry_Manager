package com.example.smartpantrymanager;

// Importing necessary libraries to implement class.
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

// Importing date and number-handling classes.
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for displaying the Add/Edit Screen to the User.
public class AddEditIngredientActivity extends AppCompatActivity {
    // Declaring the form components.
    private EditText editIngredientName;
    private EditText editQuantity;
    private Spinner spinUnit;
    private Button btnExpiryDate;
    private Button btnSave;

    // Storing the selected expiry date.
    private String expiryDate = "";

    // Overridden Lifecycle method to start up the Add/Edit Activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Calling the parent Activity's onCreate method
        super.onCreate(savedInstanceState);

        // Setting the Activity's Layout to display the Add/Edit Screen.
        setContentView(R.layout.activity_add_edit_ingredient);

        // Linking Java variables to the XML components.
        editIngredientName = findViewById(R.id.aei_name);
        editQuantity = findViewById(R.id.aei_quantity);
        spinUnit = findViewById(R.id.aei_unit);
        btnExpiryDate = findViewById(R.id.aei_expiry_date);
        btnSave = findViewById(R.id.aei_save);

        // Opening the date picket when the expiry button is pressed.
        btnExpiryDate.setOnClickListener(v -> {
            openDatePicker();
        });

        // Validating the form when the Save button is pressed.
        btnSave.setOnClickListener(v -> {
            validateIngredient();
        });
    }

    // Method used to display the Expiry Date Picker.
    private void openDatePicker() {
        // Obtaining the current date for the date picker.
        Calendar calendar = Calendar.getInstance();

        // Creating a DatePickerDialog with the current date as its initial value.
        DatePickerDialog dtPick = new DatePickerDialog(
                AddEditIngredientActivity.this,

                (view, year, month, day) -> {
                    // Formatting the selected date as DD/MM/YYYY
                    expiryDate = day + "/" + (month + 1) + "/" + year;

                    btnExpiryDate.setText(expiryDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Preventing the user from selecting a date before the current date.
        dtPick.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Displaying the date picker
        dtPick.show();
    }

    // Method used to perform input validation on input fields before saving the data to the database.
    private void validateIngredient() {
        // Reading the user's input and removing any surrounding whitespaces.
        String ingredientName = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        // Reading the selected unit of measurement.
        String unit = spinUnit.getSelectedItem().toString();

        // Checking that the ingredient name is not empty.
        if (ingredientName.isEmpty()) {
            editIngredientName.setError("Ingredient Name is Required.");
            editIngredientName.requestFocus();

            return;
        }

        // Checking that the quantity has been entered.
        if (quantityText.isEmpty()) {
            editQuantity.setError("Quantity is required.");
            editQuantity.requestFocus();

            return;
        }

        // Converting the quantity to a number and checking its validity.
        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException ex) {
            editQuantity.setError("Enter a valid quantity.");
            editQuantity.requestFocus();

            return;
        }

        // Checking and determining whether the user selected a measurement unit
        if (spinUnit.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a measurement unit.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Checking and determining whether the user selected an expiry date.
        if (btnExpiryDate.getText().toString().equals("Select Expiry Date")) {
            Toast.makeText(
                    this,
                    "Please select an expiry date",
                    Toast.LENGTH_SHORT
            ).show();

            // Stopping the ingredient from being saved.
            return;
        }

        // Rejecting any values that are either zero, negative, infinite and non-values.
        if (!Double.isFinite(quantity) || quantity <= 0) {
            editQuantity.setError("Quantity must be greater than zero.");
            editQuantity.requestFocus();

            return;
        }

        // Creating an instance of the database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Opening the database for writing
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Preparing the ingredient details for insertion.
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, ingredientName);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_UNIT, unit);

        // Saving the Expiry Date only when it has been selected.
        if (expiryDate.isEmpty()) {
            values.putNull(DatabaseHelper.COLUMN_EXPIRY_DATE);
        } else {
            values.put(DatabaseHelper.COLUMN_EXPIRY_DATE, expiryDate);
        }

        // Inserting the ingredient into the pantry table and returns the row ID.
        long result = db.insert(
                DatabaseHelper.TABLE_PANTRY,
                null,
                values
        );

        // Closing the database helper
        dbHelper.close();

        // Checking whether the ingredient was saved successfully by validating the row number.
        if (result != 1) {
            // Displaying a success message to the user
            Toast.makeText(
                    this,
                    "Ingredient Saved Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            // Returning to the Pantry Screen
            finish();
        } else {
            // Informing the user that the ingredients couldn't be saved.
            Toast.makeText(
                    this,
                    "Failed to Save Ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
