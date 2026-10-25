package com.example.smartpantrymanager;

// Importing necessary libraries to implement class.
import android.os.Bundle;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

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

    // Database helper used to save or update pantry ingredients
    private DatabaseHelper databaseHelper;

    // Storing the selected expiry date.
    private String expiryDate = "";

    // Storing the ID of the ingredient being edited.
    // A value of -1 means a new ingredient is being added.
    private int ingredientId = -1;

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

        // Initializing the database helper
        databaseHelper = new DatabaseHelper(this);

        // Checking whether an existing ingredient ID was passed to this screen.
        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != - 1) {
            // Existing ingredient method used to load its details into the form.
            loadIngredientForEditing();
        }

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

        // Creating an instance of the database helper for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

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

        long result;

        if (ingredientId == -1) {
            // Inserting the ingredient into the pantry table and returns the row ID.
            result = db.insert(
                    DatabaseHelper.TABLE_PANTRY,
                    null,
                    values
            );
        } else {
            // Editing option used to update the existing pantry ingredient.
            result = databaseHelper.updatePantryItem(
                    ingredientId,
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );
        }

        boolean success = ingredientId == -1 ? result != -1 : result > 0;

        if (success) {
            Toast.makeText(
                    this,
                    ingredientId == -1
                            ? "Ingredient Saved Successfully"
                            : "Ingredient Updated Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            // Returning to the Pantry screen.
            finish();
        } else {
            Toast.makeText(
                    this,
                    ingredientId == -1
                            ? "Failed to Save Ingredient"
                            : "Failed to Update Ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Loading the selected ingredient's saved details into the form.
    private void loadIngredientForEditing() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PANTRY,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ingredientId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Retrieving the saved ingredient details
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY)
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNIT)
            );

            String savedExpiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPIRY_DATE)
            );

            // Populating the form with the existing values.
            editIngredientName.setText(name);
            editQuantity.setText(String.valueOf(quantity));

            // Selecting the saved measurement unit
            for (int i = 0; i < spinUnit.getCount(); i++) {
                if (spinUnit.getItemAtPosition(i).toString().equals(unit)) {
                    spinUnit.setSelection(i);

                    break;
                }
            }

            // Restoring the expiry date if one exists
            if (savedExpiryDate != null && !savedExpiryDate.isEmpty()) {
                expiryDate = savedExpiryDate;

                btnExpiryDate.setText(savedExpiryDate);
            }
        }

        cursor.close();
    }
}
