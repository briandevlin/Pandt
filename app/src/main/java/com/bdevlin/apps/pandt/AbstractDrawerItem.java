package com.bdevlin.apps.pandt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class AbstractDrawerItem<T> implements IDrawerItem<T> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false);

        //return getFactory().factory(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
        return null;
    }
}
