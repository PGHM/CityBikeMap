package com.pghm.citybikes;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jussi on 4.7.2016.
 */

/* Reflection works differently in Dalvik it seems, so we must test this method here with real
   device instead of standard unit test with JVM */
@RunWith(AndroidJUnit4.class)
public class InjectionTest {

    @Test
    public void testConstantInjection() throws Exception {
        Util.injectConstant("BIKE_DATA_URL", "not really url");
        assertEquals(Constants.BIKE_DATA_URL, "not really url");
    }
}
