package com.adityathakker.wmb.async;

import android.os.AsyncTask;
import android.util.Log;

import com.adityathakker.wmb.database.DatabaseHelper;
import com.adityathakker.wmb.interfaces.IRoutesCalculationsCallback;
import com.adityathakker.wmb.model.MultiJourneyModel;
import com.adityathakker.wmb.model.SingleJourneyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by adityajthakker on 17/8/16.
 */
public class RouteCalculationsAsync extends AsyncTask<Void, Void, List<Object>>{
    private static final String TAG = RouteCalculationsAsync.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private int sourceId, destinationId;
    private IRoutesCalculationsCallback routesCallback;
    public RouteCalculationsAsync(DatabaseHelper databaseHelper, int sourceId, int destinationId, IRoutesCalculationsCallback routesCallback){
        this.databaseHelper = databaseHelper;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.routesCallback = routesCallback;
    }
    @Override
    protected List<Object> doInBackground(Void... voids) {

        List<SingleJourneyModel> singleJourneyModelList = databaseHelper.getSingleBusJourneys(sourceId, destinationId);
        StringBuilder busNumbersSingleJourney = new StringBuilder();
        String busNumbers = "";
        if(singleJourneyModelList != null){
            for(SingleJourneyModel singleJourneyModel: singleJourneyModelList){
                Log.d(TAG, "doInBackground: Bus Numbers: " + singleJourneyModel.getBusNumber());
                busNumbersSingleJourney.append("\'" + singleJourneyModel.getBusNumber() + "\',");
            }
            busNumbers = busNumbersSingleJourney.toString();
            busNumbers = busNumbers.substring(0,busNumbers.length()-1);
            Log.d(TAG, "doInBackground: Bus Numbers String: " + busNumbersSingleJourney);
        }
        List<MultiJourneyModel> multiJourneyModelList = databaseHelper.getMultiBusJourneys(sourceId, destinationId, busNumbers);

        List<Object> allJourneys = new ArrayList<>();
        if(singleJourneyModelList != null){
            Collections.sort(singleJourneyModelList, new Comparator<SingleJourneyModel>() {
                @Override
                public int compare(SingleJourneyModel self, SingleJourneyModel other) {
                    return self.getStops()-other.getStops();
                }
            });
            for(SingleJourneyModel singleJourneyModel: singleJourneyModelList){
                Log.d(TAG, "Single Journey: " + singleJourneyModel.toString());
                allJourneys.add(singleJourneyModel);
            }
        }
        if(multiJourneyModelList != null){
            Collections.sort(multiJourneyModelList, new Comparator<MultiJourneyModel>() {
                @Override
                public int compare(MultiJourneyModel self, MultiJourneyModel other) {
                    return self.getStops()-other.getStops();
                }
            });
            for(MultiJourneyModel multiJourneyModel: multiJourneyModelList){
                Log.d(TAG, "Multi Journey: " + multiJourneyModel.toString());
                allJourneys.add(multiJourneyModel);
            }
        }

        return allJourneys;
    }

    @Override
    protected void onPostExecute(List<Object> allJourneys) {
        super.onPostExecute(allJourneys);
        routesCallback.onRoutesCalculated(allJourneys);
    }
}
