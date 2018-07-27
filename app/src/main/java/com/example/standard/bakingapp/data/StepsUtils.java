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

public class StepsUtils extends Network {

    // Todo: die gleichen Schritte wie in IngredientsUtils ausführen

    private static final String LOG_TAG = StepsUtils.class.getName();

    public StepsUtils() {
        Log.d("Test", "StepsUtils: Constructor");
    }

    /**
     * Query the USGS dataset and return a list of {@link Step} objects.
     */
    public static List<Step> fetchStepData(Context context, String requestUrl, int recipeId) {
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
        List<Step> steps = extractFeatureFromJson(context, jsonResponse, recipeId);

        // Return the list of {@link Book}s
        return steps;
    }

    /**
     * Return a list of {@link Step} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Step> extractFeatureFromJson(Context context, String stepJSON, int recipeId) {
        // If the JSON string is empty or null, then return early.
        int id = recipeId - 1;
        // Todo: Use RecipeId um gezielt die Zubereitungsschritte auszuwählen
        if (TextUtils.isEmpty(stepJSON)) {
            return null;
        }

        // Create an empty ArrayList that i can start adding recipes
        List<Step> steps = new ArrayList<>();

        try {
            int index = 0;
            String shortDescription = "";
            String description = "";
            String videoURL = "";
            String thumbnailURL = "";

            JSONArray baseJsonResponseArray = new JSONArray(stepJSON);

            Log.d(LOG_TAG, "Base lengt = " + baseJsonResponseArray.length());
            /*
             * Each item of the baseJsonResponseArray persists in
             * an id, a name, an array of ingredients and an array of steps
             * each array of ingredients includes the attributes quantity, measure and ingredient
             * each array of steps includes the attributes id, shortDescription, description, videoURL and thumbnailUrl
             * */

            JSONObject currentRecipe = baseJsonResponseArray.getJSONObject(id);


            // And now the array "steps"--------------------------------------------------------
            JSONArray stepsArray = currentRecipe.getJSONArray("steps");
            // Objects and attributes within the array "steps"
            for (int k = 0; k < stepsArray.length(); k++) {
                JSONObject currentStep = stepsArray.getJSONObject(k);

                if (currentStep.has("id"))
                    index = currentStep.getInt("id");
                if (currentStep.has("shortDescription"))
                    shortDescription = currentStep.getString("shortDescription");
                if (currentStep.has("description"))
                    description = currentStep.getString("description");
                if (currentStep.has("videoURL") && !currentStep.getString("videoURL").equals("")) {
                    videoURL = currentStep.getString("videoURL");
                    Log.d(LOG_TAG, "VideoUrl");
                } else if (currentStep.has("thumbnailURL") && !currentStep.getString("thumbnailURL").equals("")){
                    thumbnailURL = currentStep.getString("thumbnailURL");
                    Log.d(LOG_TAG, "ThumbnailUrl");
                }

                Step newStep = new Step(index, shortDescription, description, videoURL, thumbnailURL);
                steps.add(newStep);
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
        return steps;
    }

}
