package com.bdevlin.apps.pandt.Adapters;
import android.app.Application;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.DrawerItem.DividerDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.folders.Folder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 9/15/2015.
 */
// Create the base adapter extending from RecyclerView.Adapter
    // the viewholder VH will be from the internal drawerItem viewholder
public abstract class NavigationBaseRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>   {

    // <editor-fold desc="Fields">
    private static final String TAG = NavigationBaseRecyclerAdapter.class.getSimpleName();
    protected boolean mDataValid;
    protected ObjectCursor<NavigationDrawerItem> mCursor;
    protected int mRowIDColumn;
    private List<IDrawerItem> mDrawerItems = new ArrayList<>();
    public  OnItemClickListener mOnItemClickListener ;
    public OnClickListener mOnClickListener;
    private Application application;
    private int previousSelection = -1;
    private static View previousView;
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnClickListener {
        void onClick(View v, int position, IDrawerItem item);
    }

    // </editor-fold>

    // <editor-fold desc="Constructor & init()">
    public NavigationBaseRecyclerAdapter(/*HomeActivity activity,*/ ObjectCursor<NavigationDrawerItem> c) {
       /* Context applicationContext = activity.getApplicationContext();
         application = activity.getApplication();*/
        // cursor will be null at construction; the loader will swap in the cursor when loaded
        init(c);
    }


    void init(ObjectCursor<NavigationDrawerItem> c) {
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        setHasStableIds(true);
    }
    // </editor-fold>

    // <editor-fold desc="RecyclerAdaptor methods">

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener  = listener;
    }

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public final void onBindViewHolder (VH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        onBindViewHolder(holder, mCursor, position);
    }

    public abstract void onBindViewHolder(VH holder, ObjectCursor<NavigationDrawerItem> cursor, int position);

    public void handleSelection(View v, int pos) {
        if (mDataValid && mCursor != null) {
            if (previousSelection > -1) {
                Log.d(TAG, "prev: " + previousSelection);
                IDrawerItem prev = getItem(previousSelection);
                if (prev != null) {
                    prev.setSelected(false);
                }
                notifyItemChanged(previousSelection);
            } else {
                for (int i = 0; i < getItemCount(); i++) {
                    if (getItem(i).isSelected()) {
                        getItem(i).setSelected(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }

            //highlight the new item
            if (pos > -1) {
                IDrawerItem cur = getItem(pos);
                if (cur != null) {
                    cur.setSelected(true);
                }
                notifyItemChanged(pos);

                if (v != null) {
                   // Log.d(TAG, "new: " + ((TextView) v.findViewById(R.baseId.baseId)).getText());
                    Log.d(TAG, "new: " + pos);
                    v.setSelected(true);
                    v.invalidate();
                }
            }
            previousSelection = pos;
        }
    }

    @Override
    public int getItemCount () {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId (int position) {
        if(hasStableIds() && mDataValid && mCursor != null){
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public IDrawerItem getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return mDrawerItems.get(position);
    }

    // </editor-fold>

    // <editor-fold desc="Cursor">

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    public void changeCursor(ObjectCursor<NavigationDrawerItem> cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private boolean isCursorInvalid() {
        return mCursor == null || mCursor.isClosed()
                || mCursor.getCount() <= 0 || !mCursor.moveToFirst();
    }

    public Cursor getCursor() {
        return mCursor;
    }


    public Cursor swapCursor(ObjectCursor<NavigationDrawerItem> newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            recalculateList();
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    /**
     * <p>Converts the cursor into a CharSequence. Subclasses should override this
     * method to convert their results. The default implementation returns an
     * empty String for null values or the default String representation of
     * the value.</p>
     *
     * @param cursor the cursor to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    public CharSequence convertToString(ObjectCursor<Folder> cursor) {
        return cursor == null ? "" : cursor.toString();
    }



    private void recalculateList() {
        final List<IDrawerItem> newFolderList = new ArrayList<>();
        recalculateListFolders(newFolderList);
    }

    private void recalculateListFolders(List<IDrawerItem> itemList) {
        if (isCursorInvalid()) {
            return;
        }
        int length = mDrawerItems.size();

         do {
             final NavigationDrawerItem f = mCursor.getModel();
             mDrawerItems.add(f);
         } while (mCursor.moveToNext());

        notifyItemRangeInserted(length, mDrawerItems.size());

    }

    public void addDrawerItem(int position, IDrawerItem drawerItem) {
        mDrawerItems.add(position, drawerItem);
        notifyItemInserted(position);
    }

    public void clearDrawerItems() {
        int count = mDrawerItems.size();
        mDrawerItems.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void removeDrawerItem(int position) {
        mDrawerItems.remove(position);
        notifyItemRemoved(position);
    }
    // </editor-fold>

}
