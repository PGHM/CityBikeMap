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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pghm.citybikes.Constants;
import com.pghm.citybikes.Elements.BikeStationFragmentHost;
import com.pghm.citybikes.Elements.NoSwipeViewPager;
import com.pghm.citybikes.R;
import com.pghm.citybikes.fragments.BikeStationListFragment;
import com.pghm.citybikes.fragments.BikeStationMapFragment;
import com.pghm.citybikes.models.BikeStation;
import com.pghm.citybikes.network.Callback;
import com.pghm.citybikes.network.Requests;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BikeStationFragmentHost {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) NoSwipeViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private int fragmentsLoaded = 0;
    private ArrayList<BikeStation> stations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    synchronized public void fragmentLoaded() {
        fragmentsLoaded++;
        if (fragmentsLoaded == Constants.TAB_COUNT) {
            initializeBikeStations();
        }
    }

    public void initializeBikeStations() {
        Log.i(Constants.LOG_NAME, "1");
        Requests.fetchBikeData(this, new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                if (result != null) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            stations.add(BikeStation.fromJson(result.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    initializeFragments();
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initializeBikeStations();
                        }
                    }, Constants.BIKE_INITIALIZE_RETRY_DELAY);
                    Toast.makeText(MainActivity.this, R.string.could_not_load_bike_stations,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initializeFragments() {
        //TODO: implement
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
