package com.bdevlin.apps.pandt;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;
import com.bdevlin.apps.provider.MockUiProvider;

/**
 * Created by brian on 9/26/2015.
 */
public class PrimaryDrawerItem extends BasePrimaryDrawerItem<PrimaryDrawerItem>  {

    private static final String TAG = PrimaryDrawerItem.class.getSimpleName();

    // <editor-fold desc="Fields">
   // private static PrimaryDrawerItem.ListItemViewHolder.IMyViewHolderClicks viewHolederClicks;
    private static PrimaryDrawerItem.IMyViewHolderClicks viewHolederClicks;
    public int id;
    public String name;
    protected  int[] mTo;
    protected  int[] mFrom;
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public  interface IMyViewHolderClicks {
        public void onPotato(View caller);

        public void onTomato(ImageView callerImage);
    }
    public interface ViewHolderFactory<T> {
        T factory(View v);
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public PrimaryDrawerItem(Cursor c)
    {
        if (c != null) {
            id = c.getInt(MockUiProvider.FOLDER_ID_COLUMN);
            name = c.getString(MockUiProvider.FOLDER_NAME_COLUMN);
        }
        setPostOnBindViewListener(new OnPostBindViewListener() {

            public void onBindView(IDrawerItem drawerItem, View view)
            {
                Log.d(TAG, "Post bind View ");
            }
        });

   viewHolederClicks = new IMyViewHolderClicks() {
            public void onPotato(View caller) { Log.d(TAG, "Poh-tah-tos"); };
            public void onTomato(ImageView callerImage) { Log.d(TAG,"To-m8-tohs"); }
        };
    }
    // </editor-fold>

    @Override
    public String getType() {
        return "PRIMARY_ITEM";
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.textview;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {

        Context ctx = holder.itemView.getContext();

        ListItemViewHolder viewHolder = (ListItemViewHolder) holder;

//        for (int i = 0; i < count; i++) {
//            viewHolder.views[i].setText(cursor.getString(from[i]));
//        }
//       viewHolder.views[0].setText(String.valueOf(id));
//        viewHolder.views[1].setText(name);
        viewHolder.id.setText(String.valueOf(id));
        viewHolder.name.setText(name);

        onPostBindView(this, holder.itemView);
    }

    @Override
    public ItemFactory getFactory() {
        return new ItemFactory();
    }

    public   class ItemFactory implements ViewHolderFactory<ListItemViewHolder> {

        public ListItemViewHolder factory(View v) {

            return new ListItemViewHolder(
                    v,
                    viewHolederClicks
            );
        }
    }

    protected    class ListItemViewHolder extends BaseViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder
    {
        public IMyViewHolderClicks mListener;

        public ListItemViewHolder( View itemLayoutView, IMyViewHolderClicks listener)
        {
            super(itemLayoutView);

            this.mListener = listener;
            // Attach a click listener to the entire row view
            itemLayoutView.setOnClickListener(this);

            this.icon.setOnClickListener(this);
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

 /*       public  interface IMyViewHolderClicks {
            public void onPotato(View caller);

            public void onTomato(ImageView callerImage);
        }*/
    }
}
