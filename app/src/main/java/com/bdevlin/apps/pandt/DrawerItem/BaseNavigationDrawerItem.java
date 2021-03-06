package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.widgets.ImageHolder;
import com.bdevlin.apps.ui.widgets.StringHolder;
import com.bdevlin.apps.utils.Utils;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class BaseNavigationDrawerItem<T> extends BaseDrawerItem<T> {

    protected void bindViewHelper(BaseViewHolder viewHolder) {

        Context ctx = viewHolder.itemView.getContext();

        viewHolder.itemView.setTag(this);


            //set the item selected if it is
            viewHolder.itemView.setSelected(isSelected());


        //get the correct color for the text
        int color = getColor(ctx);
        int selectedTextColor = getSelectedTextColor(ctx);

        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);

        int selectedIconColor = getSelectedIconColor(ctx);
        if (this instanceof NavigationDrawerItem) {
            Utils.setBackground(viewHolder.view, Utils.getSelectableBackground(ctx, selectedColor));
        } else {
            viewHolder.name2.setTextColor(Utils.getTextColorStateList(color, selectedTextColor));
        }
       // Utils.setBackground(viewHolder.view, Utils.getSelectableBackground(ctx, selectedColor));

        //set the colors for textViews
        viewHolder.name.setTextColor(Utils.getTextColorStateList(color, selectedTextColor));

        viewHolder.id.setText(String.valueOf(getNavId()));

        //set the text for the baseName
        StringHolder.applyTo(this.getBaseName(), viewHolder.name);

       //set the text for the subName
        StringHolder.applyTo(this.getListuriString(), viewHolder.name2);

        // this takes care of the cursor icons
        if (baseIcon != null) {
            int resId = ctx.getResources().getIdentifier(baseIcon, "drawable", ctx.getPackageName());
            this.setImageHolder(resId);
        }
        int iconColor = getIconColor(ctx);

       // Drawable icon = ImageHolder.decideIcon(getImageHolder(), ctx);
        Drawable icon = ImageHolder.decideIcon(getImageHolder(), ctx, iconColor, isIconTinted(), 1);

        //ImageHolder.applyMultiIconTo(icon, viewHolder.imageView);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.imageView);


    }

    protected static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageView imageView;
        protected TextView name;
        protected TextView name2;

        protected TextView id;


        public BaseViewHolder(View view) {
            super(view);

            this.view = view;

            this.imageView = (ImageView) view.findViewById(R.id.imageview2);
            this.name = (TextView) view.findViewById(R.id.baseName);
            this.name2 = (TextView) view.findViewById(R.id.name2);
            this.id = (TextView) view.findViewById(R.id.id);
        }
    }
}
