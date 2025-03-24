package com.pghm.citybikes.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkSingleton {

    private static NetworkSingleton instance;
    private final RequestQueue requestQueue;

    private NetworkSingleton(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static NetworkSingleton getInstance(Context context) {
        if(instance == null){
            instance = new NetworkSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return this.requestQueue;
    }
}
