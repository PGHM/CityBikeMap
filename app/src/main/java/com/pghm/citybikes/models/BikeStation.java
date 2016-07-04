package com.pghm.citybikes.models;

import android.content.Context;

import com.pghm.citybikes.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jussi on 30.6.2016.
 */
public class BikeStation {

    private String id;
    private String name;
    private double lat;
    private double lon;
    private int bikesAvailable;
    private int spacesAvailable;
    private boolean allowDropoff;

    public BikeStation(String id, String name, double lat, double lon, int bikesAvailable,
                       int spacesAvailable, boolean allowDropoff) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.bikesAvailable = bikesAvailable;
        this.spacesAvailable = spacesAvailable;
        this.allowDropoff = allowDropoff;
    }

    public static BikeStation fromJson(JSONObject obj) throws JSONException {
        return new BikeStation(
                obj.getString("id"),
                obj.getString("name"),
                obj.getDouble("y"),
                obj.getDouble("x"),
                obj.getInt("bikesAvailable"),
                obj.getInt("spacesAvailable"),
                obj.getBoolean("allowDropoff")
        );
    }

    /* Assume that bike station id, name or location do not change during the lifetime of the app */
    public void updateFromJson(JSONObject obj) throws JSONException {
        bikesAvailable = obj.getInt("bikesAvailable");
        spacesAvailable = obj.getInt("spacesAvailable");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public int getSpacesAvailable() {
        return spacesAvailable;
    }

    public String getFreeBikesText(Context context) {
        return String.format(context.getString(R.string.free_bikes),
                bikesAvailable, this.getTotalSpace());
    }

    public int getTotalSpace() {
        return bikesAvailable + spacesAvailable;
    }

    public boolean allowDropoff() {
        return allowDropoff;
    }
}
