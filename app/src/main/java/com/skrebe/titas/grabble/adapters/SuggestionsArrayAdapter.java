package com.skrebe.titas.grabble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.skrebe.titas.grabble.entities.WordScore;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by titas on 17.1.10.
 */

public class SuggestionsArrayAdapter extends ArrayAdapter<String> {

    private PatriciaTrie<String> words;
    private List<String> suggestions;
    private List<WordScore> letterCounts;
    private Filter filter = new CustomFilter();

    public SuggestionsArrayAdapter(Context context, int resource, PatriciaTrie<String> words, List<WordScore> letterCounts) {
        super(context, resource, new ArrayList<String>());
        this.suggestions = new ArrayList<>();
        this.letterCounts = letterCounts;
        this.words = words;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }


    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            suggestions.clear();

            if(constraint != null){

                String prefix = constraint.toString().toLowerCase();
                String prefixUpper =  prefix.substring(0, 1).toUpperCase() + prefix.substring(1);

                SortedMap<String, String> prefixesUpper = words.prefixMap(prefixUpper);
                SortedMap<String, String> prefixes = words.prefixMap(prefix);

                addSuggestions(prefixesUpper, suggestions);
                addSuggestions(prefixes, suggestions);

                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }

        private void addSuggestions(SortedMap<String, String> prefixes, List<String> suggestions) {
            for (String word : prefixes.keySet()){
                if(enoughLetters(word.toLowerCase())) {
                    suggestions.add(word);
                }
            }
        }

        private boolean enoughLetters(String word) {
            for (WordScore ws : letterCounts){
                int count = StringUtils.countMatches(word, ws.getWord());
                if(count > ws.getScore()) return false;
            }
            return true;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filtered = (List<String>)results.values;
            if(results != null && results.count > 0) {
                Collections.sort(filtered, String.CASE_INSENSITIVE_ORDER);
                clear();
                addAll(filtered);
                notifyDataSetChanged();
            }
        }
    }


}
