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
    private static final String TAG = SimpleCursorRecyclerAdapter.class.getSimpleName();

    // <editor-fold desc="Fields">
   // private List<IDrawerItem> mDrawerItems = new ArrayList<>();
    Context mContext;
    protected  int[] mFrom;
    protected  int[] mTo;
    protected  String[] mOriginalFrom;
    private final OnStartDragListener mDragStartListener;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public SimpleCursorRecyclerAdapter (Context context,
                                       /* int layout,*/
                                        ObjectCursor<PrimaryDrawerItem> c,
                                        String[] from,
                                        int[] to,
                                        OnStartDragListener dragStartListener) {
        super(c);
        mDragStartListener = dragStartListener;
        mContext = context;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
    }

    // </editor-fold>

    // <editor-fold desc="Adapter methods">
    //// Create new views (invoked by the layout manager)
    @Override
    public  PrimaryDrawerItem.ListItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        PrimaryDrawerItem item = new PrimaryDrawerItem(null);
        //AbstractDrawerItem.getViewHolder inflates the view item and returns the ListItemViewHolder(view)
      return item.getViewHolder(parent);
    }

    // // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder (PrimaryDrawerItem.ListItemViewHolder holder, ObjectCursor<PrimaryDrawerItem> cursor, int position ) {
        Log.d(TAG,"onBindViewHolder");
        // gets the IDrawerItem at this position then bind the viewholder to it
        getItem(position).bindView(holder);
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
        //called by navigationDrawerFragment Loader
        findColumns(c, mOriginalFrom);
        // sawp cursor on the base class will use the cursor to generate the drawerItems
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
// not used here
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


