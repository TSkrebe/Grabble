package com.skrebe.titas.grabble.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.skrebe.titas.grabble.R;
import com.skrebe.titas.grabble.WordScore;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by titas on 16.10.1.
 */
public class SimpleListAdapter extends ArrayAdapter<WordScore> {

    private Context context;
    private List<WordScore> values;
    private int layout;

    public SimpleListAdapter(Context context, int layout, List<WordScore> objects) {
        super(context, layout, objects);
        this.context = context;
        this.values = objects;
        this.layout = layout;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(this.layout, parent, false);
        TextView word = (TextView)row.findViewById(R.id.word);
        TextView score = (TextView)row.findViewById(R.id.word_score);
        word.setText(values.get(position).getWord());
        score.setText(values.get(position).getScore()+"");
        return row;
    }
}
