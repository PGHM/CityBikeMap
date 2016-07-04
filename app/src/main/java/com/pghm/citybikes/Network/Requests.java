package com.pghm.citybikes.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pghm.citybikes.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jussi on 30.6.2016.
 */
public class Requests {
    public static void fetchBikeData(Context context, final Callback<JSONArray> callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Constants.BIKE_DATA_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constants.LOG_NAME, response.toString());
                        try {
                            callback.callback(response.getJSONArray("stations"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.callback(null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constants.LOG_NAME, error.toString());
                        callback.callback(null);
                    }
                });
        NetworkSingleton.getInstance(context).getRequestQueue().add(request);
    }
}
