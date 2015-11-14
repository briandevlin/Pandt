package com.bdevlin.apps.ui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bdevlin.apps.utils.Utils;

/**
 * Created by brian on 11/14/2015.
 */
public class ImageHolder {
    private int mIconRes = -1;
    private Drawable mIcon;

    public ImageHolder(@DrawableRes int iconRes) {
        this.mIconRes = iconRes;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public static Drawable decideIcon(ImageHolder imageHolder, Context ctx) {
        if (imageHolder == null) {
            return null;
        } else {
            return imageHolder.decideIcon(ctx);
        }
    }

    public Drawable decideIcon(Context ctx) {
        Drawable icon = mIcon;

            icon = Utils.getCompatDrawable(ctx, mIconRes);

        return icon;
    }

    public static void applyMultiIconTo(Drawable icon,  ImageView imageView) {
        //if we have an icon then we want to set it
        if (icon != null) {
            imageView.setImageDrawable(icon);
            //make sure we display the icon
            imageView.setVisibility(View.VISIBLE);
        } else {
            //hide the icon
            //imageView.setVisibility(View.GONE);
        }
    }

}
