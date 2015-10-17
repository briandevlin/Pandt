package com.bdevlin.apps.pandt.Cursors;


import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.MainContentDrawerItem;

import com.bdevlin.apps.pandt.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bdevlin on 9/7/2015.
 */


public abstract class ContentBaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>   {

    // <editor-fold desc="Fields">
    private static final String TAG = ContentBaseRecyclerViewAdapter.class.getSimpleName();

    protected boolean mDataValid;
    protected int mRowIDColumn;
    protected ObjectCursor<MainContentDrawerItem> mCursor;
    private List<IDrawerItem> mDrawerItems = new ArrayList<>();
    private Application application;

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    public  OnItemClickListener mOnItemClickListener ;

    // </editor-fold>

    // <editor-fold desc="Interfaces">
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener  = listener;
    }
    // </editor-fold>


    public ContentBaseRecyclerViewAdapter(ObjectCursor<MainContentDrawerItem> c) {
        init(c);
    }

    void init(ObjectCursor<MainContentDrawerItem> c) {
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        setHasStableIds(true);
    }


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

    public abstract void onBindViewHolder(VH holder, ObjectCursor<MainContentDrawerItem> cursor, int position);

    @Override
    public int getItemCount() {
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

    public void changeCursor(ObjectCursor<MainContentDrawerItem> cursor) {
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


    public Cursor swapCursor(ObjectCursor<MainContentDrawerItem> newCursor) {
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

        }
        return oldCursor;
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
            final MainContentDrawerItem f = mCursor.getModel();
            mDrawerItems.add(f);
        } while (mCursor.moveToNext());

        notifyItemRangeInserted(length, mDrawerItems.size());

    }

    public IDrawerItem getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return mDrawerItems.get(position);
    }
/*
    public static class ContentItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mLabel;
        public ImageView mImage;
        private Context context;
        public IMyViewHolderClicks mListener;

        public ContentItemViewHolder(Context context, View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            this.mListener = listener;
            this.mTextView = (TextView) itemLayoutView.findViewById(R.id.id);
            this.mLabel = (TextView) itemLayoutView.findViewById(R.id.name);
            this.mImage = (ImageView) itemLayoutView.findViewById(R.id.imageview2);
            this.context = context;
            mImage.setOnClickListener(this);
            // Attach a click listener to the entire row view
            itemLayoutView.setOnClickListener(this);

        }

        // Handles the row being  clicked
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            if (v instanceof ImageView) {
                mListener.onTomato((ImageView) v);
            } else {
                mListener.onPotato(v);
            }
            // Triggers click upwards to the adapter on click
//            if (listener != null)
//                listener.onItemClick(itemView, getLayoutPosition());
            Toast.makeText(v.getContext(), "Id: " + getAdapterPosition(), Toast.LENGTH_LONG).show();
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(View caller);

            public void onTomato(ImageView callerImage);
        }

    }*/





    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
