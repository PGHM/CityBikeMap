package com.pghm.citybikes;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pghm.citybikes.network.Callback;
import com.pghm.citybikes.network.Requests;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RequestTest {

    @Test
    public void testBikeDataFetch() throws Exception {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("{ stations: [] }"));
        server.enqueue(new MockResponse().setResponseCode(500));
        server.start();
        Util.injectConstant("BIKE_DATA_URL", server.url("/bikes").toString());

        /* Because we are testing async operation we need to wait for the result so we use countdown
           latch that waits with await() for certain time for countDown() to occur or times out */
        final CountDownLatch signal = new CountDownLatch(1);

        Requests.fetchBikeData(InstrumentationRegistry.getContext(), new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                assertNotNull(result);
                signal.countDown();
            }
        });

        signal.await(10, TimeUnit.SECONDS);

        final CountDownLatch signal2 = new CountDownLatch(1);

        Requests.fetchBikeData(InstrumentationRegistry.getContext(), new Callback<JSONArray>() {
            @Override
            public void callback(JSONArray result) {
                assertNull(result);
                signal2.countDown();
            }
        });

        signal2.await(10, TimeUnit.SECONDS);
    }}