package com.skrebe.titas.grabble;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.maps.android.kml.KmlLayer;
import com.skrebe.titas.grabble.AsyncResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by titas on 16.9.28.
 */
public class PlaceMarkParser extends AsyncTask<String, Void, KmlLayer> {

    private AsyncResponse asyncResponse = null;
    private Context c;
    private ProgressDialog progDailog;

    public PlaceMarkParser(Context c){
        asyncResponse = (AsyncResponse) c;
        this.c = c;
        progDailog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progDailog.setMessage("Loading map data...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected KmlLayer doInBackground(String... urls) {
        String url = urls[0];
        try {
            InputStream inputStream = new URL(url).openStream();
            return new KmlLayer(null, inputStream, c.getApplicationContext());
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(KmlLayer layer) {
        asyncResponse.processFinish(layer);
        progDailog.dismiss();
    }

}
