package com.skrebe.titas.grabble.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skrebe.titas.grabble.CustomAutoTextView;
import com.skrebe.titas.grabble.R;
import com.skrebe.titas.grabble.entities.WordScore;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private List<WordScore> letters;
    private Context context;
    private CustomAutoTextView textView;
    private int size;

    public GridViewAdapter(Context context, List<WordScore> letters, CustomAutoTextView autoCompleteTextView){
        this.letters = letters;
        this.context = context;
        this.textView = autoCompleteTextView;
        this.size = letters.size();
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int position) {
        return letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.grid_view_item, parent, false);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.insertProgrammatically(v);

            }
        });
        String[] data = letters.get(position).toString().split("=");

        TextView letterView = (TextView)row.findViewById(R.id.grid_cell_letter);
        letterView.setText(data[0]);

        TextView scoreView = (TextView)row.findViewById(R.id.grid_cell_score);
        scoreView.setText(data[1]);
        //delete button
        if(size == position+1)
            scoreView.setText("");

        return row;
    }

    public void setLetters(List<WordScore> wordScores){
        this.letters = wordScores;
    }


}
