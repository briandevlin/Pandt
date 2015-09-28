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

/**
 * Created by brian on 9/26/2015.
 */
public class PrimaryDrawerItem extends BasePrimaryDrawerItem<PrimaryDrawerItem>  {

    private static final String TAG = PrimaryDrawerItem.class.getSimpleName();
    private static PrimaryDrawerItem.ListItemViewHolder.IMyViewHolderClicks viewHolederClicks;

//private OnPostBindViewListener postBindViewListener;

    public interface ViewHolderFactory<T> {
        T factory(View v);
    }

    public PrimaryDrawerItem(Cursor c)
    {

//        postBindViewListener = new OnPostBindViewListener() {
//
//            public void onBindView(IDrawerItem drawerItem, View view)
//            {
//                Log.d(TAG, "Post bind View ");
//            }
//        };

        setPostOnBindViewListener(new OnPostBindViewListener() {

            public void onBindView(IDrawerItem drawerItem, View view)
            {
                Log.d(TAG, "Post bind View ");
            }
        });

   viewHolederClicks = new ListItemViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller) { Log.d(TAG, "Poh-tah-tos"); };
            public void onTomato(ImageView callerImage) { Log.d(TAG,"To-m8-tohs"); }
        };
    }

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

        onPostBindView(this, holder.itemView);
    }

    @Override
    public ItemFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ListItemViewHolder> {
        public ListItemViewHolder factory(View v) {
            return new ListItemViewHolder(
                   /* parent.getContext(),*/
                    v, /*,
                    mTo,*/
                    viewHolederClicks
            );
        }
    }

 /*   public static class ViewHolder extends BaseViewHolder {
        private View badgeContainer;
        private TextView badge;

        public ViewHolder(View view, Context ctx) {
            super(view, ctx);
        }
    }*/


    public static  class ListItemViewHolder extends BaseViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder
    {
        public TextView[] views;
        public ImageView mImage;
        private Context context;
        public IMyViewHolderClicks mListener;

        public ListItemViewHolder(/*Context context,*/ View itemLayoutView/*, int[] to*/, IMyViewHolderClicks listener)
        {
            super(itemLayoutView);

           /* this.context = context;*/
            this.mListener = listener;
            // Attach a click listener to the entire row view
            itemLayoutView.setOnClickListener(this);

           /* views = new TextView[to.length];
            for(int i = 0 ; i < to.length ; i++) {
                views[i] = (TextView) itemView.findViewById(to[i]);
            }*/

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
    }
}
