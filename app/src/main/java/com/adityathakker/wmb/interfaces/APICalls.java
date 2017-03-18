package com.adityathakker.wmb.interfaces;

import com.adityathakker.wmb.model.UpdatesPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adityajthakker on 23/8/16.
 */
public interface APICalls {
    @GET("get_locations_for_bus.php")
    Call<UpdatesPojo> getLocationUpdates(@Query("bus_no") String busNumber);
}
