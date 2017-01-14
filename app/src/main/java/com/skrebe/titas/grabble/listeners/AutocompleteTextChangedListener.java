package com.skrebe.titas.grabble.listeners;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.skrebe.titas.grabble.adapters.GridViewAdapter;
import com.skrebe.titas.grabble.entities.WordScore;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteTextChangedListener implements TextWatcher {

    private TextInputLayout infoView;
    private List<WordScore> gridList;
    private GridViewAdapter gridAdapter;

    public AutocompleteTextChangedListener(TextInputLayout infoView, List<WordScore> gridList, GridViewAdapter gridAdapter){
        this.infoView = infoView;
        this.gridList = gridList;
        this.gridAdapter = gridAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        infoView.setError(null);
        infoView.setErrorEnabled(false);
        List<WordScore> listLive = liveList(gridList, s.toString());
        gridAdapter.setLetters(listLive);
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * Returns current situation of number of letters available
     */
    private List<WordScore> liveList(List<WordScore> gridList, String s) {
        s = s.toLowerCase();
        List<WordScore> liveList = new ArrayList<>();
        for(WordScore ws : gridList){
            int nochars = s.length() - s.replaceAll(ws.getWord(), "").length();
            liveList.add(new WordScore(ws.getWord(), ws.getScore()-nochars));
        }
        return liveList;
    }
}
