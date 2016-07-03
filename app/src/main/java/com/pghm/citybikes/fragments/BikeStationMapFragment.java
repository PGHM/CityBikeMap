package com.pghm.citybikes.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pghm.citybikes.Constants;
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
    public BikeStationMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(view);

        requestPermissions();
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                initializeStations(stations);
                map.moveCamera(CameraUpdateFactory.newLatLng(Constants.DEFAULT_POSITION));
                map.moveCamera(CameraUpdateFactory.zoomTo(Constants.DEFAULT_ZOOM));
                if (hasFineLocationPermission()) {
                    map.setMyLocationEnabled(true);
                }
            }
        });
        host.fragmentLoaded();
        return view;
    }

    public void requestPermissions() {
        if (!hasFineLocationPermission()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean hasFineLocationPermission() {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        }
    }

    synchronized public void initializeStations(final Collection<BikeStation> stations) {
        this.stations = stations;
        if (map != null && stations != null) {
            for (BikeStation station : stations) {
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(station.getLat(), station.getLon()))
                        .icon(BitmapDescriptorFactory.fromResource(
                                Util.getBikeIconMapResource(station.getBikesAvailable())))
                        .title(station.getName())
                        .snippet(String.format(getContext().getString(R.string.free_bikes),
                                station.getBikesAvailable(), station.getTotalSpace()));
                Marker marker = map.addMarker(options);
                markersById.put(station.getId(), marker);
            }
        }
    }

    public void updateStations(final Collection<BikeStation> stations) {
        this.stations = stations;
        for (BikeStation station : this.stations) {
            Marker marker = markersById.get(station.getId());
            if (marker != null) {
                marker.setSnippet(String.format(getContext().getString(R.string.free_bikes),
                        station.getBikesAvailable(), station.getTotalSpace()));
                marker.setIcon(BitmapDescriptorFactory.fromResource(
                        Util.getBikeIconMapResource(station.getBikesAvailable())));
            }
        }
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
