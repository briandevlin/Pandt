package com.bdevlin.apps.pandt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class BasePrimaryDrawerItem<T> extends BaseDrawerItem<T> {

    protected static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageView icon;
        protected TextView name;
        protected TextView description;

        public BaseViewHolder(View view) {
            super(view);

            this.view = view;

            this.icon = (ImageView) view.findViewById(R.id.imageView2);
//            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
//            this.description = (TextView) view.findViewById(R.id.material_drawer_description);
        }
    }
}
