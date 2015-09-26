package com.bdevlin.apps.pandt;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

/**
 * Created by brian on 9/26/2015.
 */
public class PrimaryDrawerItem extends BasePrimaryDrawerItem<PrimaryDrawerItem>  {

    public PrimaryDrawerItem(Cursor c)
    {

    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.textview;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();
    }

    public static class ViewHolder extends BaseViewHolder {
        private View badgeContainer;
        private TextView badge;

        public ViewHolder(View view, Context ctx) {
            super(view, ctx);
        }
    }
}
