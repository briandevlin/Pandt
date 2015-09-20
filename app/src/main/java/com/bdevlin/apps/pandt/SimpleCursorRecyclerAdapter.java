package com.bdevlin.apps.pandt;
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

/**
 * Created by brian on 9/15/2015.
 */
public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<ListItemViewHolder> {
    Context mContext;
    private static final String TAG = SimpleCursorRecyclerAdapter.class.getSimpleName();
    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    private ListItemViewHolder.IMyViewHolderClicks viewHolederClicks;

    public SimpleCursorRecyclerAdapter (Context context, int layout, Cursor c, String[] from, int[] to) {
        super(c);
        mContext = context;
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        viewHolederClicks = new ListItemViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller) { Log.d(TAG, "Poh-tah-tos"); };
            public void onTomato(ImageView callerImage) { Log.d(TAG,"To-m8-tohs"); }
        };
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

    @Override
    public ListItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);

        return new ListItemViewHolder(
                parent.getContext(),
                v,
                mTo,
                viewHolederClicks
        );
    }

    @Override
    public void onBindViewHolder (ListItemViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        for (int i = 0; i < count; i++) {
            holder.views[i].setText(cursor.getString(from[i]));
        }
    }
    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
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
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }
}

class ListItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
{
    public TextView[] views;
    public ImageView mImage;
    private Context context;
    public IMyViewHolderClicks mListener;

    public ListItemViewHolder(Context context, View itemLayoutView, int[] to, IMyViewHolderClicks listener)
    {
        super(itemLayoutView);
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
        if (v instanceof ImageView) {
            mListener.onTomato((ImageView) v);
        } else {
            mListener.onPotato(v);
        }
        Toast.makeText(v.getContext(), "Id: " + getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public  interface IMyViewHolderClicks {
        public void onPotato(View caller);

        public void onTomato(ImageView callerImage);
    }
}
