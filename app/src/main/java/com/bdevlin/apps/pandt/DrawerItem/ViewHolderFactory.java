package com.bdevlin.apps.pandt.DrawerItem;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by brian on 10/12/2015.
 */
public interface ViewHolderFactory<T extends RecyclerView.ViewHolder> {
    T factory(View v);
}
