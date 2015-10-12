package com.bdevlin.apps.pandt.DrawerItem;

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
    //protected Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener = null;

    //public abstract NavigationDrawerItem.ItemFactory getFactory();


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

    public abstract ViewHolderFactory getFactory();

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
      /*  View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false);

        NavigationDrawerItem.ItemFactory factory = getFactory();

        NavigationDrawerItem.ListItemViewHolder factory1 = factory.factory(v);*/
        return getFactory().factory(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

//    @Override
//    public NavigationDrawerItem.ListItemViewHolder getViewHolder(ViewGroup parent) {
//
////        View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false);
//
////        PrimaryDrawerItem.ItemFactory factory = getFactory();
////
////        PrimaryDrawerItem.ListItemViewHolder factory1 = factory.factory(v);
////
////return factory1;
//        return getFactory().factory(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
//
//    }
}
