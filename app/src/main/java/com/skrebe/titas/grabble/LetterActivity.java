package com.skrebe.titas.grabble;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.skrebe.titas.grabble.adapters.GridViewAdapter;
import com.skrebe.titas.grabble.adapters.SuggestionsArrayAdapter;
import com.skrebe.titas.grabble.entities.WordScore;
import com.skrebe.titas.grabble.helpers.DatabaseHelper;
import com.skrebe.titas.grabble.helpers.Helper;
import com.skrebe.titas.grabble.listeners.AutocompleteTextChangedListener;

import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterActivity extends AppCompatActivity {


    private CustomAutoTextView autoCompleteTextView;
    private GridView gridView;
    private TextInputLayout textInputLayout;
    private List<WordScore> gridList;
    private PatriciaTrie<String> words;
    private GridViewAdapter gridAdapter;
    private ArrayAdapter<String> autoCompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputStream inputStream = getResources().openRawResource(R.raw.grabble);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        words = new PatriciaTrie<>();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null){
                words.put(line, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridView = (GridView)findViewById(R.id.gridview);
        autoCompleteTextView = (CustomAutoTextView)findViewById(R.id.autoCompleteTextView);
        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);

        DatabaseHelper db = new DatabaseHelper(this);
        gridList = db.getLetterCounts();
        gridList.add(new WordScore("<-", 0));

        gridAdapter = new GridViewAdapter(this, gridList, autoCompleteTextView);
        gridView.setAdapter(gridAdapter);

        autoCompleteAdapter = new SuggestionsArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, words, gridList);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        autoCompleteTextView.addTextChangedListener(new AutocompleteTextChangedListener(textInputLayout, gridList, gridAdapter));

    }


    public void addWord(View view){
        String word = autoCompleteTextView.getText().toString();
        if(word.length() < 7){
            textInputLayout.setError(getString(R.string.wordConstraint7));
            return;
        }
        if(words.get(word) == null){
            textInputLayout.setError(word + getString(R.string.wordConstraintExists));
            return;
        }
        if(!enoughLetters(word)){
            textInputLayout.setError(getString(R.string.wordConstraintLetters));
            return;
        }
        int score = Helper.wordScore(word);
        autoCompleteTextView.setText("");

        Snackbar.make(view, "Word " + word + " was added", Snackbar.LENGTH_LONG).setActionTextColor(Color.GREEN).show();
        DatabaseHelper db = new DatabaseHelper(this);

        db.addWord(word, score);

        gridList.clear();
        gridList.addAll(db.getLetterCounts());
        gridList.add(new WordScore("<-", 0));

        gridAdapter.notifyDataSetChanged();
        autoCompleteAdapter.notifyDataSetChanged();
        gridAdapter.setLetters(gridList);
        gridView.invalidateViews();
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
            if(mapDB.get(e.getKey()) < charCount){
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
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
