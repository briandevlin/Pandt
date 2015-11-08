
package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.widget.ListView;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Cursors.ContentBaseRecyclerViewAdapter;
import com.bdevlin.apps.pandt.Cursors.ContentCursorRecyclerAdapter;
import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.pandt.Cursors.NavigationBaseRecyclerAdapter;
import com.bdevlin.apps.pandt.Cursors.MyObjectCursorLoader;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.Cursors.SimpleAdapter;
import com.bdevlin.apps.pandt.Cursors.NavigationCursorRecyclerAdapter;
import com.bdevlin.apps.pandt.DividerItemDecoration;
import com.bdevlin.apps.pandt.DrawerItem.MainContentDrawerItem;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.pandt.folders.FolderController;
import com.bdevlin.apps.pandt.helper.SimpleItemTouchHelperCallback;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.ui.activity.core.HomeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public  class MainContentFragment extends /*ListFragment*/ Fragment
        implements ViewMode.ModeChangeListener/*, AdapterViewCompat.OnItemClickListener*/ {

    // <editor-fold desc="Fields">
    private static final String TAG = MainContentFragment.class.getSimpleName();

   // private static final int LOADER_ID = 1;

    private static final String CONVERSATION_LIST_KEY = "conversation-list";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_position";
    private static final int LOADER_ID_MESSAGES_LOADER = 1;
    private ListView mListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private int mSectionNumber = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private final Handler mHandler = new Handler();

    private GenericListContext mViewContext;
    private  ControllableActivity mActivity;
    private FolderController folderController = null;
    private ActionBarController actionBarController = null;

    private  MainContentCallbacks mCallbacks;

    // True if we are on a tablet device
    private static boolean mTabletDevice;

    private final CursorLoads mCursorCallbacks = new CursorLoads();


    private  ArrayAdapter<Items.ListItem> adapter = null;

    private  SimpleAdapter simpleAdapter = null;

    //private  SimpleCursorAdapter mAdapter = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentCursorRecyclerAdapter mRecycleCursorAdapter;

   /* @Override
    public void onItemClick(AdapterViewCompat<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),
                parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }*/

    // </editor-fold>

    // <editor-fold desc="Interfaces">

    public interface MainContentCallbacks {
        void onMainContentItemSelected(final int position, Items.ListItem listItem);
    }

    /**
     * A dummy implementation of the {@link MainContentCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static MainContentCallbacks sDummyCallbacks = new MainContentCallbacks() {
        @Override
        public void onMainContentItemSelected(final int position, Items.ListItem listItem) {
        }
    };
    // </editor-fold>


    // <editor-fold desc="new instance">

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainContentFragment newInstance(GenericListContext viewContext,int sectionNumber) {
        final MainContentFragment fragment = new MainContentFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putBundle(CONVERSATION_LIST_KEY, viewContext.toBundle());
        fragment.setArguments(args);
        return fragment;
    }

    // </editor-fold>

   // <editor-fold desc="Mandatory empty constructor">
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainContentFragment() { super();}
    // </editor-fold>

    // <editor-fold desc="Life Cycle">
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "in MainContentFragment onCreate");
        // Get the context from the arguments
        if (getArguments() != null) {
             final Bundle args = getArguments();
            mViewContext = GenericListContext.forBundle(args.getBundle(CONVERSATION_LIST_KEY));
            //mAccount = mViewContext.account;
        }



       // setRetainInstance(false);
    }


    @Override
    public void onViewModeChanged(int newMode) {
        Log.v(TAG, "in onViewModeChanged  " + newMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


       // mListView = Utils.getViewOrNull(rootView, android.R.id.list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);


//         use this setting to improve performance if you know that changes
//         in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        mRecyclerView.addItemDecoration(itemDecoration);

        //TODO
      /*  ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecycleCursorAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);*/

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
                R.id.profile_email_text
        };
        mActivity = (HomeActivity) activity;

        mActivity = (ControllableActivity) activity;

        onViewModeChanged(mActivity.getViewMode().getMode());
        ViewMode mode = mActivity.getViewMode();
        mode.enterConversationListMode();
        mode.addListener(this);

/*        if (mode.isConversationMode() && mode.getMode() == ViewMode.UNKNOWN) {
                mode.enterConversationMode();
            } else {
        mode.enterConversationListMode();
            }*/
        mode.enterConversationListMode();


        Toolbar toolbar = mActivity.getActionBarController().getSupportToolBar();
        if (toolbar != null && mViewContext.folder != null ) {
            toolbar.setSubtitle("Main " + mViewContext.folder.name);
        }
        else {
            if (toolbar != null) toolbar.setSubtitle("Main");
        }

        actionBarController = mActivity.getActionBarController();
       ActionBar ab =  actionBarController.getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

//        mCallbacks = mActivity.getMainContentCallbacks();
//
//        Context activityContext = mActivity.getActivityContext();
//
//        final LoaderManager manager = getLoaderManager();
//
//       Context applicationContext = mActivity.getApplicationContext();



        mRecycleCursorAdapter = new ContentCursorRecyclerAdapter(mActivity,
               /* R.layout.textview,*/
                null,
                MockContract.FOLDERS_PROJECTION, // string[] column names
                toId
                );



/*        mRecycleCursorAdapter.setOnItemClickListener(
                new NavigationBaseRecyclerAdapter.OnItemClickListener() {
                    public void onItemClick(View itemView, int position)
                    {
                        Log.d(TAG,"ContentBaseRecyclerViewAdapter.OnItemClickListener");
//                        mCallbacks = mActivity.getNavigationDrawerCallbacks();
//                        mCallbacks.onNavigationDrawerItemSelected(position, null);
                    }

                });*/


        mRecyclerView.setAdapter(mRecycleCursorAdapter);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

        startLoading();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mSectionNumber = savedInstanceState.getInt(ARG_SECTION_NUMBER);
            mFromSavedInstanceState = true;
        }


    }

    // </editor-fold



//    @Override
//    public void onListItemClick(ListView listView, View view, int position,
//                                long id) {
//        Toast.makeText(getActivity(),
//                listView.getItemAtPosition(position).toString(),
//                Toast.LENGTH_LONG).show();
//
//      //  Items.ListItem listItem = (Items.ListItem)listView.getAdapter().getItem(position);
//       // Object item = listView.getAdapter().getItem(position);
////        final Object item = getListAdapter().getItem(position);
////        Log.d(TAG, String.format("view item (%d): %s", position,
////                listItem));
//        selectItem(position, null);
//
//    }


    private void selectItem(final int position, Items.ListItem listItem) {
        mCurrentSelectedPosition = position;

        if (mCallbacks != null) {
            mCallbacks.onMainContentItemSelected(position + 1, listItem);
        }
    }

    // <editor-fold desc="Life Cycle">

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ControllableActivity)) {
            return;
        }

        try {

            mActivity = (ControllableActivity) activity;
           // actionBarController = mActivity.getActionBarController();
//            folderController = mActivity.getFolderController();
//            if (folderController != null) {
//               // folderController.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
//            }
            mCallbacks = mActivity.getMainContentCallbacks();

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sDummyCallbacks;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
         outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putInt(ARG_SECTION_NUMBER, mSectionNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {

        // Clear the list's mCursorAdapter

  //      mListView.setAdapter(null);

        mActivity.getViewMode().removeListener(this);

        super.onDestroyView();
    }

    // </editor-fold>


    // <editor-fold desc="Option menus">

    // remember these methods don't get called if the activity handles it and return true
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("onOptionsItemSelected","yes");
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
super.onCreateOptionsMenu(menu,inflater);
    }



    @Override
    public void  onPrepareOptionsMenu(Menu menu) {
super.onPrepareOptionsMenu(menu);
    }



    // </editor-fold>

    // <editor-fold desc="Loaders">

    private void startLoading() {
        final LoaderManager lm = getLoaderManager();
        final Bundle args = createLoaderArgs(getActivity().getIntent());
        //showSendCommand(false);
        // Prepare the loader. starts or reconnects
        lm.initLoader(LOADER_ID_MESSAGES_LOADER, args, mCursorCallbacks);
    }
    private Bundle createLoaderArgs(Intent intent) {
        Uri data = intent.getData();
        // long contextId = ContentUris.parseId(data);

        Bundle args = new Bundle();
        // args.putLong(INITIAL_ID, 0L);
        // args.putInt(INITIAL_POSITION, intent.getIntExtra(INITIAL_POSITION,
        // -1));

        return args;
    }

    public  final CursorCreator<MainContentDrawerItem> FACTORY = new CursorCreator<MainContentDrawerItem>() {
        @Override
        public MainContentDrawerItem createFromCursor(Cursor c) {
            return new MainContentDrawerItem((HomeActivity)mActivity, c);
        }

        @Override
        public String toString() {
            return "PrimaryDrawerItem CursorCreator";
        }
    };

    private class CursorLoads implements LoaderManager.LoaderCallbacks<ObjectCursor<MainContentDrawerItem>> {

        @Override
        public Loader<ObjectCursor<MainContentDrawerItem>> onCreateLoader(int id, Bundle args) {
            // change these
            final String[] mProjection = MockContract.FOLDERS_PROJECTION;
            final Uri contentUri = MockContract.Folders.CONTENT_URI;
            final CursorCreator<MainContentDrawerItem> mFactory = FACTORY;


            switch (id) {
                case LOADER_ID_MESSAGES_LOADER:
                 /*   return new CursorLoader(getActivity().getApplicationContext(), contentUri,
                            mProjection , null, null, null);*/
                    return new MyObjectCursorLoader<MainContentDrawerItem>(getActivity().getApplicationContext(),
                            contentUri, mProjection, mFactory);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ObjectCursor<MainContentDrawerItem>> loader, ObjectCursor<MainContentDrawerItem> data) {

            if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
                 return;
            }

            int count = data.getCount();

            switch (loader.getId()) {
                case LOADER_ID_MESSAGES_LOADER:
                  //  mAdapter.swapCursor(data);
                    if (mRecycleCursorAdapter != null) {
                        mRecycleCursorAdapter.swapCursor(data);
                    }
                    break;
            }
        }


        @Override
        public void onLoaderReset(Loader<ObjectCursor<MainContentDrawerItem>> loader) {
            // For whatever reason, the Loader's data is now unavailable.
            // Remove any references to the old data by replacing it with
            // a null Cursor.
            if (mRecycleCursorAdapter != null) {
                mRecycleCursorAdapter.swapCursor(null);
            }
        }
    }

    // </editor-fold>

}