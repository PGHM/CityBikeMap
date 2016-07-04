package com.pghm.citybikes;

import android.content.Context;

import com.pghm.citybikes.models.BikeStation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/* Unit tests are written for pieces of code that can easily be unit tested. Activities and
 Fragments cannot be easily unit tested as they rely on Android SDK and mocking everything we need
 from Android SDK would be tedious and sometimes impossible. Alternative would be to structure
 Activities and Fragments in such manner that they would not contain any business logic and all the
 logic would be exported to objects that the Activities initialize and use. But it is a trade off
 between code readability and bloatedness against unit testability. In this project I use the
 traditional way of writing business logic in the Activities and Fragments and the tests for code
 in these elements will be in the Application Tests that run on real device and simulate user input.
 */
//@RunWith(PowerMockRunner.class )
//@PrepareForTest(ContextCompat.class)
public class UnitTests {

    @Test
    public void testBikeStationParsing() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("bikeData.json");
        String input = Util.convertStreamToString(in);
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

    @Test
    public void testBikeStationUpdating() throws Exception {
        BikeStation station = new BikeStation("ID47", "Rautatieasema", 60.24, 24.60, 0, 20, true);
        JSONObject updateObject = new JSONObject();
        updateObject.put("bikesAvailable", 5);
        updateObject.put("spacesAvailable", 15);
        station.updateFromJson(updateObject);

        assertEquals(station.getSpacesAvailable(), 15);
        assertEquals(station.getBikesAvailable(), 5);
    }

    @Test
    public void testBikeStationTotalBikes() throws Exception {
        BikeStation station = new BikeStation("ID47", "Rautatieasema", 60.24, 24.60, 0, 20, true);
        assertEquals(station.getTotalSpace(), 20);
    }

    @Test
    public void testBikeStationGetFreeBikesText() throws Exception {
        BikeStation station = new BikeStation("ID47", "Rautatieasema", 60.24, 24.60, 5, 15, true);
        Context context = mock(Context.class);
        when(context.getString(R.string.free_bikes)).thenReturn("Free bikes: %1$s / %2$s");
        assertEquals(station.getFreeBikesText(context), "Free bikes: 5 / 20");
    }

    @Test
    public void testBikeStationSorting() throws Exception {
        BikeStation station = new BikeStation("ID49", "Valimo", 60.24, 24.60, 5, 15, true);
        BikeStation station2 = new BikeStation("ID47", "Rautatieasema", 60.24, 24.60, 5, 15, true);
        BikeStation station3 = new BikeStation("ID48", "Baana", 60.24, 24.60, 5, 15, true);
        List<BikeStation> stations = new ArrayList<>();
        stations.add(station);
        stations.add(station2);
        stations.add(station3);
        Util.sortStationsByName(stations);

        assertEquals(stations.get(0).getName(), "Baana");
        assertEquals(stations.get(1).getName(), "Rautatieasema");
        assertEquals(stations.get(2).getName(), "Valimo");
    }

    @Test
    public void testGetBikeIconResource() throws Exception {
        assertEquals(Util.getBikeIconResource(0), R.drawable.red);
        assertEquals(Util.getBikeIconResource(1), R.drawable.yellow);
        assertEquals(Util.getBikeIconResource(6), R.drawable.green);

        assertEquals(Util.getBikeIconMapResource(0), R.drawable.red_small);
        assertEquals(Util.getBikeIconMapResource(1), R.drawable.yellow_small);
        assertEquals(Util.getBikeIconMapResource(6), R.drawable.green_small);
    }
}