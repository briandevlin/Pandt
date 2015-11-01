package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by brian on 9/26/2015.
 */
public interface IDrawerItem<T> {
    String getType();
    void bindView(RecyclerView.ViewHolder holder);
    int getLayoutRes();
    View generateView(Context ctx, ViewGroup parent);
    RecyclerView.ViewHolder getViewHolder(ViewGroup parent);
}
