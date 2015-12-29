package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;


/**
 * Created by brian on 9/26/2015.
 */
public abstract class AbstractDrawerItem<T> implements IDrawerItem<T> {

    protected OnPostBindViewListener mOnPostBindViewListener = null;
    protected boolean mSelected = false;



    @Override
    public boolean isSelected() {
        return mSelected;
    }


    public void setSelected(boolean selected) {
        this.mSelected = selected;

    }


    public OnPostBindViewListener getOnPostBindViewListener() {
        return mOnPostBindViewListener;
    }

    public void  setPostOnBindViewListener(OnPostBindViewListener onPostBindViewListener) {
        this.mOnPostBindViewListener = onPostBindViewListener;

    }

    public void onPostBindView(IDrawerItem drawerItem, View view) {
        if (mOnPostBindViewListener != null) {
            mOnPostBindViewListener.onBindView(drawerItem, view);
        }
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder = getFactory().factory(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    public abstract ViewHolderFactory getFactory();

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        return getFactory().factory(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }
}
