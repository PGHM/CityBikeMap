package com.pghm.citybikes;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Jussi on 3.7.2016.
 */
public class Util {

    public static Drawable getBikeIcon(Context context, int bikesAvailable) {
        if (bikesAvailable == 0) {
            return context.getResources().getDrawable(R.drawable.red);
        } else if (bikesAvailable < Constants.LOW_BIKE_THRESHOLD) {
            return context.getResources().getDrawable(R.drawable.yellow);
        } else {
            return context.getResources().getDrawable(R.drawable.green);
        }
    }
}
