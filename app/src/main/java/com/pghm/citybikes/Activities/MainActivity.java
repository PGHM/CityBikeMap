package com.pghm.citybikes.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pghm.citybikes.Constants;
import com.pghm.citybikes.Elements.NoSwipeViewPager;
import com.pghm.citybikes.R;
import com.pghm.citybikes.fragments.BikeStationListFragment;
import com.pghm.citybikes.fragments.BikeStationMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) NoSwipeViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fragment fragment = new BikeStationListFragment();
        Log.d(Constants.LOG_NAME, fragment.toString());
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
