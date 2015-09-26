package com.bdevlin.apps.pandt;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by brian on 9/26/2015.
 */
public class PrimaryDrawerItem extends BasePrimaryDrawerItem<PrimaryDrawerItem>  {

    public PrimaryDrawerItem(Cursor c)
    {

    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();
    }
}
