package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.NavigationBaseRecyclerAdapter;
import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;
import com.bdevlin.apps.provider.MockUiProvider;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;

/**
 * Created by brian on 9/26/2015.
 */
public class NavigationDrawerItem
        extends BaseNavigationDrawerItem<NavigationDrawerItem> {

    private static final String TAG = NavigationDrawerItem.class.getSimpleName();

    private static final boolean DEBUG = true;
    // <editor-fold desc="Fields">
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    private static IViewHolderClicked viewHolderClicked;
    private static NavigationBaseRecyclerAdapter.OnItemClickListener drawerItemClicked;
    public int id;
    public String name;
    protected int[] mTo;
    protected int[] mFrom;
    private ControllableActivity mActivity;
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public interface IViewHolderClicked {
         void onTextClicked(View caller);

         void onImageClicked(ImageView callerImage);
    }

//    public interface ViewHolderFactory<T> {
//        T factory(View v);
//    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public NavigationDrawerItem(ControllableActivity activity, Cursor c) {
        if (activity != null) {
            this.mActivity = activity;
        }

        if (c != null) {
            id = c.getInt(MockUiProvider.FOLDER_ID_COLUMN);
            name = c.getString(MockUiProvider.FOLDER_NAME_COLUMN);
        }
        setPostOnBindViewListener(new OnPostBindViewListener() {

            public void onBindView(IDrawerItem drawerItem, View view) {
                if (DEBUG) Log.d(TAG, "Post bind View ");
            }
        });

        viewHolderClicked = new IViewHolderClicked() {
            public void onTextClicked(View caller) {
                if (DEBUG) Log.d(TAG, "Poh-tah-tos");
            }

            public void onImageClicked(ImageView callerImage) {
                if (DEBUG) Log.d(TAG, "To-m8-tohs");
            }
        };

        drawerItemClicked = new NavigationBaseRecyclerAdapter.OnItemClickListener() {

            public void onItemClick(View itemView, int position) {
                if (DEBUG) Log.d(TAG, "onItemView: " + position);
                ViewParent parent = null;
                //ViewParent getclass = null;
                NavigationDrawerItem item =null;

                if (itemView instanceof ImageView) {
                    //parent =  itemView.getParent().getParent();
                     parent = itemView.getParent();
                    LinearLayout r;
                    if (parent == null) {
                        Log.d("TEST", "this.getParent() is null");
                    }
                    else {
                        if (parent instanceof ViewGroup) {
                            ViewParent grandparent = ((ViewGroup) parent).getParent();
                            if (grandparent == null) {
                                Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is null");

                            }
                            else {
                                if (grandparent instanceof RecyclerView) {
                                    r = (LinearLayout) parent;
                                    item = (NavigationDrawerItem )(r.getTag());
                                    parent = grandparent;
                                }
                                else {
                                    Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is not a RelativeLayout");
                                }
                            }
                        }
                        else {
                            Log.d("TEST", "this.getParent() is not a ViewGroup");
                        }
                    }
                   /* NavigationDrawerItem  nav = null;
                    try {
                        //getclass = itemView.getParent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }*/

                   // item = (NavigationDrawerItem )(nav.getTag());
                    // item = ((NavigationDrawerItem )getclass).getTag();
                } else {
                    parent =  itemView.getParent();
                    item = (NavigationDrawerItem )(itemView.getTag());
                }
                if (parent instanceof RecyclerView) {
                    if (DEBUG)  Log.d(TAG, "from the recycler " );
                    if (mActivity != null) {
                        mCallbacks = mActivity.getNavigationDrawerCallbacks();
                        mCallbacks.onNavigationDrawerItemSelected(position, item);
                    }
                } else {
                    if (DEBUG)  Log.d(TAG, "from the ArrayAdapter ");
                    if (mActivity != null) {
                        mCallbacks = mActivity.getNavigationDrawerCallbacks();
                        mCallbacks.onNavigationDrawerArraySelected(position, item);
                    }
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

        bindViewHelper((BaseViewHolder) holder);

        viewHolder.id.setText(String.valueOf(id));
        viewHolder.name.setText(name);
        //viewHolder.setIcon(R.drawable.ic_settings_applications_black_18dp);

        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public  class ItemFactory implements ViewHolderFactory<ListItemViewHolder> {

        public ListItemViewHolder factory(View v) {

            return new ListItemViewHolder(
                    v,
                    viewHolderClicked,
                    drawerItemClicked
            );
        }
    }

    // Used to cache the views within the item layout for fast access
    public  class ListItemViewHolder extends BaseViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder {

        public IViewHolderClicked mViewHolderListener;
        NavigationBaseRecyclerAdapter.OnItemClickListener drawerItemListener;

        public ListItemViewHolder(View itemLayoutView, IViewHolderClicked listener, NavigationBaseRecyclerAdapter.OnItemClickListener itemClicked) {
            super(itemLayoutView);

            this.mViewHolderListener = listener;
            this.drawerItemListener = itemClicked;
            // Attach a click listener to the entire row view
            itemLayoutView.setOnClickListener(this);

            this.icon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position
            int pos = getAdapterPosition();


            NavigationDrawerItem item = (NavigationDrawerItem )(v.getTag());

            if (pos == -1){
                pos = (item.id - 1);
            }

            if (v instanceof ImageView) {
                mViewHolderListener.onImageClicked((ImageView) v);
            } else {
                mViewHolderListener.onTextClicked(v);
            }

            if (drawerItemListener == null) {
                throw new NullPointerException("mOnItemClickListener is null in ");
            }
            drawerItemListener.onItemClick(v, pos);

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
