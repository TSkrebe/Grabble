package com.skrebe.titas.grabble.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skrebe.titas.grabble.R;
import com.skrebe.titas.grabble.entities.LetterEntity;
import com.skrebe.titas.grabble.entities.LocationPointEntity;
import com.skrebe.titas.grabble.entities.WordEntity;
import com.skrebe.titas.grabble.entities.WordScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_LOCATION_POINTS =
            "CREATE TABLE " + LocationPointEntity.TABLE_NAME + " (" +
                    LocationPointEntity.COlUMN_NAME_NAME + TEXT_TYPE + " PRIMARY KEY," +
                    LocationPointEntity.COlUMN_NAME_LETTER + TEXT_TYPE + COMMA_SEP +
                    LocationPointEntity.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    LocationPointEntity.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    LocationPointEntity.COLUMN_NAME_VISITED + INTEGER_TYPE + " )";


    private static final String SQL_CREATE_LETTERS =
            "CREATE TABLE " + LetterEntity.TABLE_NAME + " (" +
                    LetterEntity.COLUMN_NAME_LETTER + TEXT_TYPE + " PRIMARY_KEY," +
                    LetterEntity.COLUMN_NAME_COUNT + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_WORDS =
            "CREATE TABLE " + WordEntity.TABLE_NAME + " (" +
                    WordEntity._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    WordEntity.COLUMN_NAME_WORD + TEXT_TYPE + COMMA_SEP +
                    WordEntity.COLUMN_NAME_SCORE + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationPointEntity.TABLE_NAME;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationsPoint.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION_POINTS);
        db.execSQL(SQL_CREATE_WORDS);
        db.execSQL(SQL_CREATE_LETTERS);
        for (char a='a'; a <= 'z'; a++){
            ContentValues cv = new ContentValues();
            cv.put(LetterEntity.COLUMN_NAME_LETTER, a+"");
            cv.put(LetterEntity.COLUMN_NAME_COUNT, 0);
            db.insert(LetterEntity.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertLocationPoints(Map<String, Marker> markers){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<String, Marker> e : markers.entrySet()) {
                Marker marker = e.getValue();
                String name = e.getKey();
                ContentValues cv = new ContentValues();
                cv.put(LocationPointEntity.COLUMN_NAME_LATITUDE, marker.getPosition().latitude);
                cv.put(LocationPointEntity.COLUMN_NAME_LONGITUDE, marker.getPosition().longitude);
                cv.put(LocationPointEntity.COlUMN_NAME_LETTER, marker.getTitle().trim());
                cv.put(LocationPointEntity.COlUMN_NAME_NAME, name);
                cv.put(LocationPointEntity.COLUMN_NAME_VISITED, 0);
                db.insert(LocationPointEntity.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("ERROR DB", e.toString());
        }finally {
            db.endTransaction();
        }

    }


    public int deleteLocationPoints() {
        return getWritableDatabase().delete(LocationPointEntity.TABLE_NAME, null, null);
    }


    public void getAllMarkerOptions(GoogleMap map, Map<String, Marker> markers){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + LocationPointEntity.TABLE_NAME, null);

        while(cursor.moveToNext()){
            String letter = cursor.getString(cursor.getColumnIndex(LocationPointEntity.COlUMN_NAME_LETTER));
            String name = cursor.getString(cursor.getColumnIndex(LocationPointEntity.COlUMN_NAME_NAME));
            double latitude = cursor.getDouble(cursor.getColumnIndex(LocationPointEntity.COLUMN_NAME_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(LocationPointEntity.COLUMN_NAME_LONGITUDE));
            int visited = cursor.getInt(cursor.getColumnIndex(LocationPointEntity.COLUMN_NAME_VISITED));

            BitmapDescriptor bd = (visited == 0) ?
                    BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red) :
                    BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue);

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(bd)
                    .title(" " + letter));  //space for centering letter
            //load to memory
            markers.put(name, marker);
        }
        cursor.close();
    }

    public void setLocationPointVisited(String name, TextView popUp, AnimationSet animation) {
        Log.e("NAME", name);
        SQLiteDatabase db = getWritableDatabase();
        //Find location point
        Cursor c = db.query(LocationPointEntity.TABLE_NAME,
                new String[]{LocationPointEntity.COLUMN_NAME_VISITED, LocationPointEntity.COlUMN_NAME_LETTER},
                LocationPointEntity.COlUMN_NAME_NAME + " = ?",
                new String[]{name}, null, null, null);
        c.moveToNext();
        int visited = c.getInt(0);
        String letter = c.getString(1);
        c.close();
        //if not visited make visited and add picked letter
        if(visited == 0){
            ContentValues cv = new ContentValues();
            cv.put(LocationPointEntity.COLUMN_NAME_VISITED, 1);
            db.update(LocationPointEntity.TABLE_NAME,
                    cv,
                    LocationPointEntity.COlUMN_NAME_NAME + " = ?",
                    new String[]{name});
            addLetter(letter, 1);
            popUp.setText(letter.toUpperCase());
            popUp.startAnimation(animation);
        }

       // db.close();
    }

    public void addLetter(String letter, int amount) {
        letter = letter.toLowerCase();
        SQLiteDatabase db = getWritableDatabase();

        int count = getLetterCount(letter);

        ContentValues cv = new ContentValues();
        cv.put(LetterEntity.COLUMN_NAME_LETTER, letter);
        cv.put(LetterEntity.COLUMN_NAME_COUNT, count+amount);
        db.update(LetterEntity.TABLE_NAME, cv, LetterEntity.COLUMN_NAME_LETTER + " = ? ", new String[]{letter});
    }


    public int getLetterCount(String letter){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LetterEntity.TABLE_NAME,
                new String[]{LetterEntity.COLUMN_NAME_COUNT},
                LetterEntity.COLUMN_NAME_LETTER + " = ?",
                new String[]{letter}, null, null, null);
        cursor.moveToNext();
        Log.e("ERROROR count", "|" + letter + "|");
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Map<String, Integer> getAllLetterCount(){
        Map<String, Integer> map = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LetterEntity.TABLE_NAME, null);
        Log.e("KEYMAP", cursor.getCount()+"");

        while(cursor.moveToNext()){
            Log.e("KEYMAP", cursor.getString(0));
            map.put(cursor.getString(0), cursor.getInt(1));
        }
        cursor.close();
        db.close();
        return map;
    }

    public void addWord(String word, int score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WordEntity.COLUMN_NAME_WORD, word);
        cv.put(WordEntity.COLUMN_NAME_SCORE, score);
        db.insert(WordEntity.TABLE_NAME, null, cv);
        db.close();
    }

    public void removeLetters(Map<String, Integer> mapWord) {
        for (Map.Entry<String, Integer> e : mapWord.entrySet()){
            int countDel = e.getValue();
            String letter = e.getKey();
            addLetter(letter, -countDel);
        }
    }

    public List<WordScore> getWordScores() {
        List<WordScore> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordEntity.TABLE_NAME +
                " ORDER BY " + WordEntity.COLUMN_NAME_SCORE +" DESC", null);

        while(cursor.moveToNext()){
            String word = cursor.getString(1);
            int score = cursor.getInt(2);
            list.add(new WordScore(word, score));
        }
        cursor.close();
        return list;
    }

    public List<WordScore> getLetterCounts() {
        List<WordScore> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LetterEntity.TABLE_NAME, null);
        while(cursor.moveToNext()){
            String word = cursor.getString(0);
            int score = cursor.getInt(1);
            list.add(new WordScore(word, score));
        }
        cursor.close();
        return list;
    }
}
