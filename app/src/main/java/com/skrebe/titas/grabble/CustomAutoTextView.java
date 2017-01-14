package com.skrebe.titas.grabble;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CustomAutoTextView extends AutoCompleteTextView {

    public CustomAutoTextView(Context context) {
        super(context);
    }

    public CustomAutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void insertProgrammatically(View v) {

        ListAdapter adapter = getAdapter();
        setAdapter(null);
        String letter = ((TextView)v.findViewById(R.id.grid_cell_letter)).getText().toString();

        if(letter.equals("<-")){    //delete character
            if(getSelectionStart() != 0) {
                getText().delete(getSelectionStart() - 1, getSelectionStart());
            }
        }else{                      //add character
            getText().insert(getSelectionStart(), letter.toLowerCase());
        }
        setAdapter((ArrayAdapter)adapter);
    }
}
