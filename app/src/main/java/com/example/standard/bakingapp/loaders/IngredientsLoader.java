package com.example.standard.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.standard.bakingapp.data.Ingredient;
import com.example.standard.bakingapp.data.IngredientsUtils;

import java.util.List;

public class IngredientsLoader extends AsyncTaskLoader<List<Ingredient>> {

    private String mUrl;
    private int mRecipeId;

    public IngredientsLoader(Context context, String mUrl, int mRecipeId) {
        super(context);

        Log.d("Test", "IngredientsLoader: Constructor");
        this.mUrl = mUrl;
        this.mRecipeId = mRecipeId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("Test", "IngredientsLoader: onStartLoading");
        forceLoad();
    }

    @Override
    public List<Ingredient> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.d("Test", "IngredientsLoader: loadInBackground");
        // Todo: Hier muss die Id auch mit rein, done
        List<Ingredient> ingredients = IngredientsUtils.fetchIngredientData(getContext(), mUrl, mRecipeId);
        return ingredients;
    }

}
