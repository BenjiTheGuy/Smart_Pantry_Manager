package com.example.smartpantrymanager;

// Importing the required Android classes and libraries
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Importing the RecyclerView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Importing collections libraries
import java.util.List;

// Adapter responsible for displaying pantry ingredients.
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // List containing the pantry ingredients.
    private final List<PantryItem> pantryItems;

    // Constructor receives the ingredients to display, to edit and to delete.
    public PantryAdapter(List<PantryItem> pantryItems,
                         OnItemClickListener listener,
                         OnItemDeleteListener deleteListener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    // Creating a ViewHolder for an ingredient row.
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {
        // Loading the ingredient row layout.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        // Returning a ViewHolder containing the row.
        return new PantryViewHolder(view);
    }

    // Connecting ingredient data to the row's TextViews
    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {
        // Getting the ingredient for this row.
        PantryItem item = pantryItems.get(position);

        // Displaying the ingredient name
        holder.txtName.setText(item.getName());

        // Displaying the quantity and measurement unit
        holder.txtQuantity.setText(
                item.getQuantity() + " " + item.getUnit()
        );

        // Displaying the expiry date when available
        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            holder.txtExpiry.setVisibility(View.GONE);
        } else {
            holder.txtExpiry.setVisibility(View.VISIBLE);
            holder.txtExpiry.setText(
                    "Expiry Date: " + item.getExpiryDate()
            );
        }

        // Opening the selected ingredient by redirecting to Add/Edit Ingredient screen when the row
        // is clicked.
        holder.itemView.setOnClickListener(v ->
            listener.onItemClick(pantryItems.get(position))
        );

        // Allowing for the user to long press an ingredient in order to delete it.
        holder.itemView.setOnLongClickListener(v -> {
            deleteListener.onItemDelete(pantryItems.get(position));

            return true;
        });
    }

    // Returning the number of ingredients in the list.
    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    // ViewHolder stores references to the row's interface's components.
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity, txtExpiry;

        // Linking the row's TextViews to their Java variables.
        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_ingredient_name);

            txtQuantity = itemView.findViewById(R.id.item_ingredient_quantity);

            txtExpiry = itemView.findViewById(R.id.item_ingredient_expiry);
        }
    }

    // Defining what happens when a pantry ingredient is selected.
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    // Defining the action to perform when an ingredient is deleted.
    public interface OnItemDeleteListener {
        void onItemDelete(PantryItem item);
    }

    // Storing the action to perform when an ingredient is clicked.
    private final OnItemClickListener listener;

    // Storing the action to perform when an ingredient is deleted.
    private final OnItemDeleteListener deleteListener;
}