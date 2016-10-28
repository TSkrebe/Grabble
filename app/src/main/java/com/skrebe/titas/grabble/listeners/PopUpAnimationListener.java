package com.skrebe.titas.grabble.listeners;

import android.view.animation.Animation;
import android.widget.TextView;


/**
 * Created by titas on 16.10.1.
 */
public class PopUpAnimationListener implements Animation.AnimationListener {

    private TextView popUp;

    public PopUpAnimationListener(TextView popUp){
        this.popUp = popUp;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        popUp.setText("");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
