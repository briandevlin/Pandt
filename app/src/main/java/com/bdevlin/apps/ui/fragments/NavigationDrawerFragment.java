package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
//import android.content.CursorLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
//import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.pandt.Cursors.NavigationBaseRecyclerAdapter;
import com.bdevlin.apps.pandt.DividerItemDecoration;
//import com.bdevlin.apps.pandt.DrawerClosedObserver;
import com.bdevlin.apps.pandt.DrawerItem.DividerDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.ui.activity.core.HomeActivity;
import com.bdevlin.apps.pandt.Cursors.MyObjectCursorLoader;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.Cursors.NavigationCursorRecyclerAdapter;
import com.bdevlin.apps.pandt.accounts.Account;
import com.bdevlin.apps.pandt.accounts.AccountController;
import com.bdevlin.apps.pandt.accounts.AccountObserver;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.pandt.folders.FolderController;
//import com.bdevlin.apps.pandt.folders.FolderObserver;
import com.bdevlin.apps.pandt.folders.FolderUri;
import com.bdevlin.apps.pandt.helper.OnStartDragListener;
import com.bdevlin.apps.pandt.helper.SimpleItemTouchHelperCallback;
import com.bdevlin.apps.provider.MockContract;

import java.util.ArrayList;

// instantiated from the navdrawer_content.xml  <fragment android:id="@+id/navigation_drawer"
public class NavigationDrawerFragment
        extends ListFragment
        implements LoaderManager.LoaderCallbacks<ObjectCursor<NavigationDrawerItem>>,
        ViewMode.ModeChangeListener,
        OnStartDragListener {

    // <editor-fold desc="Fields">

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String BUNDLE_LIST_STATE = "flf-list-state";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private static final int LOADER_ID = 1;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    private int mCurrentSelectedPosition = 0;
    
    private  ControllableActivity mActivity;
   
   /* private FolderController folderController = null;
    private ActionBarController actionBarController = null;
    private AccountController mAccountController;

    private Account mCurrentAccount;
    private Account mNextAccount = null;*/
    /** The folder we will change to once the drawer (if any) is closed */
   /* private Folder mNextFolder = null;
    private Uri mFolderListUri;
    private FolderUri mSelectedFolderUri = FolderUri.EMPTY;*/

    //ArrayAdapter<Folder> mCursorAdapter;

    /*private DrawerClosedObserver mDrawerObserver = null;
    private FolderObserver mFolderObserver = null;
    private AccountObserver mAccountObserver = null;*/

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NavigationCursorRecyclerAdapter mRecycleCursorAdapter;

    private ItemTouchHelper mItemTouchHelper;
    private boolean mFromSavedInstanceState;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    // </editor-fold>

    // <editor-fold desc="Interfaces">

    public  final CursorCreator<NavigationDrawerItem> FACTORY = new CursorCreator<NavigationDrawerItem>() {
        @Override
        public NavigationDrawerItem createFromCursor(Cursor c) {
            return new NavigationDrawerItem((HomeActivity)mActivity, c);
        }

        @Override
        public String toString() {
            return "PrimaryDrawerItem CursorCreator";
        }
    };
    /**
     * Callbacks interface that all activities using this fragment must implement.
     * we use this interface to replace the fragments name in the HomeActivity via UIControllerBase
     * when the usr selects an item in the Navigation drawer list
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        public void onNavigationDrawerItemSelected(int position, NavigationDrawerItem itemView);
        public void onNavigationDrawerArraySelected(int position, NavigationDrawerItem itemView);
    }


    /**
     * A dummy implementation of the {@link NavigationDrawerCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static NavigationDrawerCallbacks sDummyCallbacks = new NavigationDrawerCallbacks() {
        @Override
        public void onNavigationDrawerItemSelected(int position, NavigationDrawerItem itemView) {

        }
        public void onNavigationDrawerArraySelected(int position, NavigationDrawerItem itemView) {

        }
    };

    // </editor-fold>

    // <editor-fold desc="new instance">

    // we don't have anew instance method because we are instantiated by the layout when inflated
    // </editor-fold>

    // <editor-fold desc="Mandatory empty constructor">

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NavigationDrawerFragment() {
super();
    }


    // </editor-fold>

    // <editor-fold desc="Create methods">

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (DEBUG) Log.v(TAG, "in NavigationDrawerFragment onCreate");
        if (getArguments() != null) {
            final Bundle args = getArguments();

        }
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null) {
            if (DEBUG) Log.v(TAG, "in NavigationDrawerFragment savedInstanceState");
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        mActivity.getViewMode().addListener(this);
    }

    @Override
    public void onViewModeChanged(int newMode) {
        if (DEBUG) Log.v(TAG, "NavigationDrawerFragment: in onViewModeChanged  " + newMode);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setFadingEdgeLength(0);
            mRecyclerView.setClipToPadding(false);

            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mLayoutManager.scrollToPosition(0);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //Cursor cursor = getActivity().getContentResolver().query(MockContract.Folders.CONTENT_URI, MockContract.FOLDERS_PROJECTION, null, null, null);


            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

            mRecyclerView.addItemDecoration(itemDecoration);


            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecycleCursorAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }



        if (savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_LIST_STATE)) {
//            mRecyclerView.onRestoreInstanceState(savedInstanceState
//                    .getParcelable(BUNDLE_LIST_STATE));
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListView lv = getListView();
        final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        int[] toId = new int[]{
                R.id.id,
                R.id.name
        };
        mActivity = (HomeActivity) activity;

        mRecycleCursorAdapter = new NavigationCursorRecyclerAdapter(mActivity,
               /* R.layout.textview,*/
                null,
                MockContract.FOLDERS_PROJECTION, // string[] column names
                toId,// resource id's from the itemview
                this);

        mRecycleCursorAdapter.setOnItemClickListener(
                new NavigationBaseRecyclerAdapter.OnItemClickListener() {
                    public void onItemClick(View itemView, int position)
                    {
                        Log.d(TAG,"mRecycleCursorAdapter.setOnItemClickListener");
                        mCallbacks = mActivity.getNavigationDrawerCallbacks();
                        mCallbacks.onNavigationDrawerItemSelected(position, null);
                    }

                });

        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mRecycleCursorAdapter);

         ArrayList<IDrawerItem> mDrawerItems = new ArrayList<>();
        DividerDrawerItem divider = new DividerDrawerItem();


        NavigationDrawerItem settings =  new  NavigationDrawerItem(mActivity, null);
        settings.name = "Settings";
        settings.id = 1;
        settings.setIcon(R.drawable.ic_settings_applications_black_24dp);


        NavigationDrawerItem help =  new  NavigationDrawerItem(mActivity, null);
        help.name = "Help";
        help.id = 2;
        help.setIcon(R.drawable.ic_help_black_24dp);

     /*   NavigationDrawerItem item2 =  new  NavigationDrawerItem(mActivity, null);
        item2.name = "About";
        item2.id = 3;*/
        mDrawerItems.add(divider);
        mDrawerItems.add(settings);
        mDrawerItems.add(help);
       // mDrawerItems.add(item2);

        DrawerItemAdapter mCursorAdapter = new DrawerItemAdapter(
                mActivity.getApplicationContext(),
                mDrawerItems );

        setListAdapter(mCursorAdapter);


        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, this);
    }



    // </editor-fold>

    // <editor-fold desc="Misc">


    // </editor-fold>

    // <editor-fold desc="Life Cycle">

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (!(activity instanceof ControllableActivity)) {
                // log something here
            }
            mActivity = (ControllableActivity) activity;
           // folderController = mActivity.getFolderController();
            mCallbacks = mActivity.getNavigationDrawerCallbacks();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }


    // </editor-fold>

    // <editor-fold desc="state">

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (mDrawerListView != null) {
//            outState.putParcelable(BUNDLE_LIST_STATE,
//                    mDrawerListView.onSaveInstanceState());
//        }

//        if (mSelectedFolderUri != null) {
//            outState.putString(BUNDLE_SELECTED_FOLDER,
//                    mSelectedFolderUri.toString());
//        }

        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    // </editor-fold>

    // <editor-fold desc="optionsMenu">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    // </editor-fold>

    // <editor-fold desc="Loader callbacks">


    @Override
    public Loader<ObjectCursor<NavigationDrawerItem>> onCreateLoader(int id, Bundle bundle) {
        final String[] mProjection = MockContract.FOLDERS_PROJECTION;
        final CursorCreator<NavigationDrawerItem> mFactory = FACTORY;
        final Uri folderListUri;
        // we only have one loader but...
        switch (id) {

            case LOADER_ID:
                final Uri folderUri = MockContract.Folders.CONTENT_URI;

                // I have disabled the account loader: this needs the account loaders to run
                // folderListUri = mCurrentAccount.folderListUri;

                return new MyObjectCursorLoader<NavigationDrawerItem>(getActivity().getApplicationContext(),
                        folderUri, mProjection, mFactory);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<ObjectCursor<NavigationDrawerItem>> loader, ObjectCursor<NavigationDrawerItem> data) {

        if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
            Log.e(TAG, String.format(
                    "Received null cursor from loader id: %d",
                    loader.getId()));
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorRecyclerAdapter.
                if (mRecycleCursorAdapter != null) {
                    mRecycleCursorAdapter.swapCursor(data);
                }

                Log.e(TAG, String.format(
                        "Received cursor from loader id: %d",
                        loader.getId()));
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<ObjectCursor<NavigationDrawerItem>> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        if (mRecycleCursorAdapter != null) {
            mRecycleCursorAdapter.swapCursor(null);
        }
    }

// </editor-fold>

    // <editor-fold desc="DrawerItemAdapter">



    public class DrawerItemAdapter extends ArrayAdapter<IDrawerItem> {
        public DrawerItemAdapter(Context context, @NonNull ArrayList<IDrawerItem> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NavigationDrawerItem.ListItemViewHolder vh = null;
            // Get the data item for this position
            IDrawerItem user = getItem(position);

            String type = user.getType();
            if (type == "DIVIDER_ITEM") {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_divider, parent, false);
                    return convertView;
                }else {

                }
            }
            else {
                NavigationDrawerItem nav = (NavigationDrawerItem)getItem(position);
              //   vh =  (NavigationDrawerItem.ListItemViewHolder)nav.getViewHolder(parent);
              //  nav.bindView(vh);

              // generateView essentially wraps the above methods and returns itemView
                return nav.generateView(getContext(),parent);
            }
          return null;
        }
    }

    // </editor-fold>

}
