package com.example.smartpantrymanager;

// Importing libraries to use in class.
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Providing reusable methods for checking pantry ingredient expiry dates.
public final class ExpiryDateHelper {
    // Matching the date format used by the Add/Edit Ingredient screen.
    private static final String DATE_FORMAT = "d/M/yyyy";

    // Preventing the class from being instantiated outside this class.
    private ExpiryDateHelper() {
    }

    // Converting an expiry date string into a Date object. Returning null if the date is missing or
    // invalid.
    public static Date parseExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        // Rejecting impossible dates which don't exist on the calendar.
        formatter.setLenient(false);

        ParsePosition position = new ParsePosition(0);
        Date parsedDate = formatter.parse(expiryDate.trim(), position);

        // Ensuring that the entire string was parsed successfully,
        if (parsedDate == null || position.getIndex() != expiryDate.trim().length()) {
            return null;
        }

        return parsedDate;
    }

    // Removing the time aspect of the date so any comparisons made between dates don't include the
    // time
    private static Calendar getDateOnly(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    // Returning true if the expiry date is before the current date. Any invalid dates are treated
    // as unsafe.
    public static boolean isExpired(String expiryDate) {
        Date parsedDate = parseExpiryDate(expiryDate);

        if (parsedDate == null) {
            return true;
        }

        Calendar expiry = getDateOnly(parsedDate);
        Calendar today = getDateOnly(new Date());

        return expiry.before(today);
    }

    // Returning true if the ingredient's expiry date is today or within the next 3 days.
    public static boolean isExpiringSoon(String expiryDate) {
        Date parsedDate = parseExpiryDate(expiryDate);

        if (parsedDate == null || isExpired(expiryDate)) {
            return false;
        }

        Calendar expiry = getDateOnly(parsedDate);
        Calendar today = getDateOnly(new Date());

        Calendar three_days = (Calendar) today.clone();

        three_days.add(Calendar.DAY_OF_YEAR, 3);

        return !expiry.after(three_days);
    }
}
