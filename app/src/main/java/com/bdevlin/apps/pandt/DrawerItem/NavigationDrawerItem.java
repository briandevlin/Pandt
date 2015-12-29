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
import android.widget.ListView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Adapters.NavigationBaseRecyclerAdapter;
import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;
import com.bdevlin.apps.provider.MockUiProvider;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;
import com.bdevlin.apps.ui.widgets.StringHolder;

/**
 * Created by brian on 9/26/2015.
 */
public class NavigationDrawerItem
        extends BaseNavigationDrawerItem<NavigationDrawerItem> {


    // <editor-fold desc="Fields">
    private static final String TAG = NavigationDrawerItem.class.getSimpleName();
    private static final boolean DEBUG = true;
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    private static IViewHolderClicked viewHolderClicked;
    private static NavigationBaseRecyclerAdapter.OnItemClickListener drawerItemClicked;
   // public int id;
//    public String name;
//    public String uriString;
    protected int[] mTo;
    protected int[] mFrom;
    private ControllableActivity mActivity;
    private static int mSelectedPosition = 0;
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
            name = new StringHolder(c.getString(MockUiProvider.FOLDER_NAME_COLUMN));
            uriString = c.getString(MockUiProvider.FOLDER_URI_COLUMN);
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
                if (DEBUG) Log.d(TAG, "onItemClick: " + position);
                //mSelectedPosition = position;
                ViewParent parent = null;
                //ViewParent getclass = null;
                NavigationDrawerItem item = null;

                // this use case is when the imageview is selected on the draweritem
                if (itemView instanceof ImageView) {
                    //parent =  itemView.getParent().getParent();
                     parent = itemView.getParent();
                    LinearLayout r;
                    if (parent == null) {
                        if (DEBUG) Log.d("TEST", "this.getParent() is null");
                    }
                    else {
                        if (parent instanceof ViewGroup) {
                            ViewParent grandparent = ((ViewGroup) parent).getParent();
                            if (grandparent == null) {
                                if (DEBUG) Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is null");

                            }
                            else {
                                if (grandparent instanceof RecyclerView || grandparent instanceof ListView) {
                                    r = (LinearLayout) parent;
                                    item = (NavigationDrawerItem )(r.getTag());
                                    parent = grandparent;
                                }
                                else {
                                    if (DEBUG) Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is not a RelativeLayout");
                                }
                            }
                        }
                        else {
                            if (DEBUG) Log.d("TEST", "this.getParent() is not a ViewGroup");
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
                    if (mSelectedPosition != position) {
                        //((RecyclerView) parent).getAdapter().notifyItemChanged(mSelectedPosition);
                        item.setSelected(true);
                        itemView.setSelected(true);
                       long id =  ((RecyclerView) parent).getAdapter().getItemId(mSelectedPosition);
                       View view =  ((RecyclerView) parent).getLayoutManager().getChildAt(mSelectedPosition);

                        View viewatpos = ((RecyclerView) parent).getLayoutManager().findViewByPosition(mSelectedPosition);


                       if (viewatpos != null) view.setSelected(false);
                        mSelectedPosition = position;
                        //((RecyclerView) parent).getAdapter().notifyItemChanged(position);
                    }
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
        return R.layout.navdraweritemview;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {

        Context ctx = holder.itemView.getContext();

        //get our viewHolder
        ListItemViewHolder viewHolder = (ListItemViewHolder) holder;

        bindViewHelper((BaseViewHolder) holder);

       // NavDrawerItemView  itemView = (NavDrawerItemView)viewHolder.itemView;


       // TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.navdraweritemview);

//        if (uriString != null) {
//            int resId = ctx.getResources().getIdentifier(uriString, "drawable", ctx.getPackageName());
//            this.setImageHolder(resId);
//            Drawable imageView =  itemView.setContent(resId);
//        }
       // bindViewHelper((BaseViewHolder) holder);

      //  viewHolder.id.setText(String.valueOf(id));
      //  viewHolder.name.setText(name);
      // viewHolder.itemView.
        if (DEBUG) Log.d(TAG, "calling onPostBindView");
        onPostBindView(this, viewHolder.itemView);
    }

    // <editor-fold desc="ViewHolder">
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

            this.imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View itemView) {

            int position = getLayoutPosition(); // gets item position
            int pos = getAdapterPosition();
            ViewParent parent = null;
            //ViewParent getclass = null;
            NavigationDrawerItem item =null;

            // this use case is when the imageview is selected on the draweritem
            if (itemView instanceof ImageView) {
                parent = itemView.getParent();
                LinearLayout r;
                if (parent == null) {
                    if (DEBUG) Log.d("TEST", "this.getParent() is null");
                }
                else {
                    if (parent instanceof ViewGroup) {
                        ViewParent grandparent = ((ViewGroup) parent).getParent();
                        if (grandparent == null) {
                            if (DEBUG) Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is null");
                        }
                        else {
                            if (grandparent instanceof RecyclerView || grandparent instanceof ListView) {
                                r = (LinearLayout) parent;
                                item = (NavigationDrawerItem )(r.getTag());
                                parent = grandparent;
                            }
                            else {
                                if (DEBUG) Log.d("TEST", "((ViewGroup) this.getParent()).getParent() is not a RelativeLayout");
                            }
                        }
                    }
                    else {
                        if (DEBUG) Log.d("TEST", "this.getParent() is not a ViewGroup");
                    }
                }

            } else {
                parent =  itemView.getParent();
                item = (NavigationDrawerItem )(itemView.getTag());
            }

            if (pos == -1){
                pos = (item.id - 1);
            }

            if (itemView instanceof ImageView) {
                mViewHolderListener.onImageClicked((ImageView) itemView);
            } else {
                mViewHolderListener.onTextClicked(itemView);
            }

            if (drawerItemListener == null) {
                throw new NullPointerException("mOnItemClickListener is null in ");
            }
            drawerItemListener.onItemClick(itemView, pos);

            Toast.makeText(itemView.getContext(), "Id: " + pos, Toast.LENGTH_LONG).show();
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

    // </editor-fold>

}
