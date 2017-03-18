package com.adityathakker.wmb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adityathakker.wmb.R;
import com.adityathakker.wmb.model.BusStopsModel;

import java.util.List;

/**
 * Created by adityajthakker on 17/8/16.
 */

public class BusStopsRecyclerAdapter extends RecyclerView.Adapter<BusStopsRecyclerAdapter.BusStopsRecyclerViewHolder> {
    public static final String TAG = BusStopsRecyclerAdapter.class.getSimpleName();
    private Context context;
    private OnItemClickListener clickListener;
    private List<BusStopsModel> busStopsModelList;

    public BusStopsRecyclerAdapter(Context context, List<BusStopsModel> busStopsModelList) {
        this.context = context;
        this.busStopsModelList = busStopsModelList;
    }

    @Override
    public BusStopsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_stops_row, parent, false);
        BusStopsRecyclerViewHolder viewHolder = new BusStopsRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BusStopsRecyclerViewHolder holder, int position) {
        BusStopsModel busStopsModel = busStopsModelList.get(position);
        holder.busStopName.setText(busStopsModel.getName());
    }

    @Override
    public int getItemCount() {
        return busStopsModelList == null?0:busStopsModelList.size();
    }

    public void setFilter(List<BusStopsModel> filteredModelList) {
        busStopsModelList = filteredModelList;
        notifyDataSetChanged();
    }

    class BusStopsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView busStopName;
        public BusStopsRecyclerViewHolder(View itemView) {
            super(itemView);
            busStopName = (TextView) itemView.findViewById(R.id.bus_stops_row_textView_bus_stop_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition(), busStopsModelList.get(getAdapterPosition()));
        }
    }

    //Click Listener Interface
    public interface OnItemClickListener {
        public void onItemClick(View view, int position, BusStopsModel busStopsModel);
    }

    //Setting up the click listener
    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
