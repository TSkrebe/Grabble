package com.skrebe.titas.grabble;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.skrebe.titas.grabble.adapters.GridViewAdapter;
import com.skrebe.titas.grabble.listeners.AutocompleteTextChangedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LetterActivity extends AppCompatActivity {


    private AutoCompleteTextView autoCompleteTextView;
    private GridView gridView;
    private Set<String> dictionary;
    private TextInputLayout textInputLayout;
    private BaseAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputStream inputStream = getResources().openRawResource(R.raw.grabble);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> words = new ArrayList<>();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null){
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionary = new HashSet<>(words);

        gridView = (GridView)findViewById(R.id.gridview);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, words);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.addTextChangedListener(new AutocompleteTextChangedListener(textInputLayout));

        DatabaseHelper db = new DatabaseHelper(this);
        List<WordScore> list2 = db.getLetterCounts();
        gridAdapter = new GridViewAdapter(this, list2);
        gridView.setAdapter(gridAdapter);

    }


    public void addWord(View view){
        String word = autoCompleteTextView.getText().toString();
        if(word.length() < 7){
            textInputLayout.setError("Word must have 7 letters");

            return;
        }
        if(!dictionary.contains(word)){
            textInputLayout.setError("does not exit in the dictionary");
            return;
        }
        if(!enoughLetters(word)){
            textInputLayout.setError("Collect more letters");
            return;
        }
        int score = Helper.wordScore(word);
        autoCompleteTextView.setText("");

        DatabaseHelper db = new DatabaseHelper(this);


        db.addWord(word, score);

//        wordBankAdapter.clear();
//        wordBankAdapter.addAll(db.getLetterCounts());

    }

    private boolean enoughLetters(String word) {
        word = word.toLowerCase();
        DatabaseHelper db = new DatabaseHelper(this);
        Map<String, Integer> mapDB = db.getAllLetterCount(), mapWord = new HashMap<>();
        for(int i = 0; i < word.length(); i++){
            String c = word.charAt(i) + "";
            int count = (mapWord.get(c) == null) ? 0 : mapWord.get(c);
            mapWord.put(c, count + 1);
        }

        for (Map.Entry<String, Integer> e : mapWord.entrySet()){
            int charCount = e.getValue();
            Log.e("KEY", e.getKey());
            if(mapDB.get(e.getKey()) < charCount){
                Log.e("COUNT", e.getKey() + " " + mapDB.get(e.getKey()) + " " + charCount);
                return false;
            }
        }
        db.removeLetters(mapWord);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
