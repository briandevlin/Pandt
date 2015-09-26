package com.bdevlin.apps.pandt;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.bdevlin.apps.pandt.folders.Folder;

/**
 * Created by brian on 9/15/2015.
 */
public abstract class CursorRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    // <editor-fold desc="Fields">
    protected boolean mDataValid;
    protected ObjectCursor<Folder> mCursor;
    protected int mRowIDColumn;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public CursorRecyclerAdapter(ObjectCursor<Folder> c) {
        // cursor will be null at construction the loader will swap in the cursor when loaded
        init(c);
    }
    // </editor-fold>

    void init(ObjectCursor<Folder> c) {
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        setHasStableIds(true);
    }

    // <editor-fold desc="Adaptor methods">

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

    public abstract void onBindViewHolder(VH holder, ObjectCursor<Folder> cursor, int position);

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


    // </editor-fold>

    // <editor-fold desc="Cursor">

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    public void changeCursor(ObjectCursor<Folder> cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }


    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there was not one.
     * If the given new Cursor is the same instance is the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(ObjectCursor<Folder> newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
       // final Folder f = mCursor.getModel();
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
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

    // </editor-fold>

}
