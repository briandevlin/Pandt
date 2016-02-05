package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.Utils;

/**
 * Created by brian on 10/24/2015.
 */
public class DividerDrawerItem extends AbstractDrawerItem<DividerDrawerItem>  {
    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    @Override
    public String getType() {
        return "DIVIDER_ITEM";
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();

        //get our viewHolder
        ViewHolder viewHolder = (ViewHolder) holder;

        //set the identifier from the drawerItem here. It can be used to run tests
       // holder.itemView.setBaseId(getIdentifier());

        //define how the divider should look like
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);
        viewHolder.view.setMinimumHeight(1);
        viewHolder.itemView.setClickable(false);
        viewHolder.itemView.setEnabled(false);
        viewHolder.itemView.setMinimumHeight(1);

        //set the color for the divider
        viewHolder.divider.setBackgroundColor(Utils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
       // int accent = ctx.getResources().getColor(R.color.colorAccent);

       // viewHolder.divider.setBackgroundColor( ctx.getResources().getColor(R.color.colorAccent));
        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.drawer_divider;
    }

    public static class ItemFactory implements ViewHolderFactory<RecyclerView.ViewHolder> {
        public RecyclerView.ViewHolder factory(View v) {
            return new ViewHolder(v);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private View divider;
        private View name;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.material_drawer_divider);
            this.name =  view.findViewById(R.id.material_drawer_name);
        }
    }
}
