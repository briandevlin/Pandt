
package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.ActionBarController;
import com.bdevlin.apps.pandt.ControllableActivity;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.pandt.folders.FolderController;
import com.bdevlin.apps.provider.MockContract;

/**
 * A placeholder fragment containing a simple view.
 */
public  class MainContentFragment extends ListFragment implements ViewMode.ModeChangeListener {

    // <editor-fold desc="Fields">
    private static final String TAG = MainContentFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;

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
    private ListView mListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private int mSectionNumber = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private final Handler mHandler = new Handler();

    private GenericListContext mViewContext;
    private ControllableActivity mActivity;
    private FolderController folderController = null;
    private ActionBarController actionBarController = null;

    private  MainContentCallbacks mCallbacks;

    // True if we are on a tablet device
    private static boolean mTabletDevice;

    private final CursorLoads mCursorCallbacks = new CursorLoads();

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
        MainContentFragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
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
    public MainContentFragment() { }
    // </editor-fold>


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the context from the arguments
        if (getArguments() != null) {
             final Bundle args = getArguments();
            mViewContext = GenericListContext.forBundle(args.getBundle(CONVERSATION_LIST_KEY));
            //mAccount = mViewContext.account;
        }

        onViewModeChanged(mActivity.getViewMode().getMode());
        mActivity.getViewMode().addListener(this);

        setRetainInstance(false);
    }


    @Override
    public void onViewModeChanged(int newMode) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        // not setting the mCursorAdapter here waiting til we create the actionBarController
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        mActivity = (ControllableActivity) activity;
        actionBarController = mActivity.getActionBarController();
       ActionBar ab =  actionBarController.getSupportActionBar();
       // ab.setDisplayHomeAsUpEnabled(true);

        mCallbacks = mActivity.getMainContentCallbacks();

        Context activityContext = mActivity.getActivityContext();

        final LoaderManager manager = getLoaderManager();

       Context applicationContext = mActivity.getApplicationContext();


       ArrayAdapter<Items.ListItem> adapter  =  new ArrayAdapter<Items.ListItem>(
               // actionBarController.getSupportActionBar().getThemedContext(),
               mActivity.getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
              Items.ITEMS
        );

        setListAdapter(adapter);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
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


    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        Toast.makeText(getActivity(),
                listView.getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();

        Items.ListItem listItem = (Items.ListItem)listView.getAdapter().getItem(position);
       // Object item = listView.getAdapter().getItem(position);
//        final Object item = getListAdapter().getItem(position);
        Log.d(TAG, String.format("view item (%d): %s", position,
                listItem));
        selectItem(position, listItem);

    }


    private void selectItem(final int position, Items.ListItem listItem) {
        mCurrentSelectedPosition = position;

        if (mCallbacks != null) {
            mCallbacks.onMainContentItemSelected(position + 1, listItem);
        }

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ControllableActivity)) {
            return;
        }

        try {

            mActivity = (ControllableActivity) activity;
            actionBarController = mActivity.getActionBarController();
            folderController = mActivity.getFolderController();
            if (folderController != null) {
               // folderController.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
            }
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

        mListView.setAdapter(null);

        mActivity.getViewMode().removeListener(this);

        super.onDestroyView();
    }


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

    }



    @Override
    public void  onPrepareOptionsMenu(Menu menu) {

    }



    // </editor-fold>


    private class CursorLoads implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            // change these
            final String[] mProjection = MockContract.FOLDERS_PROJECTION;
            final Uri contentUri = MockContract.Folders.CONTENT_URI;


            switch (id) {
                case LOADER_ID:
                    return new CursorLoader(getActivity().getApplicationContext(), contentUri,
                            mProjection , null, null, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
                 return;
            }

            int count = data.getCount();

            switch (loader.getId()) {
                case LOADER_ID:

                    break;
            }
        }


        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // For whatever reason, the Loader's data is now unavailable.
            // Remove any references to the old data by replacing it with
            // a null Cursor.
            // mAdapter.swapCursor(null);
        }
    }



}