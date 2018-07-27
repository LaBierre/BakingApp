package com.example.standard.bakingapp.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.standard.bakingapp.Network;
import com.example.standard.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IngredientsUtils extends Network {

    private static final String LOG_TAG = IngredientsUtils.class.getName();

    public IngredientsUtils() {

        Log.d("Test", "IngredientsUtils: Constructor");
    }

    /**
     * Query the USGS dataset and return a list of {@link Ingredient} objects.
     */
    public static List<Ingredient> fetchIngredientData(Context context, String requestUrl, int recipeId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(context, requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(context, url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_http), e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Ingredient> ingredients = extractFeatureFromJson(context, jsonResponse, recipeId);

        // Return the list of {@link Book}s
        return ingredients;
    }

    /**
     * Return a list of {@link Ingredient} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Ingredient> extractFeatureFromJson(Context context, String ingredientJSON, int recipeId) {

        int id = recipeId - 1;
        Log.d(LOG_TAG, "RecipeId = " + id);
        // Todo: Use RecipeId um gezielt die Zutaten auszuwählen, done

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(ingredientJSON)) {
            return null;
        }

        // Create an empty ArrayList that i can start adding recipes
        List<Ingredient> ingredients = new ArrayList<>();

        try {
            String ingredient = "";
            String quantity = "";
            String measure = "";

            // das ruft das ganze Array auf
            JSONArray baseJsonResponseArray = new JSONArray(ingredientJSON);
            /*
             * Each item of the baseJsonResponseArray persists in
             * an id, a name, an array of ingredients and an array of steps
             * each array of ingredients includes the attributes quantity, measure and ingredient
             * each array of steps includes the attributes id, shortDescription, description, videoURL and thumbnailUrl
             * */

            // Todo: Einsetzen von RecipeId in i, done
            JSONObject currentRecipe = baseJsonResponseArray.getJSONObject(id);

            // Now the array "ingredients"------------------------------------------------------
            JSONArray ingredientsArray = currentRecipe.getJSONArray("ingredients");

            // Objects and attributes within the array "ingredients"
            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject currentIngredient = ingredientsArray.getJSONObject(j);

                if (currentIngredient.has("ingredient"))
                    ingredient = currentIngredient.getString("ingredient");

                int quantityValue;
                if (currentIngredient.has("quantity")) {
                    quantityValue = currentIngredient.getInt("quantity");
                    quantity = String.valueOf(quantityValue);
                }
                if (currentIngredient.has("measure"))
                    measure = currentIngredient.getString("measure");

                Ingredient newIngredient = new Ingredient(ingredient, quantity, measure);
                ingredients.add(newIngredient);
            }
            // ---------------------------------------------------------------------------------

        } catch (JSONException e) {
            /*
             * If an error is thrown when executing any of the above statements in the "try" block,
             * catch the exception here, so the app doesn't crash. Print a log message
             * with the message from the exception.
             */
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_three), e);
        }
        return ingredients;
    }

}
