package com.pghm.citybikes.models;

import android.content.Context;

import com.pghm.citybikes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jussi on 30.6.2016.
 */
public class BikeStation {

    private final String id;
    private final String name;
    private final double lat;
    private final double lon;
    private int bikesAvailable;
    private int spacesAvailable;

    public BikeStation(
            String id,
            String name,
            double lat,
            double lon,
            int bikesAvailable,
            int spacesAvailable
    ) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.bikesAvailable = bikesAvailable;
        this.spacesAvailable = spacesAvailable;
    }

    public static BikeStation fromJson(JSONObject obj) throws JSONException {
        int spacesAvailable = parseSpacesAvailable(obj);
        if (spacesAvailable > 0) {
            return new BikeStation(
                    obj.getString("id"),
                    obj.getString("name"),
                    obj.getDouble("lat"),
                    obj.getDouble("lon"),
                    parseBikesAvailable(obj),
                    spacesAvailable
            );
        } else {
            throw new JSONException("Bike station object did not have all data");
        }
    }

    /* Assume that bike station id, name or location do not change during the lifetime of the app.
    * Also assume that station did not suddenly have invalid data on bike or space availablity */
    public void updateFromJson(JSONObject obj) throws JSONException {
        bikesAvailable = parseBikesAvailable(obj);
        spacesAvailable = parseSpacesAvailable(obj);
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

    public String getFreeBikesText(Context context) {
        return String.format(context.getString(R.string.free_bikes), bikesAvailable, this.getTotalSpace());
    }

    private int getTotalSpace() {
        return bikesAvailable + spacesAvailable;
    }

    private static int parseBikesAvailable(JSONObject obj) {
        try {
            JSONArray bikesAvailableByTypeArray = obj
                    .getJSONObject("availableVehicles")
                    .getJSONArray("byType");

            for (int i = 0; i < bikesAvailableByTypeArray.length(); i++) {
                JSONObject bikesAvailableObject = bikesAvailableByTypeArray.getJSONObject(i);
                String type = bikesAvailableObject
                        .getJSONObject("vehicleType")
                        .getString("formFactor");
                if (type.equals("BICYCLE")) {
                    return bikesAvailableObject.getInt("count");
                }
            }

            return 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    private static int parseSpacesAvailable(JSONObject obj) {
        try {
            JSONArray spacesAvailableByTypeArray = obj
                    .getJSONObject("availableSpaces")
                    .getJSONArray("byType");

            for (int i = 0; i < spacesAvailableByTypeArray.length(); i++) {
                JSONObject spacesAvailableObject = spacesAvailableByTypeArray.getJSONObject(i);
                String type = spacesAvailableObject
                        .getJSONObject("vehicleType")
                        .getString("formFactor");
                if (type.equals("BICYCLE")) {
                    return spacesAvailableObject.getInt("count");
                }
            }

            return 0;
        } catch (JSONException e) {
            return 0;
        }
    }
}
