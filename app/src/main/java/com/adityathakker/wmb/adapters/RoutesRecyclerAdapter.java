package com.adityathakker.wmb.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adityathakker.wmb.R;
import com.adityathakker.wmb.model.MultiJourneyModel;
import com.adityathakker.wmb.model.SingleJourneyModel;
import com.adityathakker.wmb.ui.activity.TrackingActivity;
import com.adityathakker.wmb.utils.AppConst;

import java.util.List;

/**
 * Created by adityajthakker on 17/8/16.
 */

public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = RoutesRecyclerAdapter.class.getSimpleName();
    private Context context;
    private List<Object> allJourneys;
    private final int VIEW_TYPE_SINGLE = 1;
    private final int VIEW_TYPE_MULTI = 2;
    public RoutesRecyclerAdapter(Context context, List<Object> allJourneys) {
        this.context = context;
        this.allJourneys = allJourneys;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SINGLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_single_row, parent, false);
            SingleRoutesViewHolder viewHolder = new SingleRoutesViewHolder(view);
            return viewHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_multi_row, parent, false);
            MultiRoutesViewHolder viewHolder = new MultiRoutesViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (allJourneys.get(position) instanceof SingleJourneyModel) {
            return VIEW_TYPE_SINGLE;
        } else if (allJourneys.get(position) instanceof MultiJourneyModel) {
            return VIEW_TYPE_MULTI;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SINGLE){
            SingleRoutesViewHolder singleRoutesViewHolder = (SingleRoutesViewHolder) holder;
            SingleJourneyModel singleJourneyModel = (SingleJourneyModel) allJourneys.get(position);
            if(singleJourneyModel != null){
                Log.d(TAG, "onBindViewHolder: Single Journey: " + singleJourneyModel.toString());
                singleRoutesViewHolder.busNumber.setText(singleJourneyModel.getBusNumber());
                singleRoutesViewHolder.sourceText.setText(singleJourneyModel.getSourceName());
                singleRoutesViewHolder.destinationText.setText(singleJourneyModel.getDestinationName());
                singleRoutesViewHolder.stops.setText(singleJourneyModel.getStops() + "");
            }else{
                Log.d(TAG, "onBindViewHolder: Single Journey is Null");
            }
        }else{
            MultiRoutesViewHolder multiRoutesViewHolder = (MultiRoutesViewHolder) holder;
            MultiJourneyModel multiJourneyModel = (MultiJourneyModel) allJourneys.get(position);
            if(multiJourneyModel != null){
                Log.d(TAG, "onBindViewHolder: Multi Journey: " + multiJourneyModel.toString());
                multiRoutesViewHolder.busNumberEnding.setText(multiJourneyModel.getEndingBusNumber());
                multiRoutesViewHolder.busNumberStarting.setText(multiJourneyModel.getStartingBusNumber());
                multiRoutesViewHolder.sourceText.setText(multiJourneyModel.getSourceName());
                multiRoutesViewHolder.destinationText.setText(multiJourneyModel.getDestinationName());
                multiRoutesViewHolder.changeAt.setText(multiJourneyModel.getMiddleName());
                multiRoutesViewHolder.stops.setText(multiJourneyModel.getStops() + "");
            }else{
                Log.d(TAG, "onBindViewHolder: Multi Journey is Null");
            }

        }
    }

    @Override
    public int getItemCount() {
        return allJourneys != null?allJourneys.size():0;
    }

    class SingleRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sourceText, destinationText, busNumber, stops;
        public SingleRoutesViewHolder(View itemView) {
            super(itemView);
            sourceText = (TextView) itemView.findViewById(R.id.routes_single_row_linear_layout_source_destination_source_text);
            destinationText = (TextView) itemView.findViewById(R.id.routes_single_row_linear_layout_source_destination_dest_text);
            busNumber = (TextView) itemView.findViewById(R.id.routes_single_row_textView_starting_bus);
            stops = (TextView) itemView.findViewById(R.id.routes_single_row_linear_layout_stops);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SingleJourneyModel singleJourneyModel = (SingleJourneyModel) allJourneys.get(getAdapterPosition());
            Intent intent = new Intent(context, TrackingActivity.class);
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_IS_SINGLE, true);
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_NAME, singleJourneyModel.getSourceName());
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_NAME, singleJourneyModel.getDestinationName());
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STARTING_ID, singleJourneyModel.getSourceId());
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_ENDING_ID, singleJourneyModel.getDestinationId());
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER, singleJourneyModel.getBusNumber());
            intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_STOPS, singleJourneyModel.getStops());
            context.startActivity(intent);
        }
    }

    class MultiRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sourceText, destinationText, busNumberStarting, busNumberEnding, changeAt, stops;
        public MultiRoutesViewHolder(View itemView) {
            super(itemView);
            sourceText = (TextView) itemView.findViewById(R.id.routes_multi_row_linear_layout_source_destination_source_text);
            destinationText = (TextView) itemView.findViewById(R.id.routes_multi_row_linear_layout_source_destination_dest_text);
            busNumberStarting = (TextView) itemView.findViewById(R.id.routes_multi_row_textView_starting_bus);
            busNumberEnding = (TextView) itemView.findViewById(R.id.routes_multi_row_textView_ending_bus);
            changeAt = (TextView) itemView.findViewById(R.id.routes_multi_row_textView_change_at);
            stops = (TextView) itemView.findViewById(R.id.routes_multi_row_linear_layout_stops);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
