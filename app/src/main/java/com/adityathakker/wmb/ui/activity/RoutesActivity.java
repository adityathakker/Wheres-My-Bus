package com.adityathakker.wmb.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.adityathakker.wmb.utils.AppConst;
import com.adityathakker.wmb.database.DatabaseHelper;
import com.adityathakker.wmb.interfaces.IRoutesCalculationsCallback;
import com.adityathakker.wmb.R;
import com.adityathakker.wmb.async.RouteCalculationsAsync;
import com.adityathakker.wmb.adapters.RoutesRecyclerAdapter;
import com.adityathakker.wmb.utils.SimpleDividerItemDecoration;

import java.util.List;

public class RoutesActivity extends AppCompatActivity {
    private static final String TAG = RoutesActivity.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RoutesRecyclerAdapter routesRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("List Of Routes");
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        int busStopSourceId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_SOURCE_ID,-1);
        int busStopDestinationId = intent.getIntExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_DESTINATION_ID,-1);

        if(busStopSourceId == -1 || busStopDestinationId == -1){
            Toast.makeText(RoutesActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.content_routes_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Calculating Routes...");
        progressDialog.show();
        RouteCalculationsAsync routeCalculationsAsync = new RouteCalculationsAsync(databaseHelper, busStopSourceId, busStopDestinationId, new IRoutesCalculationsCallback() {
            @Override
            public void onRoutesCalculated(List<Object> allJourneys) {
                Log.d(TAG, "onRoutesCalculated: Routes are calculated");
                routesRecyclerAdapter = new RoutesRecyclerAdapter(RoutesActivity.this, allJourneys);
                recyclerView.setAdapter(routesRecyclerAdapter);
                progressDialog.dismiss();
            }
        });
        routeCalculationsAsync.execute();







    }

}
