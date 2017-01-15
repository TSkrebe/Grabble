package com.skrebe.titas.grabble;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skrebe.titas.grabble.adapters.SimpleListAdapter;
import com.skrebe.titas.grabble.entities.WordScore;
import com.skrebe.titas.grabble.helpers.DatabaseHelper;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseHelper db = new DatabaseHelper(this);

        List<WordScore> wordsAndScores = db.getWordScores();

        ListView wordList = (ListView) findViewById(R.id.wordList);
        ArrayAdapter<WordScore> listAdapter = new SimpleListAdapter(this, R.layout.simple_list_layout_item, wordsAndScores);
        wordList.setAdapter(listAdapter);

        int overall = overallScore(wordsAndScores);
        int noOfWords = wordsAndScores.size();
        int noOfDays = PreferenceManager.getDefaultSharedPreferences(this).getInt("daysPlayed", 1);
        float averagePerWord = (float)overall/noOfWords;
        float averagePerDay = (float)overall/noOfDays;

        TextView averageScore = (TextView) findViewById(R.id.averageScore);
        averageScore.setText(averagePerWord+"");

        TextView overallScore = (TextView) findViewById(R.id.overallScore);
        overallScore.setText(overall+ "");

        TextView numberOfWords = (TextView) findViewById(R.id.numberOfWords);
        numberOfWords.setText(noOfWords+"");

        TextView daysPlayed = (TextView) findViewById(R.id.daysPlayed);
        daysPlayed.setText(noOfDays+"");

        TextView averageDayScore = (TextView) findViewById(R.id.averageDayScore);
        averageDayScore.setText(averagePerDay+"");


    }

    private int overallScore(List<WordScore> wordsAndScores) {
        int scores = 0;
        for (WordScore wordScore: wordsAndScores){
            scores += wordScore.getScore();
        }
        return scores;
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
