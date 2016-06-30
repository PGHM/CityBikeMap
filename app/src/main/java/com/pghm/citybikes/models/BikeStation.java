package com.pghm.citybikes.models;

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

    public static BikeStation fromJson(JSONObject obj) {
        try {
            return new BikeStation(
                    obj.getString("id"),
                    obj.getString("name"),
                    obj.getDouble("y"),
                    obj.getDouble("x"),
                    obj.getInt("bikesAvailable"),
                    obj.getInt("spacesAvailable"),
                    obj.getBoolean("allowDropoff")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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

    public boolean allowDropoff() {
        return allowDropoff;
    }
}
