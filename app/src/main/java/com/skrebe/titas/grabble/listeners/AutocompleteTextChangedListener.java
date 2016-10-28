package com.skrebe.titas.grabble.listeners;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

/**
 * Created by titas on 16.10.1.
 */
public class AutocompleteTextChangedListener implements TextWatcher {

    private TextInputLayout infoView;

    public AutocompleteTextChangedListener(TextInputLayout infoView){
        this.infoView = infoView;
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
    }
}
