package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.widgets.ColorHolder;
import com.bdevlin.apps.ui.widgets.ImageHolder;
import com.bdevlin.apps.ui.widgets.StringHolder;

/**
 * Created by brian on 9/26/2015.
 */
public abstract class BaseDrawerItem<T> extends AbstractDrawerItem<T> {

    protected ImageHolder imageHolder;
    protected ImageHolder selectedIcon;
    protected int baseId;
    protected StringHolder baseName;
    protected String baseUri;
    protected ColorHolder iconColor = ColorHolder.fromColorRes(R.color.material_drawer_primary_icon);
    protected boolean iconTinted = true;
    protected ColorHolder selectedColor = ColorHolder.fromColorRes(R.color.material_drawer_selected);
    protected ColorHolder textColor = ColorHolder.fromColorRes(R.color.material_drawer_primary_text);
    protected ColorHolder selectedTextColor = ColorHolder.fromColorRes(R.color.material_drawer_selected_text);
    protected ColorHolder disabledTextColor;
    protected ColorHolder selectedIconColor = ColorHolder.fromColorRes(R.color.material_drawer_selected_icon);
    protected ColorHolder disabledIconColor;

    public void setImageHolder(@DrawableRes int iconRes) {
        this.imageHolder = new ImageHolder(iconRes);
    }

    public void setSelectedIcon(@DrawableRes int selectedIconRes) {
        this.selectedIcon = new ImageHolder(selectedIconRes);
    }

    public ImageHolder getImageHolder() {
        return imageHolder;
    }

    public StringHolder getBaseName() {
        return baseName;
    }

    public void setBaseName(StringHolder baseName) {
        this.baseName = baseName;
    }

    public String geturiString() {
        return baseUri;
    }


    public int getNavId() {
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public ColorHolder getSelectedColor() {
        return selectedColor;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public ColorHolder getSelectedTextColor() {
        return selectedTextColor;
    }

    public ColorHolder getDisabledTextColor() {
        return disabledTextColor;
    }
    public ImageHolder getSelectedIcon() {
        return selectedIcon;
    }
    public ColorHolder getIconColor() {
        return iconColor;
    }
    public void setIconColor(ColorHolder color) {
        this.iconColor = color;
    }
    public ColorHolder getSelectedIconColor() {
        return selectedIconColor;
    }

    protected int getSelectedIconColor(Context ctx) {
        return ColorHolder.color(getSelectedIconColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    protected int getSelectedColor(Context ctx) {
        return ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
    }

    protected int getSelectedTextColor(Context ctx) {
        return ColorHolder.color(getSelectedTextColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    protected int getColor(Context ctx) {
        int color;
//        if (this.isEnabled()) {
            color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
//        } else {
//            color = ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
//        }
        return color;
    }

    public int getIconColor(Context ctx) {
        int iconColor;
//        if (this.isEnabled()) {
            iconColor = ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
//        } else {
//            iconColor = ColorHolder.color(getDisabledIconColor(), ctx, R.attr.material_drawer_hint_icon, R.color.material_drawer_hint_icon);
//        }
        return iconColor;
    }

    public boolean isIconTinted() {
        return iconTinted;
    }
}
