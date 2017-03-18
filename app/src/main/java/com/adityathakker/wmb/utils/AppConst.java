package com.adityathakker.wmb.utils;

/**
 * Created by Aditya Thakker (Github: @adityathakker) on 17/8/16.
 */
public class AppConst {
    public static class DB {
        //DB Name
        public static final String DB_NAME = "wmb_offline.sqlite";
        public static final String DB_PATH = "/data/data/com.adityathakker.wmb/databases/";
        public static final int DB_VERSION = 1;
        
        
        //Bus Stops Table
        public static final String BUS_STOPS_TABLE_NAME = "bus_stops";
        public static final String BUS_STOPS_ID = "id";
        public static final String BUS_STOPS_NAME = "name";
        public static final String BUS_STOPS_LAT = "latitude";
        public static final String BUS_STOPS_LONG = "longitude";

        //Bus Stops Info
        public static final String BUS_INFO_STOPS_TABLE_NAME = "bus_info_stops";
        public static final String BUS_INFO_STOPS_BUS_NUMBER = "bus_number";
        public static final String BUS_INFO_STOPS_BUS_STOP_ID = "bus_stop_id";
        public static final String BUS_INFO_STOPS_SEQUENCE = "sequence";
    }

    public static class Codes {
        public static final int REQUEST_CODE_SELECT_BUS_STOP_SOURCE = 1;
        public static final int REQUEST_CODE_SELECT_BUS_STOP_DESTINATION = 2;

        public static final int RESULT_CODE_SELECT_BUS_STOP_SUCCESS = 3;
        public static final String INTENT_EXTRA_CODE_BUS_STOP_ID = "bus_stop_id";

        public static final String INTENT_EXTRA_CODE_BUS_STOP_SOURCE_ID = "bus_stop_source_id";
        public static final String INTENT_EXTRA_CODE_BUS_STOP_DESTINATION_ID = "bus_stop_destination_id";

        public static final String INTENT_EXTRA_CODE_DETAILS_IS_SINGLE = "details_journey_type";
        public static final String INTENT_EXTRA_CODE_DETAILS_STARTING_NAME = "details_starting_name";
        public static final String INTENT_EXTRA_CODE_DETAILS_ENDING_NAME = "details_ending_name";
        public static final String INTENT_EXTRA_CODE_DETAILS_STARTING_ID = "details_starting_id";
        public static final String INTENT_EXTRA_CODE_DETAILS_ENDING_ID = "details_ending_id";
        public static final String INTENT_EXTRA_CODE_DETAILS_BUS_NUMBER = "details_bus_number";
        public static final String INTENT_EXTRA_CODE_DETAILS_STOPS = "details_stops";
        public static final String INTENT_EXTRA_CODE_DETAILS_MIDDLE_NAME = "details_middle_name";

        public static final String INTENT_EXTRA_CODE_DETAILS_MIDDLE_ID = "details_middle_id";
        public static final String INTENT_EXTRA_CODE_DETAILS_STARTING_BUS_NUMBER = "details_starting_bus_number";
        public static final String INTENT_EXTRA_CODE_DETAILS_ENDING_BUS_NUMBER = "details_ending_bus_number";

        public static final String INTENT_EXTRA_CODE_DETAILS_BUS_STOP_ID = "details_bus_stop_id";

    }
    public static class STATUS{
        public static final String STATUS_SUCCESS = "success";
        public static final String STATUS_FALIED = "failed";
    }


}
