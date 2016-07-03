package com.pghm.citybikes;

import com.pghm.citybikes.models.BikeStation;

import org.json.JSONArray;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/* Unit tests are written for pieces of code that can easily be unit tested. Activities and
 Fragments cannot be easily unit tested as they rely on Android SDK and mocking everything we need
 from Android SDK would be tedious and sometimes impossible. Alternative would be to structure
 Activities and Fragments in such manner that they would not contain any business logic and all the
 logic would be exported to objects that the Activities initialize and use. But it is a trade off
 between code readability and bloatedness against unit testability. In this project I use the
 traditional way of writing business logic in the Activities and Fragments and the tests for code
 in these elements will be in the Application Tests that run on real device and simulate user input.
 */
public class UnitTests {

    @Test
    public void testBikeStationParsing() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("bikeData.json");
        String input = convertStreamToString(in);
        JSONArray stations = new JSONArray(input);
        ArrayList<BikeStation> results = new ArrayList<>();

        for (int i = 0; i < stations.length(); i++) {
            results.add(BikeStation.fromJson(stations.getJSONObject(i)));
        }

        BikeStation station1 = results.get(0);
        BikeStation station2 = results.get(1);

        assertEquals(station1.getId(), "A42");
        assertEquals(station1.getName(), "Baana");
        assertEquals(station1.getLat(), 60.164159, 0);
        assertEquals(station1.getLon(), 24.922583, 0);
        assertEquals(station1.getBikesAvailable(), 0);
        assertEquals(station1.getSpacesAvailable(), 20);
        assertTrue(station1.allowDropoff());

        assertEquals(station2.getId(), "A23");
        assertEquals(station2.getName(), "Rautatientori / itä");
        assertEquals(station2.getLat(), 60.17076, 0);
        assertEquals(station2.getLon(), 24.942522, 0);
        assertEquals(station2.getBikesAvailable(), 3);
        assertEquals(station2.getSpacesAvailable(), 17);
        assertFalse(station2.allowDropoff());
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}