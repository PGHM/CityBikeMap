package com.pghm.citybikes;

/**
 * Created by Jussi on 30.6.2016.
 */
public class Constants {

    public static final String BIKE_DATA_URL = "https://api.digitransit.fi/routing/v1/routers/hsl/bike_rental";
    public static final String LOG_NAME = "CityBikes";

    public static final int LIST_FRAGMENT_POSITION = 0;
    public static final int MAP_FRAGMENT_POSITION = 1;
    public static final int TAB_COUNT = 2;
    public static final int BIKE_STATION_INITIALIZE_RETRY_DELAY = 5000; // 5 seconds, milliseconds
    public static final int BIKE_STATION_UPDATE_INTERVAL = 60000; // 1 minute, milliseconds
}
