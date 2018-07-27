package com.example.standard.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.data.Recipe;
import com.example.standard.bakingapp.fragments.DetailFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity{

    private Recipe recipeData, id;
    private List<Recipe> recipeItems;

    private String recipeName;
    private int recipeId;
    private DetailFragment detailFragment;

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Receive data from Start Activity

//        Intent intent = getIntent();
//        Parcelable extras = intent.getParcelableExtra("data");
//
//        if (extras != null){
//            recipeData = getIntent().getParcelableExtra("data");
//        } else {
//            Toast.makeText(this, "No data transfered", Toast.LENGTH_SHORT).show();
//        }
//        recipeName = recipeData.getmName();
//        recipeId = recipeData.getmId();
        // send the recipeId to RecipeFragment
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", recipeId);
        Log.d(LOG_TAG, "RecipeActivity: onCreate - vor setContentView");

        setContentView(R.layout.activity_recipe);

//        RecipeFragment fragment = new RecipeFragment();
//        fragment.setArguments(getIntent().getExtras());
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();

        Log.d(LOG_TAG, "RecipeActivity: onCreate");

        recipeItems = new ArrayList<>();

        /*
         *  Checken ob Phone oder Tablet vorliegt
         * */


        Log.d(LOG_TAG, "RecipeActivity: Ich bin kein Tablet");
    }

//    @Override
//    public void onRecipeSelected(int recipeId) {
//
//    }
}
