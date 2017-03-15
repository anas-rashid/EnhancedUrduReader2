package com.example.manan.enhancedurdureader.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by manan on 3/9/17.
 */

public class NastaleeqTextView extends TextView {

    public NastaleeqTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NastaleeqTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NastaleeqTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nastaleeq.ttf");
        setTypeface(tf);
    }

}
