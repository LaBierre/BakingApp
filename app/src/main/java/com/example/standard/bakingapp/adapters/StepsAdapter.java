package com.example.standard.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.data.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private final StepsAdapterOnClickHandler mClickHandler;
    private Context context;
    private List<Step> stepItems;

    public StepsAdapter(Context context, StepsAdapterOnClickHandler mClickHandler, List<Step> stepItems) {
        this.mClickHandler = mClickHandler;
        this.context = context;
        this.stepItems = stepItems;
    }

    public void clear(){
        stepItems.clear();
        notifyDataSetChanged();
    }

    public void add(List<Step> stepItems) {
        Log.d("Test", "StepsAdapter: add");
        this.stepItems.addAll(stepItems);
        //this.notifyItemRangeInserted(0, recipeItems.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step stepItem = stepItems.get(position);

        String shortDescription = stepItem.getmShortDescription();

        holder.preparationStep.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        return stepItems.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface StepsAdapterOnClickHandler {
        void onClick(Step data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView preparationStep;

        public ViewHolder(View itemView) {
            super(itemView);
            preparationStep = (TextView) itemView.findViewById(R.id.tv_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Log.d("Test", "StepsAdapter: onClick");

            int index = stepItems.get(adapterPosition).getmIndex();
            String shortDescription = stepItems.get(adapterPosition).getmShortDescription();
            String description = stepItems.get(adapterPosition).getmDescription();
            String videoUrl = stepItems.get(adapterPosition).getmVideoURL();
            String thumbnailURL = stepItems.get(adapterPosition).getmThumbnailUrl();

            Step data = new Step(index, shortDescription, description, videoUrl, thumbnailURL);

            mClickHandler.onClick(data);
        }
    }

}
