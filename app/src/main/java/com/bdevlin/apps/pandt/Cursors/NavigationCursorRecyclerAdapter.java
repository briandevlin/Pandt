package com.bdevlin.apps.pandt.Cursors;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.ViewGroup;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.helper.ItemTouchHelperAdapter;
import com.bdevlin.apps.pandt.helper.OnStartDragListener;


/**
 * Created by brian on 9/15/2015.
 */
public class NavigationCursorRecyclerAdapter
        extends NavigationBaseRecyclerAdapter<NavigationDrawerItem.ListItemViewHolder>
        implements /*CursorRecyclerAdapter.OnItemClickListener,*/ ItemTouchHelperAdapter {

    private static final String TAG = NavigationCursorRecyclerAdapter.class.getSimpleName();

    // <editor-fold desc="Fields">
   // private List<IDrawerItem> mDrawerItems = new ArrayList<>();
    Context mContext;
    protected  int[] mFrom;
    protected  int[] mTo;
    protected  String[] mOriginalFrom;
    private final OnStartDragListener mDragStartListener;
    private ControllableActivity mActivity;

    // </editor-fold>

    // <editor-fold desc="Constructor">
    public NavigationCursorRecyclerAdapter(ControllableActivity activity,
                                       /* int layout,*/
                                           ObjectCursor<NavigationDrawerItem> c,
                                           String[] from,
                                           int[] to,
                                           OnStartDragListener dragStartListener) {
        super( /*activity,*/ c);
        this.mActivity = activity;
        mDragStartListener = dragStartListener;
        mContext = mActivity.getActivityContext();
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);

    }

    // </editor-fold>

    // <editor-fold desc="Adapter methods">

    //// Create new views (invoked by the layout manager)
    @Override
    public  NavigationDrawerItem.ListItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        NavigationDrawerItem item = new NavigationDrawerItem(mActivity, null);
        //AbstractDrawerItem.getViewHolder inflates the view item and returns the ListItemViewHolder(view)
      return (NavigationDrawerItem.ListItemViewHolder)item.getViewHolder(parent);
    }

    // // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder (NavigationDrawerItem.ListItemViewHolder holder, ObjectCursor<NavigationDrawerItem> cursor, int position ) {
        Log.d(TAG,"onBindViewHolder");
        // gets the IDrawerItem at this position then bind the viewholder to it
        getItem(position).bindView(holder);
    }


//    public void onItemClick(View itemView, int position)
//    {
//
//    }

    // </editor-fold>

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(ObjectCursor<NavigationDrawerItem> c, String[] from) {
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
    public Cursor swapCursor(ObjectCursor<NavigationDrawerItem> c) {
        //called by navigationDrawerFragment Loader
        findColumns(c, mOriginalFrom);
        // sawp cursor on the base class will use the cursor to generate the drawerItems
        return super.swapCursor(c);
    }

    // <editor-fold desc="DragDrop">
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

      //  Toast.makeText(mContext, "onItemMove: fromPosition " + fromPosition + " toPosition " + toPosition , Toast.LENGTH_LONG).show();
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
      //  Toast.makeText(mContext, "onItemDismiss " , Toast.LENGTH_LONG).show();
        notifyItemRemoved(position);
    }

    // </editor-fold>
}



