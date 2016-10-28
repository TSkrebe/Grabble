package com.skrebe.titas.grabble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.skrebe.titas.grabble.adapters.SimpleListAdapter;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private ListView wordList;
    private ArrayAdapter<WordScore> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHelper db = new DatabaseHelper(this);

        List<WordScore> wordsAndScores = db.getWordScores();

        wordList = (ListView)findViewById(R.id.wordList);
        listAdapter = new SimpleListAdapter(this, R.layout.simple_list_layout_item, wordsAndScores);
        wordList.setAdapter(listAdapter);

        float averagePerWord = scorePerWord(wordsAndScores);
        WordScore minimumWordScore = minimumWordScore(wordsAndScores);
        WordScore maximumWordScore = maximumWordScore(wordsAndScores);
        int noOfWords = wordsAndScores.size();


    }


    private WordScore maximumWordScore(List<WordScore> wordsAndScores) {
        int score = -1;
        WordScore wordsScore = null;
        for(WordScore ws: wordsAndScores){
            if(ws.getScore() < score) {
                wordsScore = ws;
                score = ws.getScore();
            }
        }
        return wordsScore;

    }

    private WordScore minimumWordScore(List<WordScore> wordsAndScores) {
        int score = 7*26+1;
        WordScore wordsScore = null;
        for(WordScore ws: wordsAndScores){
            if(ws.getScore() < score) {
                wordsScore = ws;
                score = ws.getScore();
            }
        }
        return wordsScore;
    }


    private float scorePerWord(List<WordScore> wordsAndScores) {
        float score = 0;
        for(WordScore wordScore: wordsAndScores){
            score += wordScore.getScore();
        }
        return score/wordsAndScores.size();
    }


}
