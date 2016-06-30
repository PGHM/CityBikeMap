package com.pghm.citybikes;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.pghm.citybikes.network.Callback;
import com.pghm.citybikes.network.Requests;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    @Test
    public void testBikeDataFetch() {
        Requests.fetchBikeData(InstrumentationRegistry.getContext(), new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                Log.d(Constants.LOG_NAME, "Huoh");
            }
        });
    }
}