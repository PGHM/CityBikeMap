package com.pghm.citybikes;

import com.pghm.citybikes.models.BikeStation;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jussi on 3.7.2016.
 */
public class Util {

    public static int getBikeIconResource(int bikesAvailable) {
        if (bikesAvailable == 0) {
            return R.drawable.red;
        } else if (bikesAvailable < Constants.LOW_BIKE_THRESHOLD) {
            return R.drawable.yellow;
        } else {
            return R.drawable.green;
        }
    }

    public static int getBikeIconMapResource(int bikesAvailable) {
        if (bikesAvailable == 0) {
            return R.drawable.red_small;
        } else if (bikesAvailable < Constants.LOW_BIKE_THRESHOLD) {
            return R.drawable.yellow_small;
        } else {
            return R.drawable.green_small;
        }
    }

    public static void sortStationsByName(List<BikeStation> stations) {
        Collections.sort(stations, new NameComparator());
    }

    public static class NameComparator implements Comparator<BikeStation> {
        @Override
        public int compare(BikeStation lhs, BikeStation rhs) {
            String left = lhs.getName();
            String right = rhs.getName();
            return left.compareTo(right);
        }
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
