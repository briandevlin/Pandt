package com.bdevlin.apps.ui.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;


public class PageMarginDrawable extends InsetDrawable {

    private final Paint mPaint;

    public PageMarginDrawable(Drawable drawable, int insetLeft, int insetTop,
                              int insetRight, int insetBottom, int color) {
        super(drawable, insetLeft, insetTop, insetRight, insetBottom);
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), mPaint);
        super.draw(canvas);
    }
}
