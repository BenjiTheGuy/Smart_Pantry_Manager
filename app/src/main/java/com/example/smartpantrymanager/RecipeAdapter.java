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

// Adapter responsible for displaying recipes in a RecyclerView.
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    // List containing the recipes retrieved from the database.
    private final List<Recipe> recipes;

    // Constructor receives the list of recipes to display.
    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    // Creating a ViewHolder for each recipe row.
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {
        // Loading the recipe row layout.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        // Returning a ViewHolder containing the row.
        return new RecipeViewHolder(view);
    }

    // Connecting a recipe's data to the row's TextViews
    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {
        // Getting the recipe for this row.
        Recipe recipe = recipes.get(position);

        // Displaying the recipe name
        holder.txtRecipeName.setText(recipe.getName());

        // Displaying the instructions
        holder.txtRecipeInstructions.setText(recipe.getInstructions());

        // Opening the selected recipe when its card is tapped.
        holder.itemView.setOnClickListener( v -> listener.onRecipeClick(recipe));
    }

    // Returning the number of recipes in the list.
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // ViewHolder stores references to the row's interface's components.
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView txtRecipeName, txtRecipeInstructions;

        // Linking the row's TextViews to their Java variables.
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRecipeName = itemView.findViewById(R.id.item_recipe_name);

            txtRecipeInstructions = itemView.findViewById(R.id.item_recipe_instructions);
        }
    }

    // Listener used when the user selects a recipe.
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final OnRecipeClickListener listener;
}
