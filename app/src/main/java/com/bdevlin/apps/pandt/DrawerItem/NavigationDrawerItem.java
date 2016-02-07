    package com.bdevlin.apps.pandt.DrawerItem;

    import android.content.Context;
    import android.database.Cursor;
    import android.graphics.Color;
    import android.support.v7.widget.RecyclerView;
    import android.support.annotation.LayoutRes;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;

    import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
    import com.bdevlin.apps.pandt.Adapters.NavigationBaseRecyclerAdapter;
    import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;
    import com.bdevlin.apps.pandt.R;
    import com.bdevlin.apps.pandt.helper.ItemTouchHelperViewHolder;
    import com.bdevlin.apps.provider.MockUiProvider;
    import com.bdevlin.apps.ui.widgets.ColorHolder;
    import com.bdevlin.apps.ui.widgets.StringHolder;

    /**
     * Created by brian on 9/26/2015.
     */
    public class NavigationDrawerItem
            extends BaseNavigationDrawerItem<NavigationDrawerItem> {


        // <editor-fold desc="Fields">
        private static final String TAG = NavigationDrawerItem.class.getSimpleName();
        private static final boolean DEBUG = true;
       // private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
        private static IViewHolderClicked viewHolderClicked;
        private static NavigationBaseRecyclerAdapter.OnItemClickListener drawerItemClicked;

    private ColorHolder background;
        private ControllableActivity mActivity;
     //   private static int mSelectedPosition = 0;
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
                baseId = c.getInt(MockUiProvider.FOLDER_ID_COLUMN);
                baseName = new StringHolder(c.getString(MockUiProvider.FOLDER_NAME_COLUMN));
                baseIcon = c.getString(MockUiProvider.FOLDER_ICON_COLUMN);
                baseUri = c.getString(MockUiProvider.FOLDER_URI_COLUMN);
               //baseListUri = c.getString(MockUiProvider.Folder)
            }
            setPostOnBindViewListener(new OnPostBindViewListener() {

                public void onBindView(IDrawerItem drawerItem, View view) {
                   // if (DEBUG) Log.d(TAG, "Post bind View ");
                }
            });

            viewHolderClicked = new IViewHolderClicked() {
                public void onTextClicked(View caller) {
                    if (DEBUG) Log.d(TAG, "Poh-tah-tos");
                }

                public void onImageClicked(ImageView callerImage) {
                   // if (DEBUG) Log.d(TAG, "To-m8-tohs");
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
            // bind the base items
            bindViewHelper((BaseViewHolder) holder);

            // bind any navigation specifics items here
            this.background = ColorHolder.fromColorRes(R.color.cyan_a700_plus);
            if (background != null) {
               // background.applyToBackground(holder.itemView);
            }

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
                implements /*View.OnClickListener,*/ ItemTouchHelperViewHolder {

            public IViewHolderClicked mViewHolderListener;
            NavigationBaseRecyclerAdapter.OnItemClickListener drawerItemListener;

            public ListItemViewHolder(View itemLayoutView, IViewHolderClicked listener, NavigationBaseRecyclerAdapter.OnItemClickListener itemClicked) {
                super(itemLayoutView);

    //            this.mViewHolderListener = listener;
    //            this.drawerItemListener = itemClicked;
    //            // Attach a click listener to the entire row view
    //            itemLayoutView.setOnClickListener(this);
    //            // Attach a click listener to the imageView
    //            this.imageView.setOnClickListener(this);

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
