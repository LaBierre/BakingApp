package com.example.standard.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.data.Step;
import com.example.standard.bakingapp.fragments.DetailFragment;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private List<Step> stepItems;
    private boolean mIsLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsLandscape = getResources().getBoolean(R.bool.landscape);
        if (mIsLandscape){
            // Set window fullscreen and remove title bar, and hide the actionbar in landscape mode
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        setContentView(R.layout.activity_detail);

        Log.d("Test", "DetailActivity: onCreate");



//        Bundle extras = getIntent().getExtras();
//
//        stepItems = new ArrayList<>();
//        stepItems = extras.getParcelableArrayList("stepItems");

        DetailFragment detailFragment = new DetailFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.phone_detail_container, detailFragment).commit();
    }
}
