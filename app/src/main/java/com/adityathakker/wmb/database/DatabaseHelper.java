package com.adityathakker.wmb.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.adityathakker.wmb.utils.AppConst;
import com.adityathakker.wmb.model.MultiJourneyModel;
import com.adityathakker.wmb.model.SingleJourneyModel;
import com.adityathakker.wmb.model.BusStopsModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, AppConst.DB.DB_NAME, null, AppConst.DB.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate was Called: " + db.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL(AppConst.DB.DROP_TABLE_HISTORY);
        onCreate(db);*/
        Log.d(TAG, "onUpgrade was Called");

    }

    public void openDatabase() {
        String dbPath = context.getDatabasePath(AppConst.DB.DB_NAME).getPath();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            return;
        } else {
            sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void closeDatabase() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    public List<BusStopsModel> getAllBusStops(){
        List<BusStopsModel> busStopsModels = new ArrayList<>();
        openDatabase();
        Cursor busStopsCursor = sqLiteDatabase.query(AppConst.DB.BUS_STOPS_TABLE_NAME, null, null, null, null, null, AppConst.DB.BUS_STOPS_NAME);
        if(busStopsCursor.moveToFirst()){
            do{
                int id = busStopsCursor.getInt(0);
                String busStopName = busStopsCursor.getString(1);
                String latitude = busStopsCursor.getString(2);
                String longitude = busStopsCursor.getString(3);
                BusStopsModel busStopsModel = new BusStopsModel(id, busStopName, latitude, longitude);
                busStopsModels.add(busStopsModel);
            }while(busStopsCursor.moveToNext());
            closeDatabase();
            return busStopsModels;
        }else{
            closeDatabase();
            return  null;
        }
    }

    public BusStopsModel getBusStop(int id){
        openDatabase();
        Cursor resultBusStop = sqLiteDatabase.query(AppConst.DB.BUS_STOPS_TABLE_NAME,null,"id=?", new String[]{Integer.toString(id)},null,null,null);
        if(resultBusStop.moveToFirst()){
            closeDatabase();
            return new BusStopsModel(resultBusStop.getInt(0),resultBusStop.getString(1),resultBusStop.getString(2),resultBusStop.getString(3));
        }else{
            closeDatabase();
            return null;
        }
    }

    public String getNameOfBusStopFromId(int id){
        openDatabase();
        Cursor nameCursor = sqLiteDatabase.rawQuery("select name from bus_stops where id=" + id, null);
        if(nameCursor.moveToFirst()){
            closeDatabase();
            return nameCursor.getString(0);
        }else{
            closeDatabase();
            return null;
        }
    }

    public int getNumberOfStopsSingleJourney(String busNumber, int sourceId, int destinationId){
        openDatabase();
        Cursor stopsCursor = sqLiteDatabase.rawQuery("select abs(s.s_no-d.d_no) as stops from (select sequence as s_no from bus_info_stops where bus_number='"+busNumber+"' and bus_stop_id="+sourceId+") as s, (select sequence as d_no from bus_info_stops where bus_number='"+busNumber+"' and bus_stop_id="+destinationId+") as d", null);
        if(stopsCursor.moveToFirst()){
            closeDatabase();
            return stopsCursor.getInt(0);
        }else{
            closeDatabase();
            return -1;
        }
    }

    public List<SingleJourneyModel> getSingleBusJourneys(int sourceId, int destinationId){
        String sourceName = getNameOfBusStopFromId(sourceId);
        String destName = getNameOfBusStopFromId(destinationId);
        openDatabase();
        List<SingleJourneyModel> singleJourneyModelList = new ArrayList<>();
        Cursor busRoutes = sqLiteDatabase.rawQuery("select b1.bus_number from bus_info_stops as b1, bus_info_stops as b2 where b1.bus_stop_id=" + sourceId + " and b2.bus_stop_id=" + destinationId + " and b1.bus_number =b2.bus_number", null);
        if(busRoutes.moveToFirst()){
            do{
                String busNumber = busRoutes.getString(0);
                singleJourneyModelList.add(new SingleJourneyModel(sourceName, destName,sourceId, destinationId, busNumber,getNumberOfStopsSingleJourney(busNumber, sourceId, destinationId)));
            }while(busRoutes.moveToNext());
            closeDatabase();
            return singleJourneyModelList;
        }else{
            closeDatabase();
            return null;
        }
    }

    public List<MultiJourneyModel> getMultiBusJourneys(int sourceId, int destinationId, String singleJourneyBuses){
        List<MultiJourneyModel> multiJourneyModelList = new ArrayList<>();
        String sourceName = getNameOfBusStopFromId(sourceId);
        String destName = getNameOfBusStopFromId(destinationId);
        openDatabase();
        Cursor multiJourneyCursor = sqLiteDatabase.rawQuery("select distinct b1.bus_number as start_number, b1.bus_stop_id as middle_id, b2.bus_number as end_number,min((abs(bs1.sequence - b1.sequence) + abs(bs2.sequence- b2.sequence) + 1)) as no_of_stops from bus_info_stops as b1, bus_info_stops as b2, (select sequence, bus_number from bus_info_stops where bus_stop_id=" + sourceId + ") as bs1, (select sequence, bus_number from bus_info_stops where bus_stop_id=" + destinationId + ") as bs2 where b1.bus_number in (select distinct bus_number from bus_info_stops where bus_stop_id=" + sourceId + ") and b2.bus_number in (select distinct bus_number from bus_info_stops where bus_stop_id=" + destinationId + ") and b1.bus_stop_id=b2.bus_stop_id and b1.bus_number<>b2.bus_number and middle_id <> " + sourceId + " and middle_id <> " + destinationId + " and middle_id not in (select bus_stop_id from bus_info_stops where bus_number in (" + singleJourneyBuses + ")) and b1.bus_number=bs1.bus_number and b2.bus_number=bs2.bus_number group by start_number, end_number order by no_of_stops limit 10",null);
        if(multiJourneyCursor.moveToFirst()){
            do{
                int middleId = multiJourneyCursor.getInt(1);
                String startingBusNumber = multiJourneyCursor.getString(0);
                String endingBusNumber = multiJourneyCursor.getString(2);
                int noOfStops = multiJourneyCursor.getInt(3);
                String middleName = getNameOfBusStopFromId(middleId);

                MultiJourneyModel multiJourneyModel = new MultiJourneyModel(
                        sourceName,
                        destName,
                        middleName,
                        sourceId,
                        middleId,
                        destinationId,
                        startingBusNumber,
                        endingBusNumber,
                        noOfStops
                );
                multiJourneyModelList.add(multiJourneyModel);

            }while (multiJourneyCursor.moveToNext());
            closeDatabase();
            return multiJourneyModelList;
        }else{
            closeDatabase();
            return null;
        }
    }

    public List<BusStopsModel> getBusStopsByBusNumber(String busNumber){
        List<BusStopsModel> busStopsModels = new ArrayList<>();
        openDatabase();

        Cursor busStopsIDsCursor = sqLiteDatabase.query(AppConst.DB.BUS_INFO_STOPS_TABLE_NAME, new String[]{AppConst.DB.BUS_INFO_STOPS_BUS_STOP_ID}, AppConst.DB.BUS_INFO_STOPS_BUS_NUMBER + "=?", new String[]{busNumber}, null, null, AppConst.DB.BUS_INFO_STOPS_SEQUENCE);
        if(busStopsIDsCursor.moveToFirst()){
            do{
                int id = busStopsIDsCursor.getInt(0);
                busStopsModels.add(getBusStop(id));
            }while(busStopsIDsCursor.moveToNext());
            closeDatabase();
            return busStopsModels;
        }else{
            closeDatabase();
            return  null;
        }

    }

}
