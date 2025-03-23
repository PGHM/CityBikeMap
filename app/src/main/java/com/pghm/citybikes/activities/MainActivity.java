package com.pghm.citybikes.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.pghm.citybikes.Constants;
import com.pghm.citybikes.R;
import com.pghm.citybikes.elements.BikeStationFragmentHost;
import com.pghm.citybikes.elements.NoSwipeViewPager;
import com.pghm.citybikes.fragments.BikeStationListFragment;
import com.pghm.citybikes.fragments.BikeStationMapFragment;
import com.pghm.citybikes.models.BikeStation;
import com.pghm.citybikes.network.Callback;
import com.pghm.citybikes.network.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BikeStationFragmentHost {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) NoSwipeViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture scheduledTask;
    private BikeStationListFragment listFragment;
    private BikeStationMapFragment mapFragment;
    private int fragmentsLoaded = 0;
    private final HashMap<String, BikeStation> stationsById = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    synchronized public void fragmentLoaded() {
        fragmentsLoaded++;
        if (fragmentsLoaded == Constants.TAB_COUNT) {
            getFragmentReferences();
            initializeBikeStations();
        }
    }

    @Override
    public void centerMapOnStation(String id) {
        BikeStation station = stationsById.get(id);
        if (station != null) {
            viewPager.setCurrentItem(Constants.MAP_FRAGMENT_POSITION);
            mapFragment.centerMapOnStation(station);
        }
    }

    private void getFragmentReferences() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BikeStationListFragment) {
                listFragment = (BikeStationListFragment)fragment;
            } else if (fragment instanceof BikeStationMapFragment) {
                mapFragment = (BikeStationMapFragment)fragment;
            }
        }
    }

    public void initializeBikeStations() {
        Requests.fetchBikeData(this, new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                if (result != null) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            BikeStation station = BikeStation.fromJson(result.getJSONObject(i));
                            stationsById.put(station.getId(), station);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    initializeFragments(stationsById.values());
                    startBikeStationUpdateTask(true);
                } else {
                    retryBikeStationInitialization();
                    Toast.makeText(MainActivity.this, R.string.could_not_load_bike_stations,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void retryBikeStationInitialization() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeBikeStations();
            }
        }, Constants.BIKE_STATION_INITIALIZE_RETRY_DELAY);
    }

    public void startBikeStationUpdateTask(boolean startDelay) {
        Runnable updateRunnable = this::updateBikeStations;

        scheduledTask = scheduler.scheduleWithFixedDelay(
                updateRunnable,
                startDelay ? Constants.BIKE_STATION_UPDATE_INTERVAL : 0,
                Constants.BIKE_STATION_UPDATE_INTERVAL,
                TimeUnit.MILLISECONDS
        );
    }

    /* Assume that station list does not change during the lifetime of the application */
    public void updateBikeStations() {
        Log.d(Constants.LOG_NAME, "Updating stations");
        Requests.fetchBikeData(this, new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                if (result != null) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            JSONObject stationObject = result.getJSONObject(i);
                            String id = stationObject.getString("id");
                            BikeStation station = stationsById.get(id);
                            if (station != null) {
                                station.updateFromJson(stationObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    updateFragments(stationsById.values());
                }
            }
        });
    }

    public void initializeFragments(final Collection<BikeStation> stations) {
        mapFragment.initializeStations(stations);
        listFragment.updateStations(stations);
    }

    public void updateFragments(final Collection<BikeStation> stations) {
        mapFragment.updateStations(stations);
        listFragment.updateStations(stations);
    }

    @Override
    public void onDestroy() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        scheduler.shutdownNow();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (scheduledTask != null && scheduledTask.isCancelled()) {
            startBikeStationUpdateTask(false);
        }
        super.onResume();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == Constants.LIST_FRAGMENT_POSITION) {
                return new BikeStationListFragment();
            } else {
                return new BikeStationMapFragment();
            }
        }

        @Override
        public int getCount() {
            return Constants.TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case Constants.LIST_FRAGMENT_POSITION:
                    return getString(R.string.list_fragment_tab_title);
                case Constants.MAP_FRAGMENT_POSITION:
                    return getString(R.string.map_fragment_tab_title);
                default:
                    return getString(R.string.unknown_tab_title);
            }
        }
    }
}
