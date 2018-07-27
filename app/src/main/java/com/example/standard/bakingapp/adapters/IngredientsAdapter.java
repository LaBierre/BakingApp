package com.example.standard.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.data.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    // Constructor

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;

        Log.d("Test", "IngredientsAdapter: Constructor");
    }

    public void clear(){
        ingredients.clear();
        notifyDataSetChanged();
    }

    public void add (List<Ingredient> ingredients){
        this.ingredients.addAll(ingredients);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("Test", "IngredientsAdapter: onCreateViewHolder");

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient ingredientItem = ingredients.get(position);

        Log.d("Test", "IngredientsAdapter: onBindViewHolder");

        String ingredientName = ingredientItem.getmIngredient();
        String quantity = ingredientItem.getmQuantity();
        String measure = ingredientItem.getmMeasure();

        holder.ingredientName.setText(ingredientName);
        holder.quantity.setText(quantity);
        holder.measure.setText(measure);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView ingredientName, quantity, measure;

        public ViewHolder(View itemView) {
            super(itemView);

            Log.d("Test", "IngredientsAdapter: ViewHolder");

            ingredientName = (TextView) itemView.findViewById(R.id.tv_ingredient);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            measure = (TextView) itemView.findViewById(R.id.tv_measure);
        }
    }

}
