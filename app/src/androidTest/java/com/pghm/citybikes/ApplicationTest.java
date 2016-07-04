package com.pghm.citybikes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pghm.citybikes.activities.MainActivity;
import com.pghm.citybikes.fragments.BikeStationMapFragment;
import com.pghm.citybikes.models.BikeStation;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.HashMap;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    MockWebServer server;
    String bikeResponse;
    String updatedBikeResponse;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("bikeData.json");
        bikeResponse = Util.convertStreamToString(in);
        in = this.getClass().getClassLoader().getResourceAsStream("updatedBikeData.json");
        updatedBikeResponse = Util.convertStreamToString(in);
        server.start();
        Util.injectConstant("BIKE_DATA_URL", server.url("/bikes").toString());
    }

    @Test
    public void testNormalLaunch() throws Exception {
        server.enqueue(new MockResponse().setBody(bikeResponse));
        server.enqueue(new MockResponse().setBody(updatedBikeResponse));

        activityRule.launchActivity(new Intent());
        final MainActivity activity = activityRule.getActivity();

        ListView stationList = (ListView)activity.findViewById(R.id.list_view);
        final BikeStation station1 = ((BikeStation) stationList.getAdapter().getItem(0));
        final BikeStation station2 = ((BikeStation) stationList.getAdapter().getItem(1));

        assertEquals(stationList.getAdapter().getCount(), 2);
        assertEquals(station1.getId(), "1");
        assertEquals(station2.getId(), "2");
        assertEquals(station1.getBikesAvailable(), 0);
        assertEquals(station2.getBikesAvailable(), 3);

        /* Get private fields with reflection to validate map state */
        BikeStationMapFragment mapFragment = (BikeStationMapFragment)Util.getPrivateVariable(
                MainActivity.class, "mapFragment", activity);
        HashMap<String, Marker> markersById = (HashMap<String, Marker>)Util.getPrivateVariable(
                BikeStationMapFragment.class, "markersById", mapFragment);
        final GoogleMap map = (GoogleMap)Util.getPrivateVariable(BikeStationMapFragment.class,
                "map", mapFragment);
        final Marker marker1 = markersById.get(station1.getId());
        final Marker marker2 = markersById.get(station2.getId());
        final LatLng defaultPosition = Constants.DEFAULT_POSITION;

        /* Map operations must be handled on main thread, otherwise exception is thrown */
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng mapPosition = map.getCameraPosition().target;
                assertEquals(marker1.getPosition().latitude, 60.164159, 0);
                assertEquals(marker1.getPosition().longitude, 24.922583, 0);
                assertEquals(marker2.getPosition().latitude, 60.17076, 0);
                assertEquals(marker2.getPosition().longitude, 24.942522, 0);
                assertEquals(marker1.getSnippet(), station1.getFreeBikesText(activity));
                assertEquals(marker2.getSnippet(), station2.getFreeBikesText(activity));
                assertEquals(mapPosition.longitude, defaultPosition.longitude, 0.000001);
                assertEquals(mapPosition.latitude, defaultPosition.latitude, 0.000001);
            }
        });

        onData(anything()).inAdapterView(withId(R.id.list_view)).atPosition(0).perform(click());

        /* Wait for the map animation to finish, for some reason espresso does not wait on this and
           map animation is not disabled when all animations are disabled from developer options */
        Thread.sleep(1000);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng updatedMapPosition = map.getCameraPosition().target;
                assertTrue(marker1.isInfoWindowShown());
                assertEquals(updatedMapPosition.latitude, marker1.getPosition().latitude, 0.000001);
                assertEquals(updatedMapPosition.longitude, marker1.getPosition().longitude, 0.000001);
            }
        });

        activity.updateBikeStations();

        /* Wait for the update to occur, we could use IdlingResource for this too but it would
           require some refactoring, not worth the effort now */
        Thread.sleep(100);

        assertEquals(station1.getBikesAvailable(), 5);
        assertEquals(station2.getBikesAvailable(), 7);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals(marker1.getSnippet(), station1.getFreeBikesText(activity));
                assertEquals(marker2.getSnippet(), station2.getFreeBikesText(activity));
            }
        });
    }

    @Test
    public void testLaunchWithNetworkError() throws Exception {
        Util.injectConstant("BIKE_STATION_INITIALIZE_RETRY_DELAY", 500);
        server.enqueue(new MockResponse().setResponseCode(500));
        server.enqueue(new MockResponse().setBody(bikeResponse));

        activityRule.launchActivity(new Intent());

        ListView stationList = (ListView) activityRule.getActivity().findViewById(R.id.list_view);
        assertEquals(stationList.getAdapter().getCount(), 0);

        Thread.sleep(500);

        assertEquals(stationList.getAdapter().getCount(), 2);
    }
}