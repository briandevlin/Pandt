package com.bdevlin.apps.ui.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by brian on 2/20/2016.
 */
public interface CollectionViewCallbacks {

    /**
     * Returns a new custom View that will be used for each of the collection group headers.
     */
    View newCollectionHeaderView(Context context, int groupId, ViewGroup parent);

    /**
     * Binds the given data (like the header label) with the given collection group header View.
     */
    void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel,
                                  Object headerTag);

    /**
     * Returns a new custom View that will be used for each of the collection item.
     */
    View newCollectionItemView(Context context, int groupId, ViewGroup parent);

    /**
     * Binds the given data with the given collection item View.
     */
    void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup,
                                int dataIndex, Object tag);

    /**
     * Can be used in place of a {@code CollectionViewCallbacks} to define a custom layout for each
     * groups.
     */
    public static interface GroupCollectionViewCallbacks extends CollectionViewCallbacks {

        /**
         * Returns the custom ViewGroup to be used as a container for each group of the
         * {@link CollectionView}. For example a {@link android.support.v7.widget.CardView} could be
         * returned.
         */
        ViewGroup newCollectionGroupView(Context context, int groupId, CollectionView.InventoryGroup group, ViewGroup parent);
    }
}