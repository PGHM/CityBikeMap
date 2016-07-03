package com.pghm.citybikes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pghm.citybikes.Elements.BikeStationFragmentHost;
import com.pghm.citybikes.R;
import com.pghm.citybikes.Util;
import com.pghm.citybikes.models.BikeStation;

import java.util.Collection;
import java.util.HashMap;

import butterknife.ButterKnife;

public class BikeStationMapFragment extends Fragment {

    /* For some reason we can't bind MapView with Butterknifes @BindView */
    private MapView mapView;
    private BikeStationFragmentHost host;
    private GoogleMap map;
    private Collection<BikeStation> stations;
    private HashMap<String, Marker> markersById = new HashMap<>();

    /* Required empty initializer */
    public BikeStationMapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(view);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                initializeStations(stations);
            }
        });
        host.fragmentLoaded();
        return view;
    }

    synchronized public void initializeStations(final Collection<BikeStation> stations) {
        this.stations = stations;
        if (map != null && stations != null) {
            for (BikeStation station : stations) {
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(station.getLat(), station.getLon()))
                        .icon(BitmapDescriptorFactory.fromResource(
                                Util.getBikeIconResource(station.getBikesAvailable())))
                        .title(station.getName());
                Marker marker = map.addMarker(options);
                markersById.put(station.getId(), marker);
            }
        }
    }

    public void updateStations(final Collection<BikeStation> stations) {
        //TODO: implement
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
