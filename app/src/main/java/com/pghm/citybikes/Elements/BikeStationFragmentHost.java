package com.pghm.citybikes.Elements;

/**
 * Created by Jussi on 30.6.2016.
 */

/* Interface for the activity hosting fragments to communicate between the activity and fragments */
public interface BikeStationFragmentHost {
    /* Fragments can let the activity know that they are ready by using this method */
    void fragmentLoaded();
    void centerMapOnStation(String id);
}
