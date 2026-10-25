package com.example.smartpantrymanager;

// Importing libraries to utilize in Database.
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

// Class responsible for creating and managing the SQLite Database.
public class DatabaseHelper extends SQLiteOpenHelper {
    // Declaring the database name and version
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Declaring the pantry table name
    public static final String TABLE_PANTRY = "pantry_items";

    // Declaring the column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // Constant SQL Statement used to create the Pantry Table upon creation or loading of the Database.
    private static final String CREATE_PANTRY_TABLE =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY_DATE + " TEXT" +
                    ")";

    // Constructor used to initialize the database helper.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method which is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the Pantry Table.
        db.execSQL(CREATE_PANTRY_TABLE);
    }

    // Method called when the database version is increased.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Method used to retrieve all ingredients from the database.
    public List<PantryItem> getAllPantryItems() {
        // Creating a list to store the retrieved ingredients.
        List<PantryItem> pantryItems = new ArrayList<>();

        // Opening the database for reading
        SQLiteDatabase db = getReadableDatabase();

        // Querying the pantry table and retrieving all columns.
        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        // Reading each row returned by the query.
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );

                // Creating a PantryItem object from the database row
                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                // Adding the ingredient to the list.
                pantryItems.add(item);
            }
        } finally {
            // Closing the cursor after reading the results.
            cursor.close();
        }

        // Returning the retrieved ingredients.
        return pantryItems;
    }

    // Updating an existing pantry ingredient using its database ID
    public int updatePantryItem(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);

        if (expiryDate == null || expiryDate.isEmpty()) {
            values.putNull(COLUMN_EXPIRY_DATE);
        } else {
            values.put(COLUMN_EXPIRY_DATE, expiryDate);
        }

        // Updating only the pantry item matching the supplied ID.
        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID +  " = ?",
                new String[]{String.valueOf(id)}
        );
    };

    // Deleting an ingredient from the Pantry by using its database ID.
    public int deletePantryItem(int id) {
        // Opening the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // Deleting only the ingredient matching the supplied ID.
        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
