package com.example.standard.bakingapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.activities.DetailActivity;
import com.example.standard.bakingapp.adapters.IngredientsAdapter;
import com.example.standard.bakingapp.adapters.StepsAdapter;
import com.example.standard.bakingapp.data.Ingredient;
import com.example.standard.bakingapp.data.Recipe;
import com.example.standard.bakingapp.data.Step;
import com.example.standard.bakingapp.loaders.IngredientsLoader;
import com.example.standard.bakingapp.loaders.StepsLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

    // Todo: implement 2 LoaderCallbacks
    // One Callback is for ingredients and the other for the steps

    private static final String LOG_TAG = RecipeFragment.class.getName();

    private String mUrl;

    private Recipe recipe;

    // Todo: need 2 Adapters and two recyclerviews, done

    //Todo: perhaps implement a progressbar for each recyclerview?

    private static final int LOADER_ID_INGREDIENTS = 0;
    private static final int LOADER_ID_STEPS = 1;

    //private OnRecipeClickListener mCallback;

    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    private RecyclerView mRvIngredients, mRvSteps;
    private ProgressBar progressBar;

    private List<Ingredient> mIngredientItems;
    private List<Step> mStepItems;
    private List<String> mSteps;

    public int recipeId;

    LoaderManager loaderIngredient, loaderStep;

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

//    // Override onAttach to make sure that the container activity has implemented the callback
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        // This makes sure that the host activity has implemented the callback interface
//        // If not, it throws an exception
//        try {
//            mCallback = (OnRecipeClickListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnImageClickListener");
//        }
//    }

    public LoaderManager.LoaderCallbacks<List<Ingredient>> ingredientLoader =
            new LoaderManager.LoaderCallbacks<List<Ingredient>>() {
                @Override
                public Loader<List<Ingredient>> onCreateLoader(int id, Bundle args) {
                    // Todo: Hier und im step loader muss die Id mit rein, done
                    Log.d(LOG_TAG, "Recipe Id = " + recipeId);
                    return new IngredientsLoader(getContext(), mUrl, recipeId);
                }

                @Override
                public void onLoadFinished(Loader<List<Ingredient>> loader, List<Ingredient> ingredients) {
                    progressBar.setVisibility(View.GONE);

                    if (ingredients != null && !ingredients.isEmpty()) {
                        mIngredientsAdapter.add(ingredients);
                        mIngredientsAdapter.notifyDataSetChanged();
                    } else {
                        mRvIngredients.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Ingredient>> loader) {
                    mIngredientsAdapter.clear();
                }
            };

    public LoaderManager.LoaderCallbacks<List<Step>> stepLoader =
            new LoaderManager.LoaderCallbacks<List<Step>>() {
                @Override
                public Loader<List<Step>> onCreateLoader(int id, Bundle args) {
                    return new StepsLoader(getContext(), mUrl, recipeId);
                }

                @Override
                public void onLoadFinished(Loader<List<Step>> loader, List<Step> steps) {
                    progressBar.setVisibility(View.GONE);

                    if (steps != null && !steps.isEmpty()) {
                        mStepsAdapter.add(steps);
                        mStepsAdapter.notifyDataSetChanged();
                    } else {
                        mRvSteps.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getString(R.string.toast_message), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Step>> loader) {
                    mStepsAdapter.clear();
                }
            };

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipe = new Recipe();

        Bundle bundle = getActivity().getIntent().getExtras();
        recipe = bundle.getParcelable("data");
        recipeId = recipe.getmId();
        Log.d("Test", "RecipeFragment recipeId = " + recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        //Bundle bundle = getActivity().getIntent().getExtras().getParcelable("data");
        //recipeId = bundle.getI;


        //String text = getArguments().getString("test");

        //Log.d(LOG_TAG, "onCreateView Test = " + text);

        progressBar = (ProgressBar) rootView.findViewById(R.id.load_indicator_recipe);

        mRvIngredients = (RecyclerView) rootView.findViewById(R.id.rv_ingredients);
        mRvSteps = (RecyclerView) rootView.findViewById(R.id.rv_steps);

        mIngredientItems = new ArrayList<>();
        mStepItems = new ArrayList<>();

        mUrl = getString(R.string.url);

        ingredients();
        steps();

        return rootView;
    }

    public void ingredients(){

        Log.d(LOG_TAG, "ingredients");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvIngredients.setHasFixedSize(true);
        mRvIngredients.setLayoutManager(layoutManager);

        mIngredientsAdapter = new IngredientsAdapter(getContext(), mIngredientItems);

        mRvIngredients.setAdapter(mIngredientsAdapter);

        loaderIngredient = getLoaderManager();
        loaderIngredient.initLoader(LOADER_ID_INGREDIENTS, null, ingredientLoader);
    }

    public void steps(){

        Log.d(LOG_TAG, "steps");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvSteps.setHasFixedSize(true);
        mRvSteps.setLayoutManager(layoutManager);

        mStepsAdapter = new StepsAdapter(getContext(), this, mStepItems);

        Log.d(LOG_TAG, "RecipeFragment steps mStepItems" + mStepItems.size());

        for (int i=0; i<mStepItems.size(); i++){
            Log.d(LOG_TAG, "RecipeFragment steps mStepItems" + mStepItems.get(i));
        }

        mRvSteps.setAdapter(mStepsAdapter);

        loaderStep = getLoaderManager();
        loaderStep.initLoader(LOADER_ID_STEPS, null, stepLoader);
    }

    @Override
    public void onClick(Step data) {
        Log.d("Test", "RecipeActivity: onClick");

        //RecipeActivity recipeActivity = new RecipeActivity();

        int index = data.getmIndex();

        Log.d(LOG_TAG, "RecipeFragment mStepItems index = " + index);

        if (getActivity().findViewById(R.id.fragment_tablet_layout) != null){

            Log.d(LOG_TAG, "RecipeActivity: Ich bin ein Tablet");

            // Todo: Die Daten müssen direkt an das Deailfragment gesendet werden

            Bundle bundle = new Bundle();
            bundle.putParcelable("step", data);
            bundle.putParcelableArrayList("bundle", (ArrayList<? extends Parcelable>) mStepItems);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tablet_detail_container, detailFragment).commit();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            Log.d(LOG_TAG, "RecipeActivity: Ich bin ein Phone");
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("bundle", (ArrayList<? extends Parcelable>) mStepItems);
            intent.putExtra("step", data);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

//    B b = new B();
//((YourActivity)getActivity).setnewFragment(b,true);
//
//    public void setNewFragment(Fragment fragment,boolean addbackstack) {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment_content, fragment);
//        if (addbackstack)
//            transaction.addToBackStack(title);
//        transaction.commit();
//    }
}
