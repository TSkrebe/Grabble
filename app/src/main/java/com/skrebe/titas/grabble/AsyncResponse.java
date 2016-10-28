package com.skrebe.titas.grabble;

import com.google.maps.android.kml.KmlLayer;

/**
 * Created by titas on 16.9.28.
 */

public interface AsyncResponse {
    void processFinish(KmlLayer output);
}
