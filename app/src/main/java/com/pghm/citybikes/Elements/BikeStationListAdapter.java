package com.pghm.citybikes.Elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pghm.citybikes.R;
import com.pghm.citybikes.Util;
import com.pghm.citybikes.models.BikeStation;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Jussi on 3.7.2016.
 */
public class BikeStationListAdapter extends ArrayAdapter<BikeStation> {

    private BikeStationFragmentHost host;

    public BikeStationListAdapter(Context context, int textViewResourceId,
                                  BikeStationFragmentHost host) {
        super(context, textViewResourceId);
        this.host = host;
    }

    public BikeStationListAdapter(Context context, int resource, List<BikeStation> items,
                                  BikeStationFragmentHost host) {
        super(context, resource, items);
        this.host = host;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.element_bike_station, null);
        }

        final BikeStation station = getItem(position);

        if (station != null) {
            ImageView status = ButterKnife.findById(v, R.id.status);
            TextView name = ButterKnife.findById(v, R.id.name);
            TextView freeBikes = ButterKnife.findById(v, R.id.free_bikes);

            name.setText(station.getName());
            freeBikes.setText(String.format(getContext().getString(R.string.free_bikes),
                    station.getBikesAvailable(), station.getTotalSpace()));
            status.setImageDrawable(Util.getBikeIcon(getContext(), station.getBikesAvailable()));
        }

        return v;
    }

}
