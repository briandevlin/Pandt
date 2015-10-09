package com.bdevlin.apps.pandt.Cursors;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.R;


/**
 * Created by bdevlin on 9/7/2015.
 */


public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.ListItemViewHolder> {

    // <editor-fold desc="Fields">
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();
    private String[] mDataset;
    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    public  OnItemClickListener listener;

    // </editor-fold>

    // <editor-fold desc="Interfaces">
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // </editor-fold>

    // Provide a suitable constructor (depends on the kind of dataset)
    // in this case just a string[]
    public RecyclerViewAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        // TextView vt =  (TextView)v.findViewById(R.id.textView);


        // ListItemViewHolder vh = new ListItemViewHolder(parent.getContext(), v);
        ListItemViewHolder vh = new ListItemViewHolder(parent.getContext(), v,
                new RecyclerViewAdapter.ListItemViewHolder.IMyViewHolderClicks() {
                    public void onPotato(View caller) { Log.d(TAG,"Poh-tah-tos"); };
                    public void onTomato(ImageView callerImage) { Log.d(TAG,"To-m8-tohs"); }
                });

        return vh;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.mLabel.setText(mDataset[position]);
    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ListItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mLabel;
        public ImageView mImage;
        private Context context;
        public IMyViewHolderClicks mListener;

        public ListItemViewHolder(Context context, View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            this.mListener = listener;
            this.mTextView = (TextView) itemLayoutView.findViewById(R.id.id);
            this.mLabel = (TextView) itemLayoutView.findViewById(R.id.name);
            this.mImage = (ImageView) itemLayoutView.findViewById(R.id.imageView2);
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

    }





    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
