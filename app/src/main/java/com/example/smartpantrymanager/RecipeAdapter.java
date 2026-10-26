package com.example.smartpantrymanager;

// Importing the required Android classes and libraries
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

// Importing the RecyclerView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Importing collections libraries
import java.util.List;

// Adapter responsible for displaying recipes in a RecyclerView.
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    // List containing the recipes retrieved from the database.
    private final List<Recipe> recipes;

    // Listener used to open a recipe when the user taps its card.
    private final OnRecipeClickListener listener;

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

        // Display the image associated with this recipe.
        int imageResource = getRecipeImage(recipe.getName());

        // Displaying the image in the recipe card.
        holder.imgRecipe.setImageResource(imageResource);

        // Opening the selected recipe when its card is tapped.
        holder.itemView.setOnClickListener( v -> listener.onRecipeClick(recipe));
    }

    // Returning the drawable resource associated with a recipe name.
    public static int getRecipeImage(String recipeName) {
        switch (recipeName) {
            case "Pancakes":
                return R.drawable.pancakes;

            case "Banana Pancakes":
                return R.drawable.banana_pancakes;

            case "Scrambled Eggs on Toast":
                return R.drawable.scrambled_eggs_on_toast;

            case "Cheese Pasta":
                return R.drawable.cheese_pasta;

            case "Cheese Toast":
                return R.drawable.cheese_toast;

            case "Full English Breakfast":
                return R.drawable.full_english_breakfast;

            case "Tomato Cheese Sandwich":
                return R.drawable.tomato_cheese_sandwich;

            case "Egg Fried Rice":
                return R.drawable.egg_fried_rice;

            case "Mince Pasta":
                return R.drawable.mince_pasta;

            case "Chicken Pasta":
                return R.drawable.chicken_pasta;

            case "Chicken Rice with Sweet Potatoes":
                return R.drawable.chicken_rice_with_sweet_potatoes;

            case "Egg Toast":
                return R.drawable.egg_toast;

            case "Basil Pesto":
                return R.drawable.basil_pesto;

            case "Beef Stew":
                return R.drawable.beef_stew;

            case "Roasted Chicken with Vegetables":
                return R.drawable.roasted_chicken_and_vegetables;

            default:
                // Temporary state if all file images aren't added.
                return R.drawable.pancakes;
        }
    }

    // Returning the number of recipes in the list.
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // ViewHolder stores references to the row's interface's components.
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView txtRecipeName, txtRecipeInstructions;
        ImageView imgRecipe;

        // Linking the row's TextViews to their Java variables.
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRecipeName = itemView.findViewById(R.id.item_recipe_name);
            txtRecipeInstructions = itemView.findViewById(R.id.item_recipe_instructions);
            imgRecipe = itemView.findViewById(R.id.item_recipe_image);
        }
    }

    // Listener used when the user selects a recipe.
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
}
