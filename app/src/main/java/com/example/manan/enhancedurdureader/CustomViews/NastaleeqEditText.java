package com.example.manan.enhancedurdureader.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by manan on 3/9/17.
 */

public class NastaleeqEditText extends EditText {
    public NastaleeqEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NastaleeqEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NastaleeqEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nastaleeq.ttf");
            setTypeface(tf);
        }
    }
}

