package com.bdevlin.apps.pandt.DrawerItem;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Adapters.ContentBaseRecyclerViewAdapter;
import com.bdevlin.apps.pandt.Interfaces.OnPostBindViewListener;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.provider.PandTContract;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.widgets.StringHolder;

/**
 * Created by brian on 10/12/2015.
 */
public class MainContentDrawerItem
        extends BaseNavigationDrawerItem<MainContentDrawerItem>  {


    // <editor-fold desc="Fields">
    private static final String TAG = MainContentDrawerItem.class.getSimpleName();
    private static final boolean DEBUG = true;
    private MainContentFragment.MainContentCallbacks mCallbacks;
    private static ContentBaseRecyclerViewAdapter.OnItemClickListener itemClicked;
    private static MainContentDrawerItem.IViewHolderClicked viewHolderClicked;
    private ControllableActivity mActivity;
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public interface IViewHolderClicked {
        public void onTextClicked(View caller);

        public void onImageClicked(ImageView callerImage);
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public MainContentDrawerItem(ControllableActivity activity, Cursor c)
    {
        if (activity != null) {
            this.mActivity = activity;
        }

        if (c != null) {
            baseId = c.getInt(PandTContract.Accounts.ACCOUNT_ID_COLUMN);
            baseName = new StringHolder(c.getString(PandTContract.Accounts.ACCOUNT_NAME_COLUMN));
            folderId = c.getInt(PandTContract.Accounts.ACCOUNT_FOLDER_ID_COLUMN);
            baseIcon = c.getString(6);
            baseCount = c.getInt(7);
            baseListUri = new StringHolder(c.getString(PandTContract.Accounts.ACCOUNT_LISTURI_COLUMN));
        }
        setPostOnBindViewListener(new OnPostBindViewListener() {

            public void onBindView(IDrawerItem drawerItem, final View view) {
              // if (DEBUG) Log.d(TAG, "Post bind View ");
                if (view instanceof CardView) {

                    final SwipeDismissBehavior<CardView> swipe
                            = new SwipeDismissBehavior();

                    swipe.setSwipeDirection(
                            SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                    swipe.setListener(
                            new SwipeDismissBehavior.OnDismissListener() {
                                @Override public void onDismiss(View view) {
                                    Toast.makeText(mActivity.getActivityContext(),
                                            "Card swiped !!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDragStateChanged(int state) {}
                            });


                    mCallbacks = mActivity.getMainContentCallbacks();
                    mCallbacks.onMainContentItemSwipe(((CardView)view),swipe);

                }
            }
        });

        viewHolderClicked = new IViewHolderClicked() {

            public void onTextClicked(View caller) {
                Log.d(TAG, "SuperCalafragiletic");
            }

            public void onImageClicked(ImageView callerImage) {
                // do something as the image was clicked
               // if (DEBUG) Log.d(TAG, "Eventhoughthesoundofitissomethingquiteatrocious ");
            }
        };

        itemClicked = new ContentBaseRecyclerViewAdapter.OnItemClickListener() {

            public void onItemClick(View itemView, int position) {
              //  if (DEBUG) Log.d(TAG, "OnItemClickListener: " + position);
                if (mActivity != null) {

                    mCallbacks = mActivity.getMainContentCallbacks();
                   mCallbacks.onMainContentItemSelected(position, 0, 0);
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
    public int getLayoutRes() {
        return R.layout.maincontentitemview;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();

        ContentItemViewHolder viewHolder = (ContentItemViewHolder) holder;
       // Context context = this.mActivity.getActivityContext();

//        if (baseIcon != null) {
//            int resId = ctx.getResources().getIdentifier(baseIcon, "drawable", ctx.getPackageName());
//            this.setImageHolder(resId);
//        }
        bindViewHelper((BaseViewHolder) holder);
//        viewHolder.baseId.setText(String.valueOf(baseId));
//        viewHolder.baseName.setText(baseName);
       // viewHolder.name2.setText("some text");

        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ContentItemViewHolder> {

        public ContentItemViewHolder factory(View v) {

            return new ContentItemViewHolder(
                    v,
                    viewHolderClicked
                    ,itemClicked
            );
        }
    }

    public static class ContentItemViewHolder extends BaseViewHolder
            implements View.OnClickListener/*, ItemTouchHelperViewHolder*/ {
        protected TextView name2;
        public IViewHolderClicked mListener;
        ContentBaseRecyclerViewAdapter.OnItemClickListener otherListener;

        public ContentItemViewHolder(View itemLayoutView, IViewHolderClicked listener, ContentBaseRecyclerViewAdapter.OnItemClickListener itemClicked) {
            super(itemLayoutView);
           // this.name2 = (TextView) view.findViewById(R.baseId.name2);
            this.mListener = listener;
            this.otherListener = itemClicked;
            // Attach a click listener to the entire row view
            //itemLayoutView.setOnClickListener(this);
            this.imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           // if (DEBUG) Log.d(TAG, "onItemView: " + v.toString());
            int position = getLayoutPosition(); // gets item position
            int pos = getAdapterPosition();


            if (v instanceof ImageView) {
                mListener.onImageClicked((ImageView) v);
            } else {
                mListener.onTextClicked(v);
            }

            if (otherListener == null) {

                throw new NullPointerException("mOnItemClickListener is null. ");
            }
            otherListener.onItemClick(v, pos);

            Toast.makeText(v.getContext(), "Id: " + pos, Toast.LENGTH_LONG).show();

        }
    }
}
