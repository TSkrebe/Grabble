package com.skrebe.titas.grabble.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skrebe.titas.grabble.R;
import com.skrebe.titas.grabble.WordScore;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by titas on 16.10.26.
 */
public class GridViewAdapter extends BaseAdapter {

    private List<WordScore> letters;
    private Context context;
    public GridViewAdapter(Context context, List<WordScore> letters){
        this.letters = letters;
        this.context = context;
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

        String[] data = letters.get(position).toString().split("-");
        TextView letterView = (TextView)row.findViewById(R.id.grid_cell_letter);
        letterView.setText(data[0]);
        TextView scoreView = (TextView)row.findViewById(R.id.grid_cell_score);
        scoreView.setText(data[1]);

        return row;
    }
}
