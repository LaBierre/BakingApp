package com.example.standard.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.standard.bakingapp.data.Recipe;
import com.example.standard.bakingapp.data.RecipeUtils;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private String mUrl;

    public RecipeLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
        Log.d("Test", "RecipeLoader: Constructor");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("Test", "RecipeLoader: onStartLoading");
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Hier kommt die Verknüpfung zum entsprechenden Daten-Utils
        Log.d("Test", "RecipeLoader: loadInBackground");
        List<Recipe> recipes = RecipeUtils.fetchRecipeData(getContext(), mUrl);
        return recipes;
    }

}
