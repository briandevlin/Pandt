package com.bdevlin.apps.pandt.DrawerItem;

import android.support.annotation.DrawableRes;

import com.bdevlin.apps.pandt.DrawerItem.AbstractDrawerItem;
import com.bdevlin.apps.ui.widgets.ImageHolder;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class BaseDrawerItem<T> extends AbstractDrawerItem<T> {
    protected ImageHolder icon;

    public void setIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);

    }

    public ImageHolder getIcon() {
        return icon;
    }

}
