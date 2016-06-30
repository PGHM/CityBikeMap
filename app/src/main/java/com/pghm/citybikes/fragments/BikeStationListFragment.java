package com.pghm.citybikes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pghm.citybikes.Elements.BikeStationFragmentHost;
import com.pghm.citybikes.R;

import butterknife.ButterKnife;

public class BikeStationListFragment extends Fragment {

    private BikeStationFragmentHost host;

    /* Required empty initializer */
    public BikeStationListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(view);
        host.fragmentLoaded();
        return view;
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
