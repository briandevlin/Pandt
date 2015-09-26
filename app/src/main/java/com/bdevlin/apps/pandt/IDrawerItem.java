package com.bdevlin.apps.pandt;

import android.support.v7.widget.RecyclerView;

/**
 * Created by brian on 9/26/2015.
 */
public interface IDrawerItem<T> {
    void bindView(RecyclerView.ViewHolder holder);
}
