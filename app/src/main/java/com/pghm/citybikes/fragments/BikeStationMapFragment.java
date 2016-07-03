package com.pghm.citybikes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pghm.citybikes.Elements.BikeStationFragmentHost;
import com.pghm.citybikes.R;
import com.pghm.citybikes.models.BikeStation;

import java.util.Collection;

import butterknife.ButterKnife;

public class BikeStationMapFragment extends Fragment {

    private BikeStationFragmentHost host;

    /* Required empty initializer */
    public BikeStationMapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(view);
        host.fragmentLoaded();
        return view;
    }

    public void initializeStations(final Collection<BikeStation> stations) {
        //TODO: implement
    }

    public void updateStations(final Collection<BikeStation> stations) {
        //TODO: implement
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            host = (BikeStationFragmentHost)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BikeStationFragmentHost");
        }
    }
}
