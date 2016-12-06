package com.skrebe.titas.grabble;

public class WordScore {
    private String word;
    private int score;

    public WordScore(String word, int score) {
        this.score = score;
        this.word = word;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word.toUpperCase() + "=" + score;
    }
}
