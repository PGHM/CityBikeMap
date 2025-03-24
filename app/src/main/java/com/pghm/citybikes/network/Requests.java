package com.pghm.citybikes.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pghm.citybikes.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Requests {
    public static void fetchBikeData(Context context, final Callback<JSONArray> callback) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.BIKE_DATA_URL,
                    new JSONObject(Constants.BIKE_DATA_QUERY),
                    response -> {
                        Log.d(Constants.LOG_NAME, response.toString());
                        try {
                            JSONArray bikeStationArray = response
                                    .getJSONObject("data")
                                    .getJSONArray("vehicleRentalStations");
                            callback.callback(bikeStationArray);
                        } catch (JSONException e) {
                            Log.e(Constants.LOG_NAME, e.toString());
                            callback.callback(null);
                        }
                    },
                    error -> {
                        Log.e(Constants.LOG_NAME, error.toString());
                        callback.callback(null);
                    }
            );
            NetworkSingleton.getInstance(context).getRequestQueue().add(request);
        } catch (JSONException e) {
            Log.e(Constants.LOG_NAME, e.toString());
            callback.callback(null);
        }
    }
}
