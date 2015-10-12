package com.bdevlin.apps.pandt.Cursors;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.DrawerItem.MainContentDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;

/**
 * Created by brian on 10/12/2015.
 */
public class ContentCursorRecyclerAdapter
extends ContentBaseRecyclerViewAdapter<MainContentDrawerItem.ContentItemViewHolder>
{
    private static final String TAG = ContentCursorRecyclerAdapter.class.getSimpleName();

    Context mContext;
    protected  int[] mFrom;
    protected  int[] mTo;
    protected  String[] mOriginalFrom;
    /*private final OnStartDragListener mDragStartListener;*/
    private ControllableActivity mActivity;

    public ContentCursorRecyclerAdapter(ControllableActivity activity,
                                        ObjectCursor<MainContentDrawerItem> c,
                                        String[] from,
                                        int[] to) {
        super(c);
        this.mActivity = activity;
        /*mDragStartListener = dragStartListener;*/
        mContext = mActivity.getActivityContext();
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
    }

    @Override
    public MainContentDrawerItem.ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        MainContentDrawerItem item = new    MainContentDrawerItem(mActivity,null);
        return (MainContentDrawerItem.ContentItemViewHolder)item.getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(MainContentDrawerItem.ContentItemViewHolder holder, ObjectCursor<MainContentDrawerItem> cursor, int position) {
        Log.d(TAG,"onBindViewHolder");
        // gets the IDrawerItem at this position then bind the viewholder to it
        getItem(position).bindView(holder);
    }

    private void findColumns(ObjectCursor<MainContentDrawerItem> c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }
}
