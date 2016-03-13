package com.bdevlin.apps.pandt.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.MainContentDrawerItem;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperAdapter;
import com.bdevlin.apps.ui.fragments.MainContentFragment;

/**
 * Created by brian on 10/12/2015.
 */
public class ContentCursorRecyclerAdapter extends ContentBaseRecyclerViewAdapter<MainContentDrawerItem.ContentItemViewHolder>
        implements /*NavigationBaseRecyclerAdapter.OnItemClickListener,*/ ItemTouchHelperAdapter
{
    // <editor-fold desc="Fields">
    private static final String TAG = ContentCursorRecyclerAdapter.class.getSimpleName();
    private static final boolean DEBUG = true;
    Context mContext;
    protected  int[] mFrom;
    protected  int[] mTo;
    protected  String[] mOriginalFrom;
    /*private final OnStartDragListener mDragStartListener;*/
    private ControllableActivity mActivity;
    private MainContentFragment.MainContentCallbacks mCallbacks;
    private static IViewHolderClicked viewHolderClicked;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    // </editor-fold>

    // <editor-fold desc="Interfaces">
    public interface IViewHolderClicked {
        public void onTextClicked(View caller);

        public void onImageClicked(ImageView callerImage);
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">

    public ContentCursorRecyclerAdapter(ControllableActivity activity,
                                        ObjectCursor<MainContentDrawerItem> c,
                                        String[] from ) {
        super(c);
        this.mActivity = activity;
        /*mDragStartListener = dragStartListener;*/
        mContext = mActivity.getActivityContext();
       // mTo = to;
       // mOriginalFrom = from;
        //findColumns(c, from);

        viewHolderClicked = new IViewHolderClicked() {

            public void onTextClicked(View caller) {
                Log.d(TAG, "SuperCalafragiletic");
            }

            public void onImageClicked(ImageView callerImage) {
                if (DEBUG) Log.d(TAG, "Eventhoughthesoundofitissomethingquiteatrocious ");
            }
        };
    }
    // </editor-fold>

    // <editor-fold desc="Recycler methods">
    @Override
    public MainContentDrawerItem.ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // if (DEBUG) Log.d(TAG, "onCreateViewHolder");
        MainContentDrawerItem item = new    MainContentDrawerItem(mActivity,null);
        return (MainContentDrawerItem.ContentItemViewHolder)item.getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final MainContentDrawerItem.ContentItemViewHolder holder, ObjectCursor<MainContentDrawerItem> cursor, final int position) {
       // if (DEBUG) Log.d(TAG,"onBindViewHolder");
        // gets the IDrawerItem at this position then bind the viewholder to it
        getItem(position).bindView(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                IDrawerItem drawerItem = getItem(pos);
                MainContentDrawerItem item = (MainContentDrawerItem)drawerItem;
                Log.d(TAG,"item count" + item.getBaseCount());
                if (v instanceof ImageView) {
                    viewHolderClicked.onImageClicked((ImageView) v);
                } else {
                    viewHolderClicked.onTextClicked(v);
                }

                //make sure there is a DrawerItem for the specific position
                if (drawerItem != null) {

                    handleSelection(v, pos);
                }
                if (mActivity != null) {
                    mCallbacks = mActivity.getMainContentCallbacks();
                    mCallbacks.onMainContentItemSelected(position,  item.getBaseCount(), item.getFolderId());
                    //mCallbacks.onMainContentScrolled(()v);
                }


            }
        });
    }

    // </editor-fold>

    public void handleSelection(View v, int pos) {
        if (pos > -1) {
            IDrawerItem cur = getItem(pos);
            if (cur != null) {
               // cur.withSetSelected(true);
            }
            notifyItemChanged(pos);

            if (v != null) {
                v.setSelected(true);//FIXME
                v.invalidate();
            }
        }
    }

    // <editor-fold desc="Cursor methods">

//    private void findColumns(ObjectCursor<MainContentDrawerItem> c, String[] from) {
//        if (c != null) {
//            int i;
//            int count = from.length;
//            if (mFrom == null || mFrom.length != count) {
//                mFrom = new int[count];
//            }
//            for (i = 0; i < count; i++) {
//                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
//            }
//        } else {
//            mFrom = null;
//        }
//    }
    // </editor-fold>
}
