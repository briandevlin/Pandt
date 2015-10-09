package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.CursorRecyclerAdapter;
import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;
import com.bdevlin.apps.provider.MockUiProvider;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;

/**
 * Created by brian on 9/26/2015.
 */
public class PrimaryDrawerItem
        extends BasePrimaryDrawerItem<PrimaryDrawerItem> {

    private static final String TAG = PrimaryDrawerItem.class.getSimpleName();


    // <editor-fold desc="Fields">
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    private static IViewHolderClicked viewHolderClicked;
    private static CursorRecyclerAdapter.OnItemClickListener itemClicked;
    public int id;
    public String name;
    protected int[] mTo;
    protected int[] mFrom;
    private ControllableActivity mActivity;
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public interface IViewHolderClicked {
        public void onTextClicked(View caller);

        public void onImageClicked(ImageView callerImage);
    }

    public interface ViewHolderFactory<T> {
        T factory(View v);
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public PrimaryDrawerItem(ControllableActivity activity, Cursor c) {
        if (activity != null) {
            this.mActivity = activity;
        }

        if (c != null) {
            id = c.getInt(MockUiProvider.FOLDER_ID_COLUMN);
            name = c.getString(MockUiProvider.FOLDER_NAME_COLUMN);
        }
        setPostOnBindViewListener(new OnPostBindViewListener() {

            public void onBindView(IDrawerItem drawerItem, View view) {
                Log.d(TAG, "Post bind View ");
            }
        });

        viewHolderClicked = new IViewHolderClicked() {
            public void onTextClicked(View caller) {
                Log.d(TAG, "Poh-tah-tos");
            }

            public void onImageClicked(ImageView callerImage) {
                Log.d(TAG, "To-m8-tohs");
            }
        };

        itemClicked = new CursorRecyclerAdapter.OnItemClickListener() {

            public void onItemClick(View itemView, int position) {
                Log.d(TAG, "onItemView: " + position);
                if (mActivity != null) {
                    mCallbacks = mActivity.getNavigationDrawerCallbacks();
                    mCallbacks.onNavigationDrawerItemSelected(1, null);
                }
            }

            ;

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

        viewHolder.id.setText(String.valueOf(id));
        viewHolder.name.setText(name);

        onPostBindView(this, holder.itemView);
    }

    @Override
    public ItemFactory getFactory() {
        return new ItemFactory();
    }

    public class ItemFactory implements ViewHolderFactory<ListItemViewHolder> {

        public ListItemViewHolder factory(View v) {

            return new ListItemViewHolder(
                    v,
                    viewHolderClicked,
                    itemClicked
            );
        }
    }

    public class ListItemViewHolder extends BaseViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder {
        public IViewHolderClicked mListener;
        CursorRecyclerAdapter.OnItemClickListener otherListener;

        public ListItemViewHolder(View itemLayoutView, IViewHolderClicked listener, CursorRecyclerAdapter.OnItemClickListener itemClicked) {
            super(itemLayoutView);

            this.mListener = listener;
            this.otherListener = itemClicked;
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
                mListener.onImageClicked((ImageView) v);
            } else {
                mListener.onTextClicked(v);
            }

            if (otherListener == null) {
                throw new NullPointerException("mOnItemClickListener is null in ");
            }
            otherListener.onItemClick(v, getAdapterPosition());

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

    }
}
