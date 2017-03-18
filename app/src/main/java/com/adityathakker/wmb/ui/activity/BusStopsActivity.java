package com.adityathakker.wmb.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.adityathakker.wmb.utils.AppConst;
import com.adityathakker.wmb.adapters.BusStopsRecyclerAdapter;
import com.adityathakker.wmb.database.DatabaseHelper;
import com.adityathakker.wmb.R;
import com.adityathakker.wmb.utils.SimpleDividerItemDecoration;
import com.adityathakker.wmb.model.BusStopsModel;

import java.util.ArrayList;
import java.util.List;

public class BusStopsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private List<BusStopsModel> allBusStops;
    private BusStopsRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stops);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select Bus Stop");
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this);

        allBusStops = databaseHelper.getAllBusStops();

        recyclerView = (RecyclerView) this.findViewById(R.id.activity_bus_stops_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        adapter = new BusStopsRecyclerAdapter(this, allBusStops);
        adapter.setOnItemClickListener(new BusStopsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, BusStopsModel busStopsModel) {
                Intent intent=new Intent();
                intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_ID,Integer.toString(busStopsModel.getId()));
                setResult(AppConst.Codes.RESULT_CODE_SELECT_BUS_STOP_SUCCESS,intent);
                finish();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_bus_stops, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<BusStopsModel> filteredModelList = filter(allBusStops, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }

            private List<BusStopsModel> filter(List<BusStopsModel> data, String newText) {
                newText = newText.toLowerCase();

                final List<BusStopsModel> filteredModelList = new ArrayList<>();
                for (BusStopsModel busStopsModel : data) {
                    final String text = busStopsModel.getName().toLowerCase();
                    if (text.contains(newText)) {
                        filteredModelList.add(busStopsModel);
                    }
                }
                Log.d("FilteredList", filteredModelList.toString());
                return filteredModelList;
            }
        });
        return true;
    }


}
