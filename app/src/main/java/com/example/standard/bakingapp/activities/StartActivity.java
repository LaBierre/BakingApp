package com.example.standard.bakingapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.adapters.RecipeAdapter;
import com.example.standard.bakingapp.data.Recipe;
import com.example.standard.bakingapp.loaders.RecipeLoader;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.RecipeAdapterOnClickHandler{
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecipeAdapter mAdapter;
    private String mUrl;
    private List<Recipe> recipeItems;
    private boolean mDetailedLayout, mIsTablet, mIsLandscape;
    private int recipeId;

    private static final String LOG_TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        Log.d("Test", "StartActivity: onCreate");

        /*
         * mDetailedLayout = false means that the home button in RecipeActivity is not clicked
         * mDetailedLayout = true means that the home button in RecipeActivity is clicked
         */
        mDetailedLayout = false;

        mIsLandscape = getResources().getBoolean(R.bool.landscape);

        // Check Internet Access
        checkConnection();

        // Todo: setTitle

        mUrl = getString(R.string.url);

        progressBar = (ProgressBar) findViewById(R.id.load_indicator_start);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //recyclerView.setHasFixedSize(true);

        recipeItems = new ArrayList<>();

        // Check if the device is a tablet or a phone
        if (findViewById(R.id.tablet_layout)!=null ||
                (findViewById(R.id.tablet_layout)==null && mIsLandscape)){
            Log.d("Test", "StartActivity: Device is a Tablet!");
            mIsTablet = true;
            int numberOfColumns = getResources().getInteger(R.integer.gallery_columns);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Log.d("Test", "StartActivity: Device is a Phone!");
            mIsTablet = false;

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        mAdapter = new RecipeAdapter(this, this, recipeItems);

        recyclerView.setAdapter(mAdapter);

        LoaderManager loader = getSupportLoaderManager();
        loader.initLoader(0, null, this);
    }

    /*
     * This method checks the Internet Connectivity and produce an alert if there is no connection
     */
    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean connectivity = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!connectivity) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_title))
                    .setMessage(getString(R.string.alert_message))
                    .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent i = getBaseContext().getPackageManager().
                                    getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    })
                    .setIcon(R.drawable.internet_connection)
                    .show();
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        Log.d("Test", "StartActivity: onCreateLoader");
        mDetailedLayout = false;
        return new RecipeLoader(getApplicationContext(), mUrl);
        // hier wird der entsprechende Loader aufgerufen
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        Log.d("Test", "StartActivity: onLoadFinished");
        progressBar.setVisibility(View.GONE);

        /*
         * This if request checks if the home button in the detail activity is clicked and avoids
         * that the lastly loaded page with movies will be added again in the list
         */
        if (!mDetailedLayout){
            if (recipes != null && !recipes.isEmpty()) {
                mAdapter.add(recipes);
                mAdapter.notifyDataSetChanged();

            } else {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        }
        mDetailedLayout = true;
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        Log.d("Test", "StartActivity: onLoaderReset");
        mAdapter.clear();
    }

    @Override
    public void onClick(Recipe data) {
        Log.d("Test", "StartActivity: onClick");

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }
}
