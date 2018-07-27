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

public class RecipeUtils extends Network {

    private static final String LOG_TAG = RecipeUtils.class.getName();

    public RecipeUtils(){
        Log.d("Test", "RecipeUtils: Constructor");
    }

    /**
     * Query the USGS dataset and return a list of {@link Recipe} objects.
     */
    public static List<Recipe> fetchRecipeData (Context context, String requestUrl){
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
        List<Recipe> recipes = extractFeatureFromJson(context, jsonResponse);

        // Return the list of {@link Book}s
        return recipes;
    }

    /**
     * Return a list of {@link Recipe} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Recipe> extractFeatureFromJson (Context context, String recipeJSON){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recipeJSON)){
            return null;
        }

        // Create an empty ArrayList that i can start adding recipes
        List<Recipe> recipes = new ArrayList<>();

        try {
            String name = "";
            int recipeId = 0;

            JSONArray baseJsonResponseArray = new JSONArray(recipeJSON);
            /*
             * Each item of the baseJsonResponseArray persists in
             * an id, a name, an array of ingredients and an array of steps
             * each array of ingredients includes the attributes quantity, measure and ingredient
             * each array of steps includes the attributes id, shortDescription, description, videoURL and thumbnailUrl
             * */
            for (int i = 0; i < baseJsonResponseArray.length(); i++){
                JSONObject currentRecipe = baseJsonResponseArray.getJSONObject(i);


                if (currentRecipe.has("name"))
                    name = currentRecipe.getString("name");

                if (currentRecipe.has("id")){
                    recipeId = currentRecipe.getInt("id");
                }

                Recipe recipeName = new Recipe(name, recipeId);
                recipes.add(recipeName);
            }

        } catch (JSONException e) {
            /*
             * If an error is thrown when executing any of the above statements in the "try" block,
             * catch the exception here, so the app doesn't crash. Print a log message
             * with the message from the exception.
             */
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_three), e);
        }
        return recipes;
    }
}

