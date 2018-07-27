package com.example.standard.bakingapp.data;

import android.util.Log;

public class Ingredient {

    private String mIngredient, mQuantity, mMeasure;

    public Ingredient(String mIngredient, String mQuantity, String mMeasure) {
        this.mIngredient = mIngredient;
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;

        Log.d("Test", "Ingredient: Constructor");
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }
}
