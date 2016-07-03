package com.pghm.citybikes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Jussi on 3.7.2016.
 */
public class Util {

    public static Drawable getBikeIcon(Context context, int bikesAvailable) {
        if (bikesAvailable == 0) {
            return ContextCompat.getDrawable(context, R.drawable.red);
        } else if (bikesAvailable < Constants.LOW_BIKE_THRESHOLD) {
            return ContextCompat.getDrawable(context, R.drawable.yellow);
        } else {
            return ContextCompat.getDrawable(context, R.drawable.green);
        }
    }
}
