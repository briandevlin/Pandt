package com.bdevlin.apps.pandt;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperAdapter;
import com.bdevlin.apps.pandt.helper.OnStartDragListener;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by brian on 9/15/2015.
 */
public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<PrimaryDrawerItem.ListItemViewHolder>
        implements ItemTouchHelperAdapter {
    // <editor-fold desc="Fields">
    private List<IDrawerItem> mDrawerItems = new ArrayList<>();
    Context mContext;
    private static final String TAG = SimpleCursorRecyclerAdapter.class.getSimpleName();
    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    //private PrimaryDrawerItem.ListItemViewHolder.IMyViewHolderClicks viewHolederClicks;
    private final OnStartDragListener mDragStartListener;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public SimpleCursorRecyclerAdapter (Context context,
                                        int layout,
                                        ObjectCursor<PrimaryDrawerItem> c,
                                        String[] from,
                                        int[] to,
                                        OnStartDragListener dragStartListener) {
        super(c);

        mDragStartListener = dragStartListener;
        mContext = context;
       mLayout = layout;  // the textview
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
       /* viewHolederClicks = new ListItemViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller) { Log.d(TAG, "Poh-tah-tos"); };
            public void onTomato(ImageView callerImage) { Log.d(TAG,"To-m8-tohs"); }
        };*/
//        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                Toast.makeText(mContext, "onChanged() ", Toast.LENGTH_LONG).show();
//               // notifyDataSetChanged();
//            }
//
//            @Override
//            public void onItemRangeChanged(int positionStart, int itemCount) {
//                Toast.makeText(mContext, "onItemRangeChanged() ", Toast.LENGTH_LONG).show();
//                notifyItemRangeChanged(positionStart, itemCount);
//            }
//
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                Toast.makeText(mContext, "onItemRangeInserted() ", Toast.LENGTH_LONG).show();
//                notifyItemRangeInserted(positionStart, itemCount);
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                Toast.makeText(mContext, "onItemRangeRemoved() ", Toast.LENGTH_LONG).show();
//                notifyItemRangeRemoved(positionStart, itemCount);
//            }
//        });
    }

    // </editor-fold>

    // <editor-fold desc="Adapter methods">
    @Override
    public  PrimaryDrawerItem.ListItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        // inflate the itemview (in this case the textview to keep it simple)
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(mLayout, parent, false);

       Log.d(TAG,"onCreateViewHolder");
        PrimaryDrawerItem item = new PrimaryDrawerItem(null);
      return   item.getViewHolder(parent);

//        return new PrimaryDrawerItem.ListItemViewHolder(
//              /*  parent.getContext(),*/
//                v/*,
//                mTo,
//                viewHolederClicks*/
//        );
    }

    @Override
    public void onBindViewHolder (PrimaryDrawerItem.ListItemViewHolder holder, ObjectCursor<PrimaryDrawerItem> cursor, int position ) {
        getItem(position).bindView(holder);
        final int count = mTo.length;
        final int[] from = mFrom;
        Log.d(TAG,"onBindViewHolder");
//        for (int i = 0; i < count; i++) {
//            holder.views[i].setText(cursor.getString(from[i]));
//        }
    }

    // </editor-fold>

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(ObjectCursor<PrimaryDrawerItem> c, String[] from) {
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

    @Override
    public Cursor swapCursor(ObjectCursor<PrimaryDrawerItem> c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }

    // <editor-fold desc="DragDrop">
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Toast.makeText(mContext, "onItemMove: fromPosition " + fromPosition + " toPosition " + toPosition , Toast.LENGTH_LONG).show();
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Toast.makeText(mContext, "onItemDismiss " , Toast.LENGTH_LONG).show();
        notifyItemRemoved(position);
    }

    // </editor-fold>
}

// <editor-fold desc="ListItemViewHolder">
/*
class ListItemViewHolder extends PrimaryDrawerItem.ViewHolder   //RecyclerView.ViewHolder
        implements View.OnClickListener, ItemTouchHelperViewHolder
{
    public TextView[] views;
    public ImageView mImage;
    private Context context;
    public IMyViewHolderClicks mListener;

    public ListItemViewHolder(Context context, View itemLayoutView, int[] to, IMyViewHolderClicks listener)
    {
        super(itemLayoutView, context);

        this.context = context;
        this.mListener = listener;
        // Attach a click listener to the entire row view
       itemLayoutView.setOnClickListener(this);

        views = new TextView[to.length];
        for(int i = 0 ; i < to.length ; i++) {
            views[i] = (TextView) itemView.findViewById(to[i]);
        }

        this.mImage = (ImageView) itemLayoutView.findViewById(R.id.imageView2);
        mImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getLayoutPosition(); // gets item position
        int pos = getAdapterPosition();
        //ListItemViewHolder holder = (ListItemViewHolder )(v.getTag());

        if (v instanceof ImageView) {
            mListener.onTomato((ImageView) v);
        } else {
            mListener.onPotato(v);
        }
        Toast.makeText(v.getContext(), "Id: " + pos, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }

    public  interface IMyViewHolderClicks {
        public void onPotato(View caller);

        public void onTomato(ImageView callerImage);
    }
}*/

// </editor-fold>


