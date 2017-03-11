package com.legacy.apppolicia.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ivan on 25/01/2017.
 */
public class SquareWImageView extends ImageView {

    public SquareWImageView(Context context) {
        super(context);
    }

    public SquareWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareWImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

}