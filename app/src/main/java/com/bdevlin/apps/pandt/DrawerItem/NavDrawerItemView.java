package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 12/26/2015.
 */
public class NavDrawerItemView extends LinearLayout {
    private ColorStateList mIconTints;
    public NavDrawerItemView(Context context) {
        this(context, null);
    }

    public NavDrawerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavDrawerItemView);
        if (a.hasValue(R.styleable.NavDrawerItemView_iconTints)) {
            mIconTints = a.getColorStateList(R.styleable.NavDrawerItemView_iconTints);
        }
    }
    public Drawable setContent(@DrawableRes int iconResId) {
        if (iconResId > 0) {
            Drawable icon = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), iconResId));
            if (mIconTints != null) {
                DrawableCompat.setTintList(icon, mIconTints);
                icon=icon.mutate();
                return icon;
            }
            //((ImageView) findViewById(R.baseId.imageHolder)).setImageDrawable(imageHolder);
        }
        //((TextView) findViewById(R.baseId.title)).setText(titleResId);
        return null;
    }
}
