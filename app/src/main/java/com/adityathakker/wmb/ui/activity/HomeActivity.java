package com.adityathakker.wmb.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adityathakker.wmb.utils.AppConst;
import com.adityathakker.wmb.async.CopyAsync;
import com.adityathakker.wmb.database.DatabaseHelper;
import com.adityathakker.wmb.interfaces.ICopyAsyncCallback;
import com.adityathakker.wmb.R;
import com.adityathakker.wmb.model.BusStopsModel;

import java.io.File;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private TextView sourceTextView, destinationTextView;
    private Button findRouteButton, byBusNumberButton, byBusStopButton;
    private DatabaseHelper databaseHelper;
    private int busStopSourceId, busStopDestinationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Where's My Bus");
        setSupportActionBar(toolbar);

        String dbPath = AppConst.DB.DB_PATH + AppConst.DB.DB_NAME;
        File databaseFile = new File(dbPath);
        Log.v(TAG, "onCreate: Dictionary Path => " + databaseFile.getAbsolutePath());
        if (!databaseFile.exists()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Setting Up...");
            progressDialog.show();
            Log.d(TAG, "onCreate: Database Does Not Exists");
            CopyAsync copyAsync = new CopyAsync(this, new ICopyAsyncCallback() {
                @Override
                public void onCopyComplete() {
                    progressDialog.dismiss();
                }
            });
            copyAsync.execute();

        }

        databaseHelper = new DatabaseHelper(this);

        sourceTextView = (TextView) this.findViewById(R.id.content_main_textView_source);
        destinationTextView = (TextView) this.findViewById(R.id.content_main_textView_destination);
        findRouteButton = (Button) this.findViewById(R.id.content_main_button_find_route);

        sourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(HomeActivity.this, BusStopsActivity.class), AppConst.Codes.REQUEST_CODE_SELECT_BUS_STOP_SOURCE);
            }
        });

        destinationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(HomeActivity.this, BusStopsActivity.class), AppConst.Codes.REQUEST_CODE_SELECT_BUS_STOP_DESTINATION);
            }
        });

        findRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sourceBusStopText = sourceTextView.getText().toString();
                String destinationBusStopText = destinationTextView.getText().toString();

                if(sourceBusStopText.equals(getResources().getString(R.string.default_text_source_bus_stop))
                        || destinationBusStopText.equals(getResources().getString(R.string.default_text_destination_bus_stop))){
                    Toast.makeText(HomeActivity.this, "Please Select Bus Stops for Source and Destination", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "onClick: Source Bus Stop Id:" + busStopSourceId + " and Destination Bus Stop Id: " + busStopDestinationId);
                Intent intent = new Intent(HomeActivity.this, RoutesActivity.class);
                intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_SOURCE_ID, busStopSourceId);
                intent.putExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_DESTINATION_ID, busStopDestinationId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConst.Codes.REQUEST_CODE_SELECT_BUS_STOP_SOURCE){
            if(data != null){
                int busStopId = Integer.parseInt(data.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_ID));
                BusStopsModel busStopsModel = databaseHelper.getBusStop(busStopId);
                sourceTextView.setText(busStopsModel.getName());
                busStopSourceId = busStopId;
            }
        }

        if(requestCode == AppConst.Codes.REQUEST_CODE_SELECT_BUS_STOP_DESTINATION){
            int busStopId = Integer.parseInt(data.getStringExtra(AppConst.Codes.INTENT_EXTRA_CODE_BUS_STOP_ID));
            BusStopsModel busStopsModel = databaseHelper.getBusStop(busStopId);
            destinationTextView.setText(busStopsModel.getName());
            busStopDestinationId = busStopId;
        }
    }


}
