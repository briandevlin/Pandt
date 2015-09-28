package com.bdevlin.apps.pandt;

import android.view.View;

/**
 * Created by bdevlin on 9/27/2015.
 */
public interface OnPostBindViewListener {
    /**
     * allows you to hook in the BindView method and modify the view after binding
     *
     * @param drawerItem the drawerItem used for this view
     * @param view       the view which will be set
     */
    void onBindView(IDrawerItem drawerItem, View view);
}
