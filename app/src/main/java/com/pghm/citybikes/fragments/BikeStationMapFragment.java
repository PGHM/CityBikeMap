package com.pghm.citybikes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pghm.citybikes.R;

import butterknife.ButterKnife;

public class BikeStationMapFragment extends Fragment {

    /* Required empty initializer */
    public BikeStationMapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(view);
        return view;
    }
}
