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
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.pandt.Adapters.NavigationBaseRecyclerAdapter;

import com.bdevlin.apps.provider.MockUiProvider;
import com.bdevlin.apps.ui.widgets.DividerItemDecoration;
//import com.bdevlin.apps.pandt.DrawerClosedObserver;
import com.bdevlin.apps.pandt.DrawerItem.DividerDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
import com.bdevlin.apps.ui.widgets.StringHolder;
import com.bdevlin.apps.utils.ViewMode;
import com.bdevlin.apps.ui.activity.core.HomeActivity;
import com.bdevlin.apps.pandt.Loaders.MyObjectCursorLoader;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.Adapters.NavigationCursorRecyclerAdapter;
//import com.bdevlin.apps.pandt.folders.FolderObserver;
import com.bdevlin.apps.pandt.helper.OnStartDragListener;
import com.bdevlin.apps.pandt.helper.SimpleItemTouchHelperCallback;
import com.bdevlin.apps.provider.MockContract;

import java.util.ArrayList;

// instantiated from the navdrawer_content.xml  <fragment android:baseId="@+baseId/navigation_drawer"
public class NavigationDrawerFragment
        extends ListFragment
        implements LoaderManager.LoaderCallbacks<ObjectCursor<NavigationDrawerItem>>,
        ListView.OnScrollListener,
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

    private ControllableActivity mActivity;
   
   /* private FolderController folderController = null;
    private ActionBarController actionBarController = null;
    private AccountController mAccountController;

    private Account mCurrentAccount;
    private Account mNextAccount = null;*/
    /**
     * The folder we will change to once the drawer (if any) is closed
     */
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
    protected RecyclerView.ItemAnimator mItemAnimator = null;
    private NavigationCursorRecyclerAdapter mRecycleCursorAdapter;

    private ItemTouchHelper mItemTouchHelper;
    private boolean mFromSavedInstanceState;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG,"onScrollStateChanged");
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG,"onScroll");
    }

    // </editor-fold>

    // <editor-fold desc="Interfaces">

    /**
     * Callbacks interface that all activities using this fragment must implement.
     * we use this interface to replace the fragments baseName in the HomeActivity via UIControllerBase
     * when the usr selects an item in the Navigation drawer list
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        public void onNavigationDrawerItemSelected(View view, int position, IDrawerItem itemView);

        public void onNavigationDrawerArraySelected(View view, int position, IDrawerItem itemView);
    }


    /**
     * A dummy implementation of the {@link NavigationDrawerCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static NavigationDrawerCallbacks sDummyCallbacks = new NavigationDrawerCallbacks() {
        @Override
        public void onNavigationDrawerItemSelected(View view, int position, IDrawerItem itemView) {

        }

        public void onNavigationDrawerArraySelected(View view, int position, IDrawerItem itemView) {

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
        // Select either the default item (0) or the last selected item.

        mActivity.getViewMode().addListener(this);
    }

    @Override
    public void onViewModeChanged(int newMode) {
        // we get here from viewmode.dispatchModeChange()
        if (DEBUG) Log.v(TAG, "NavigationDrawerFragment: in onViewModeChanged  " + newMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        SetupNavigationDrawerRecyclerView(rootView);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_LIST_STATE)) {
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        int[] toId = new int[]{
                R.id.id,
                R.id.baseName
        };
        mActivity = (HomeActivity) activity;

        mRecycleCursorAdapter = new NavigationCursorRecyclerAdapter(mActivity,
               /* R.layout.maincontentitemview,*/
                null,
                MockContract.FOLDERS_PROJECTION, // string[] column names
                toId,// resource baseId's from the itemview
                this);


        mRecycleCursorAdapter.setOnClickListener(new NavigationBaseRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(final View view, final int position, final IDrawerItem item) {
                //call the listener
                boolean consumed = false;
                mCallbacks = mActivity.getNavigationDrawerCallbacks();
                mCallbacks.onNavigationDrawerItemSelected(view, position, item);
            }
        });
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mRecycleCursorAdapter);
        }

        //These menu items are setup using an arrayadapter
        initStaticNavMenuItems();

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        //setHasOptionsMenu(true);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, this);
    }

    private void SetupNavigationDrawerRecyclerView(View rootView)
    {
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

            mRecyclerView.setHasFixedSize(true);
            //some style improvements on older devices
            mRecyclerView.setFadingEdgeLength(0);
            mRecyclerView.setClipToPadding(false);

            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mLayoutManager.scrollToPosition(0);
            mRecyclerView.setLayoutManager(mLayoutManager);

            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

            mRecyclerView.addItemDecoration(itemDecoration);

            if (mItemAnimator == null) {
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            } else {
                mRecyclerView.setItemAnimator(mItemAnimator);
            }
        }
    }
    public void initStaticNavMenuItems() {

        ArrayList<IDrawerItem> mDrawerItems = new ArrayList<>();

        DividerDrawerItem divider = new DividerDrawerItem();//FIXME need to make this non clickable

        NavigationDrawerItem settings = new NavigationDrawerItem(mActivity, null);
        settings.setBaseName(new StringHolder(getResources().getString(R.string.settings)));
        settings.setBaseId(1);
        settings.setImageHolder(R.drawable.ic_settings_black_24dp);
        settings.setSelected(true);


        NavigationDrawerItem help = new NavigationDrawerItem(mActivity, null);
        help.setBaseName(new StringHolder(getResources().getString(R.string.help)));
        help.setBaseId(2);
        help.setImageHolder(R.drawable.ic_help_outline_black_24dp);


        mDrawerItems.add(divider);
        mDrawerItems.add(settings);
        mDrawerItems.add(help);


        DrawerItemAdapter mCursorAdapter = new DrawerItemAdapter(
                mActivity.getApplicationContext(),
                mDrawerItems);

        final ListView listView = getListView();

        listView.setOnScrollListener(this);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(0, true);

        setListAdapter(mCursorAdapter);
    }


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

    public final CursorCreator<NavigationDrawerItem> FACTORY = new CursorCreator<NavigationDrawerItem>() {
        @Override
        public NavigationDrawerItem createFromCursor(Cursor c) {
            return new NavigationDrawerItem((HomeActivity) mActivity, c);
        }

        @Override
        public String toString() {
            return "PrimaryDrawerItem CursorCreator";
        }
    };

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

        if (data == null || data.getCount() <= 0 || !data.moveToFirst()) {
            Log.e(TAG, String.format(
                    "Received null cursor from loader baseId: %d",
                    loader.getId()));
            return;
        }
        int id = data.getInt(MockContract.Folders.FOLDER_ID_COLUMN);
        String name = data.getString(MockContract.Folders.FOLDER_NAME_COLUMN);
        String uri = data.getString(MockContract.Folders.FOLDER_URI_COLUMN);

        switch (loader.getId()) {
            case LOADER_ID:
                if (mRecycleCursorAdapter != null) {
                    mRecycleCursorAdapter.swapCursor(data);
                    mRecycleCursorAdapter.handleSelection(null, 0);
                    mRecycleCursorAdapter.mOnClickListener.onClick(null, 0, null);
                }

                Log.e(TAG, String.format(
                        "Received cursor from loader baseId: %d ",
                        loader.getId()
                ));
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<ObjectCursor<NavigationDrawerItem>> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        if (mRecycleCursorAdapter != null) {
           // mRecycleCursorAdapter.swapCursor(null);
        }
    }

// </editor-fold>

    // <editor-fold desc="DrawerItemAdapter & onListItemClick">

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        getListView().setItemChecked(pos, true);
         if (mActivity != null) {
            mCallbacks = mActivity.getNavigationDrawerCallbacks();
             mCallbacks.onNavigationDrawerArraySelected(v, pos, null);
        }
    }

    public  class DrawerItemAdapter extends ArrayAdapter<IDrawerItem> {
        private LayoutInflater mInflater;

        public DrawerItemAdapter(Context context, @NonNull ArrayList<IDrawerItem> users) {
            super(context, 0, users);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // Get the data item for this position
            IDrawerItem nav = getItem(position);

            if (nav.getType() == "DIVIDER_ITEM") {
                if (convertView == null) {
                   // convertView = mInflater.inflate(R.layout.drawer_divider, parent, false);
//                    holder = new ViewHolder();
//                    holder.name = (TextView) convertView.findViewById(R.id.baseName);
//                    holder.id = (TextView) convertView.findViewById(R.id.id);
//                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageview2);
//                    convertView.setTag(holder);
                   // return convertView;
                    View view = nav.generateView(getContext(), parent);
                    return view;
                } else {

                    return convertView;
                }
            } else {
                // generateView essentially wraps the above methods and returns NavDrawerItemView
                View view = nav.generateView(getContext(), parent);
                return view;
            }
        }
         class ViewHolder {
            protected View view;
            protected ImageView imageView;
            protected TextView name;
            protected TextView id;
        }
    }

    // </editor-fold>

}
