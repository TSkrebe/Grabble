package com.skrebe.titas.grabble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skrebe.titas.grabble.adapters.SimpleListAdapter;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private ListView wordList;
    private ArrayAdapter<WordScore> listAdapter;
    private TextView averageScore, overallScore, numberOfWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseHelper db = new DatabaseHelper(this);

        List<WordScore> wordsAndScores = db.getWordScores();

        wordList = (ListView)findViewById(R.id.wordList);
        listAdapter = new SimpleListAdapter(this, R.layout.simple_list_layout_item, wordsAndScores);
        wordList.setAdapter(listAdapter);

        float averagePerWord = scorePerWord(wordsAndScores);
        int overall = overallScore(wordsAndScores);
        int noOfWords = wordsAndScores.size();

        averageScore = (TextView)findViewById(R.id.averageScore);
        averageScore.setText(averagePerWord+"");
        overallScore = (TextView)findViewById(R.id.overallScore);
        overallScore.setText(overall+ "");
        numberOfWords = (TextView)findViewById(R.id.numberOfWords);
        numberOfWords.setText(noOfWords+"");
    }

    private int overallScore(List<WordScore> wordsAndScores) {
        int scores = 0;
        for (WordScore wordScore: wordsAndScores){
            scores += wordScore.getScore();
        }
        return scores;
    }



    private float scorePerWord(List<WordScore> wordsAndScores) {
        float score = 0;
        for(WordScore wordScore: wordsAndScores){
            score += wordScore.getScore();
        }
        return score/wordsAndScores.size();
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