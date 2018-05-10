package com.pghm.citybikes;

import com.google.android.gms.maps.model.LatLng;

import static java.lang.Integer.valueOf;

/* Some of the constants have call to valueOf() because we want to change them later in tests
   and valueOf() prevents them from being inlined. Inlining will prevent any kind of modification
   later via reflection. This class could also be refactored to be singleton or bunch of methods
   that return static values. With that approach it would be possible to mock the values during
   the tests more easily, but in my opinion using the current style keeps the actual code that
   uses these constants cleaner. It could also be that I'm missing an easier way to this. */
public class Constants {

    public static final String BIKE_DATA_URL = String.valueOf(
            "https://api.digitransit.fi/routing/v1/routers/hsl/bike_rental");
    public static final String LOG_NAME = "CityBikes";

    public static final LatLng DEFAULT_POSITION = new LatLng(60.169787, 24.938606);
    public static final float DEFAULT_ZOOM = 14.0f;

    public static final int MAP_FRAGMENT_POSITION = 0;
    public static final int LIST_FRAGMENT_POSITION = 1;
    public static final int TAB_COUNT = 2;
    public static final int LOW_BIKE_THRESHOLD = 5;
    public static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 100;
    public static final int BIKE_STATION_INITIALIZE_RETRY_DELAY = valueOf(5000); // 5 seconds, milliseconds
    public static final int BIKE_STATION_UPDATE_INTERVAL = 60000; // 1 minute, milliseconds
}
