package com.ingreens.recycleswipedelete;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jeeban on 19/6/18.
 */

public class Interfaces {

    public static interface changeView{
        public void onSwipeViewChange(ImageView leftImageView,TextView leftTextView,ImageView rightImageView,TextView rightTextView);

    }
}
