package com.pghm.citybikes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pghm.citybikes.R;

import butterknife.ButterKnife;

public class BikeStationListFragment extends Fragment {

    /* Required empty initializer */
    public BikeStationListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(view);
        return view;
    }
}
