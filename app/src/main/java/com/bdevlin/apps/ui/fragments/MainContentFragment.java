
package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
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
//import android.widget.ListView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.Adapters.ContentCursorRecyclerAdapter;
import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.pandt.Loaders.MyObjectCursorLoader;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;
import com.bdevlin.apps.pandt.DrawerItem.MainContentDrawerItem;
import com.bdevlin.apps.pandt.helper.SimpleItemTouchHelperCallback;
import com.bdevlin.apps.utils.GenericListContext;
//import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.ViewMode;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.ui.activity.core.HomeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public  class MainContentFragment extends /*ListFragment*/ Fragment
        implements ViewMode.ModeChangeListener//,
//        ListView.OnScrollListener
        /*, AdapterViewCompat.OnItemClickListener*/ {

    // <editor-fold desc="Fields">
    private static final String TAG = MainContentFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    private static final String CONVERSATION_LIST_KEY = "conversation-list";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String STATE_SELECTED_POSITION = "selected_position";
    private static final int LOADER_ID_MESSAGES_LOADER = 1;
    private int mCurrentSelectedPosition = 0;
    private int mSectionNumber = 0;
    private GenericListContext mViewContext;
    private  ControllableActivity mActivity;
    private ActionBarController actionBarController = null;
    private  MainContentCallbacks mCallbacks;
    private final CursorLoads mCursorCallbacks = new CursorLoads();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentCursorRecyclerAdapter mRecycleCursorAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private boolean mFromSavedInstanceState;
    // </editor-fold>

    // <editor-fold desc="Interfaces">

    public interface MainContentCallbacks {
        void onMainContentItemSelected(final int position);
        void onMainContentScrolled(RecyclerView recyclerView, int dx, int dy);
        void onMainContentItemSwipe(CardView cardView,SwipeDismissBehavior<CardView> swipe);
    }

    /**
     * A dummy implementation of the {@link MainContentCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static MainContentCallbacks sDummyCallbacks = new MainContentCallbacks() {
        @Override
        public void onMainContentItemSelected(final int position) {
        }
        public void onMainContentScrolled(RecyclerView recyclerView, int dx, int dy)
        {

        }
        public void onMainContentItemSwipe(CardView cardView,SwipeDismissBehavior<CardView> swipe)
        {

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

    // <editor-fold desc="Life Cycle Create">
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "in MainContentFragment onCreate");
        // Get the context from the arguments
        if (getArguments() != null) {
             final Bundle args = getArguments();
            mViewContext = GenericListContext.forBundle(args.getBundle(CONVERSATION_LIST_KEY));
        }
    }
    
    @Override
    public void onViewModeChanged(int newMode) {
        Log.v(TAG, "in onViewModeChanged  " + newMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_content_fragment, container, false);

        SetupMainContentRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        final Activity activity = getActivity();
//
//        if (!(activity instanceof ControllableActivity)) {
//            return;
//        }

       // mActivity = (HomeActivity) activity;
       // mActivity = (ControllableActivity) activity;

        SetupViewMode();

        SetupActionToolbar();
        
        mRecycleCursorAdapter = new ContentCursorRecyclerAdapter(mActivity,
                null,
                MockContract.FOLDERS_PROJECTION
                );
        
        mRecyclerView.setAdapter(mRecycleCursorAdapter);

        SetupTouchHelper();

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

    private void SetupViewMode()
    {
        onViewModeChanged(mActivity.getViewMode().getMode());
        ViewMode mode = mActivity.getViewMode();
        mode.enterMainContentListMode();
        mode.addListener(this);
    }

    private void SetupTouchHelper()
    {
        if (mRecyclerView != null) {
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecycleCursorAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

    }

    private void SetupMainContentRecyclerView(View rootView)
    {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               //     Log.i(TAG, "onScrolled");
                mCallbacks = mActivity.getMainContentCallbacks();
                mCallbacks.onMainContentScrolled(recyclerView,dx,dy);
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void SetupActionToolbar()
    {
        actionBarController = mActivity.getActionBarController();
        Toolbar toolbar = actionBarController.getSupportToolBar();
        CollapsingToolbarLayout ctl = actionBarController.getCollapsingToolbarLayout();
        if (ctl != null && mViewContext.folder != null ) {
            ctl.setTitle("Main Content");
            ctl.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
           TextView subTitle =  (TextView)ctl.findViewById(R.id.subTitle);
            subTitle.setText(mViewContext.folder.name);
        }
        else {
            if (ctl != null) {
                TextView subTitle =  (TextView)ctl.findViewById(R.id.subTitle);
                subTitle.setText(R.string.empty );
            }
        }
    }

    // </editor-fold

    // <editor-fold desc="Life Cycle">

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ControllableActivity)) {
            return;
        }

        try {
            mActivity = (ControllableActivity) activity;
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
    public void onDestroyView() {
        mActivity.getViewMode().removeListener(this);
        super.onDestroyView();
    }

    // </editor-fold>

    // <editor-fold desc="Option menus">

    // remember these methods don't get called if the activity handles it and return true
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            final String[] mProjection = MockContract.ACCOUNTS_PROJECTION;
            final Uri contentUri = MockContract.Accounts.CONTENT_URI;
            final CursorCreator<MainContentDrawerItem> mFactory = FACTORY;


            switch (id) {
                case LOADER_ID_MESSAGES_LOADER:
                    return new MyObjectCursorLoader<>(getActivity().getApplicationContext(),
                            contentUri, mProjection, mFactory);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ObjectCursor<MainContentDrawerItem>> loader, ObjectCursor<MainContentDrawerItem> data) {

            if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
                 return;
            }
            switch (loader.getId()) {
                case LOADER_ID_MESSAGES_LOADER:
                    if (mRecycleCursorAdapter != null) {
                        mRecycleCursorAdapter.swapCursor(data);
                    }
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<ObjectCursor<MainContentDrawerItem>> loader) {
            if (mRecycleCursorAdapter != null) {
                mRecycleCursorAdapter.swapCursor(null);
            }
        }
    }

    // </editor-fold>

}