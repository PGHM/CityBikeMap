package com.pghm.citybikes;

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
}
