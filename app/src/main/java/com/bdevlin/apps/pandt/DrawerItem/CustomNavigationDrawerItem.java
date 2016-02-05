package com.bdevlin.apps.pandt.DrawerItem;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.widgets.ColorHolder;

/**
 * Created by brian on 1/23/2016.
 */
public class CustomNavigationDrawerItem extends NavigationDrawerItem {

    private ColorHolder background;

    public CustomNavigationDrawerItem(ControllableActivity activity, Cursor c) {
        super(activity, c);
        //this.background = ColorHolder.fromColorRes(R.color.material_drawer_background);
    }
    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        super.bindView(holder);
        if (background != null) {
            background.applyToBackground(holder.itemView);
        }

    }
}
