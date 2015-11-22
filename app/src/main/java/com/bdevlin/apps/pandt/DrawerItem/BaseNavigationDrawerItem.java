package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.DrawerItem.BaseDrawerItem;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.widgets.ImageHolder;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class BaseNavigationDrawerItem<T> extends BaseDrawerItem<T> {

    protected void bindViewHelper(BaseViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        viewHolder.itemView.setTag(this);
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx);

        ImageHolder.applyMultiIconTo(icon, viewHolder.icon);


    }

    protected static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageView icon;
        protected TextView name;
        //protected TextView name2;
        protected TextView id;


        public BaseViewHolder(View view) {
            super(view);

            this.view = view;

            this.icon = (ImageView) view.findViewById(R.id.imageview2);
            this.name = (TextView) view.findViewById(R.id.name);
            this.id = (TextView) view.findViewById(R.id.id);
        }
    }
}
