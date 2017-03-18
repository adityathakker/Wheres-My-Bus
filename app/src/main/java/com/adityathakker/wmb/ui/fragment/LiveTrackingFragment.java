package com.adityathakker.wmb.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adityathakker.wmb.R;
import com.adityathakker.wmb.database.DatabaseHelper;
import com.adityathakker.wmb.interfaces.APICalls;
import com.adityathakker.wmb.model.BusStopsModel;
import com.adityathakker.wmb.model.LocationUpdatesPojo;
import com.adityathakker.wmb.model.UpdatesPojo;
import com.adityathakker.wmb.utils.AppConst;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adityajthakker on 23/8/16.
 */
public class LiveTrackingFragment extends Fragment {

    private static final String TAG = LiveTrackingFragment.class.getSimpleName();
    private boolean isMapLoaded = false;
    private MapView mapView;
    private String busNumber;
    private int busStopId;
    private DatabaseHelper databaseHelper;
    private Context context;
    private GraphicsLayer busStopsLayer;
    private GraphicsLayer busLocationsLayer;
    private Retrofit retrofit;
    private APICalls apiCalls;
    private Handler h;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_live_tracking, container, false);
        context = getContext();
        databaseHelper = new DatabaseHelper(context);


        Bundle details = getArguments();
        busStopId = details.getInt(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_STOP_ID);
        busNumber = details.getString(AppConst.Codes.INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER);

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.100/wmb/").addConverterFactory(GsonConverterFactory.create()).build();
        apiCalls = retrofit.create(APICalls.class);

        mapView = (MapView) layout.findViewById(R.id.map_view);
        busStopsLayer = new GraphicsLayer();
        busLocationsLayer = new GraphicsLayer();
        mapView.addLayer(busStopsLayer);
        mapView.addLayer(busLocationsLayer);

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mapView) && (status == STATUS.INITIALIZED)) {
                    isMapLoaded = true;
                    setupMap();
                    updateMap();

                    h = new Handler();
                    final int delay = 5000; //milliseconds

                    h.postDelayed(new Runnable(){
                        public void run(){
                            updateMap();
                            h.postDelayed(this, delay);
                        }
                    }, delay);
                }
            }
        });
        return layout;
    }

    private void setupMap() {
        List<BusStopsModel> busStopsList = databaseHelper.getBusStopsByBusNumber(busNumber);
        for(BusStopsModel busStop: busStopsList){
            Point busStopPoint = new Point(Double.parseDouble(busStop.getLongitude()), Double.parseDouble(busStop.getLatitude()));
            Point geometricBusStopPoint = (Point) GeometryEngine.project(busStopPoint, SpatialReference.create(4326), mapView.getSpatialReference());
            SimpleMarkerSymbol busStopMarker = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.TRIANGLE);
            Graphic busStopGraphic = new Graphic(geometricBusStopPoint, busStopMarker);
            busStopsLayer.addGraphic(busStopGraphic);
        }
        BusStopsModel currentBusStop = databaseHelper.getBusStop(busStopId);
        Point busStopPoint = new Point(Double.parseDouble(currentBusStop.getLongitude()), Double.parseDouble(currentBusStop.getLatitude()));
        Point geometricBusStopPoint = (Point) GeometryEngine.project(busStopPoint, SpatialReference.create(4326), mapView.getSpatialReference());
        mapView.zoomToResolution(geometricBusStopPoint, 1.53f);

    }

    private void updateMap() {
        Call <UpdatesPojo> apiCallsLocationUpdates = apiCalls.getLocationUpdates(busNumber);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating Locations...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        apiCallsLocationUpdates.enqueue(new Callback<UpdatesPojo>() {
            @Override
            public void onResponse(Call<UpdatesPojo> call, Response<UpdatesPojo> response) {
                UpdatesPojo updatesPojo = response.body();
                if(updatesPojo.getStatus().equals(AppConst.STATUS.STATUS_SUCCESS)){
                    ArrayList<LocationUpdatesPojo> locationUpdatesPojos = updatesPojo.getLocations();
                    if(busLocationsLayer != null){
                        mapView.removeLayer(busLocationsLayer);
                    }
                    busLocationsLayer = new GraphicsLayer();
                    mapView.addLayer(busLocationsLayer);
                    for(LocationUpdatesPojo locationUpdatesPojo: locationUpdatesPojos){
                        Point busLocation = new Point(Double.parseDouble(locationUpdatesPojo.getLongitude()), Double.parseDouble(locationUpdatesPojo.getLatitude()));
                        Point geometricBusPoint = (Point) GeometryEngine.project(busLocation, SpatialReference.create(4326), mapView.getSpatialReference());
                        SimpleMarkerSymbol busMarker = new SimpleMarkerSymbol(Color.BLUE, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
                        Graphic busGraphic = new Graphic(geometricBusPoint, busMarker);
                        busLocationsLayer.addGraphic(busGraphic);
                    }
                }else{
                    Toast.makeText(context, updatesPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Failure Message: " + updatesPojo.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdatesPojo> call, Throwable t) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage(), t);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_live_tracking, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                updateMap();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        h.removeCallbacksAndMessages(null);
    }
}
