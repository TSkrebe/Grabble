package com.skrebe.titas.grabble.notUsed;

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

    AsyncResponse asyncResponse = null;
    //String ns = null;
    Context c;
 //   GoogleMap map = null;


    public PlaceMarkParser(Context c){
        asyncResponse = (AsyncResponse) c;
        this.c = c;
    }

    @Override
    protected void onPostExecute(KmlLayer layer) {
        asyncResponse.processFinish(layer);
    }

    @Override
    protected KmlLayer doInBackground(String... urls) {
        String url = urls[0];
        try {
            InputStream inputStream = new URL(url).openStream();
            KmlLayer layer = new KmlLayer(null, inputStream, c.getApplicationContext());
            return layer;
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void moveCameraToKml(KmlLayer kmlLayer) {

    }


//        String url = urls[0];
//        Log.e("BAD", url);
//        Log.e("BAD", "FIRST");
//        InputStream inputStream = null;
//        try {
//            inputStream = new URL(url).openStream();
//            Log.e("BAD", "SEcond");
//
//            XmlPullParser parser = Xml.newPullParser();
//            Log.e("BAD", "3333");
//
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(inputStream, null);
//            parser.nextTag();
//            Log.e("BAD", "44444");
//
//            return readFeed(parser);
//
//        } catch (IOException e) {
//            //mark as inconsistent
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//
//        Log.e("BAD", "5555");
//
//        return new ArrayList<>();
////    }
//
//
//    private List<PlaceMark> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
//        List<PlaceMark> entries = new ArrayList<>();
//
//        parser.require(XmlPullParser.START_TAG, null, "kml");
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            // Starts by looking for the entry tag
//            if (name.equals("Placemark")) {
//                entries.add(readEntry(parser));
//            } else {
//                skip(parser);
//            }
//        }
//        return entries;
//    }
//    static String n = "Place Mark Parser";
//
//    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
//// to their respective "read" methods for processing. Otherwise, skips the tag.
//    private PlaceMark readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
//        parser.require(XmlPullParser.START_TAG, ns, "Placemark");
//        String title = null;
//        String summary = null;
//        PlaceMark.Point point = null;
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            if (name.equals("name")) {
//                title = readName(parser);
//            } else if (name.equals("description")) {
//                summary = readDescription(parser);
//            } else if (name.equals("Point")) {
//                point = readPoint(parser);
//            } else {
//                skip(parser);
//            }
//        }
//        Log.i(n, point.longitude+"");
//        return new PlaceMark(title, summary, point);
//    }
//    private PlaceMark.Point readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
//
//        parser.require(XmlPullParser.START_TAG, null, "Point");
//        String pointData = null;
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            // Starts by looking for the entry tag
//            if (name.equals("coordinates")) {
//                pointData = readPointData(parser);
//            } else {
//                skip(parser);
//            }
//        }
//        String[] data = pointData.split(pointData, ',');
//
//        return new PlaceMark.Point(Double.valueOf(data[0]), Double.valueOf(data[1]));
//    }
//
//    private String readPointData(XmlPullParser parser) throws IOException, XmlPullParserException {
//
//        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
//        String title = readText(parser);
//        Log.i(n, title);
//
//        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
//        return title;
//
//    }
//
//    // Processes title tags in the feed.
//    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
//        parser.require(XmlPullParser.START_TAG, ns, "name");
//        String title = readText(parser);
//        parser.require(XmlPullParser.END_TAG, ns, "name");
//        return title;
//    }
//
//
//    // Processes summary tags in the feed.
//    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
//        parser.require(XmlPullParser.START_TAG, ns, "description");
//        String summary = readText(parser);
//        parser.require(XmlPullParser.END_TAG, ns, "description");
//        return summary;
//    }
//
//    // For the tags title and summary, extracts their text values.
//    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
//        String result = "";
//        if (parser.next() == XmlPullParser.TEXT) {
//            result = parser.getText();
//            parser.nextTag();
//        }
//        return result;
//    }
//
//
//
//    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
//        if (parser.getEventType() != XmlPullParser.START_TAG) {
//            throw new IllegalStateException();
//        }
//        int depth = 1;
//        while (depth != 0) {
//            switch (parser.next()) {
//                case XmlPullParser.END_TAG:
//                    depth--;
//                    break;
//                case XmlPullParser.START_TAG:
//                    depth++;
//                    break;
//            }
//        }
//    }

}
