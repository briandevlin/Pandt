package com.bdevlin.apps.ui.activity.core;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bdevlin.apps.pandt.accounts.Account;
import com.bdevlin.apps.pandt.ActivityController;
import com.bdevlin.apps.pandt.CursorCreator;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.HomeActivity;
import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.pandt.MyObjectCursorLoader;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;
import com.bdevlin.apps.pandt.ObjectCursor;
import com.bdevlin.apps.pandt.PagerController;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.providers2.MailAppProvider;
import com.bdevlin.apps.providers2.UIProvider;
import com.bdevlin.apps.utils.GoogleAccountUtils;

import com.bdevlin.apps.utils.GoogleAccountManager;
import com.bdevlin.apps.utils.GoogleDriveManager;
import com.bdevlin.apps.utils.PlayServicesUtils;
import com.bdevlin.apps.utils.VolleyController;
import com.bdevlin.apps.utils.LoginAndAuthHelper;

import com.bdevlin.apps.utils.Utils;
import com.google.android.gms.auth.GoogleAuthUtil;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by brian on 7/20/2014.
 */
public abstract class UIControllerBase implements ActivityController {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerBase.class.getSimpleName();
    private static final String SAVED_ACCOUNT = "saved-account";
    private static final String SAVED_FOLDER = "saved-folder";
    private static final String SAVED_ACTION = "saved-action";
    private static final int ADD_ACCOUNT_REQUEST_CODE = 1;
    private static final String OPENED_KEY = "OPENED_KEY";
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;

    private final GoogleAccountManager accountManager;
    private final GoogleDriveManager   driveManager;

    public static final String TAG_MAIN_LIST = "tag-main-list";
    protected final HomeActivity mActivity;
    protected final Context mContext;
    private final FragmentManager mFragmentManager;

    private DrawerLayout mDrawerLayout;
    protected View mFragmentContainerView;
    private ViewGroup mDrawerItemsListContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    protected CharSequence mTitle;
    protected PagerController mPagerController;
    private Account mAccount;
    protected Folder mFolder;
    private boolean mFolderChanged = false;
    protected final ViewMode mViewMode;
    protected ContentResolver mResolver;
    private ImageLoader mImageLoader;
    private boolean mAccountBoxExpanded = false;

    private final DataSetObservable mDrawerObservers = new DataSetObservable();
    private final DataSetObservable mFolderObservers = new DataSetObservable();
    private final DataSetObservable mAccountObservers = new DataSetObservable();

    private static final int LOADER_ACCOUNT_CURSOR = 0;
    public static final int LOADER_FIRST_FOLDER = 1;

    private final AccountLoads mAccountCallbacks = new AccountLoads();
    private final FolderLoads mFolderCallbacks = new FolderLoads();

    private boolean mHaveAccountList = false;

    protected GenericListContext mConvListContext;

    private LoginAndAuthHelper mLoginAndAuthHelper;

    private CharSequence mDrawerTitle;
    private Boolean opened = null;
    private SharedPreferences prefs = null;
    private LinearLayout mAccountListContainer;
    private ImageView mExpandAccountBoxIndicator;

   // private String mNextPageToken;


// </editor-fold>

    // <editor-fold desc="Constructor">

    public UIControllerBase(HomeActivity activity, ViewMode viewMode) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mFragmentManager = activity.getSupportFragmentManager();
        final Resources r = mContext.getResources();
        boolean mIsTablet = Utils.useTabletUI(r);
        mViewMode = viewMode;
        accountManager = new GoogleAccountManager(mContext);
        driveManager = new GoogleDriveManager(mContext);//FIXME - not really implemented fully

    }
    // </editor-fold>

    // method defined in ActivityController
    @Override
    public boolean onCreate(Bundle savedInstanceState) {
        initializeActionBar();
        // Allow shortcut keys to function for the ActionBar and menus.
        mActivity.setDefaultKeyMode(Activity.DEFAULT_KEYS_SHORTCUT);
        mResolver = mActivity.getContentResolver();
        mViewMode.addListener(this);
        mPagerController = new PagerController(mActivity, this, mFragmentManager);

       // mImageLoader = new ImageLoader(mActivity);
        // set up the drawer
        SetupDrawerLayout();

        mViewMode.addListener(this);
       // mViewMode.enterConversationListMode();

        final Intent intent = mActivity.getIntent();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_ACCOUNT)) {
                setAccount((Account) savedInstanceState.getParcelable(SAVED_ACCOUNT));
            }

            if (savedInstanceState.containsKey(SAVED_FOLDER)) {
                final Folder folder = savedInstanceState.getParcelable(SAVED_FOLDER);
               // final String query = savedInstanceState.getString(SAVED_QUERY, null);
               // setListContext(folder, query);
            }

            mViewMode.handleRestore(savedInstanceState);
        } else if (intent != null) {
            handleIntent(intent);
        }

//        mActivity.getSupportLoaderManager().initLoader(LOADER_ACCOUNT_CURSOR, Bundle.EMPTY,
//                mAccountCallbacks);

        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        prefs.registerOnSharedPreferenceChangeListener(this);



        return true;
    }

    // <editor-fold desc="life cycle methods">


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    protected abstract boolean handleBackPress(boolean isSystemBackKey);

    protected abstract boolean handleUpPress();

    @Override
    public void onDestroy() {

        mPagerController.onDestroy();
    }


    @Override
    public void onPause() {
        mHaveAccountList = false;

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
       String accountName =  accountManager.getActiveAccount(mActivity);

        if (mLoginAndAuthHelper != null && mLoginAndAuthHelper.getAccountName().equals(accountName)) {
            Log.d(TAG, "Helper already set up; simply starting it.");
            mLoginAndAuthHelper.start();
            return;
        }

        Log.d(TAG, "Creating and starting new mLoginAndAuthHelper with account: " + accountName);
        mLoginAndAuthHelper = new LoginAndAuthHelper(mActivity, this,  accountName);
        mLoginAndAuthHelper.start();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStart");
        if (mLoginAndAuthHelper != null) {
            mLoginAndAuthHelper.stop();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

        mDrawerToggle.setDrawerIndicatorEnabled(isDrawerEnabled());

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                prefs = mActivity.getPreferences(Context.MODE_PRIVATE);
                opened = prefs.getBoolean(OPENED_KEY, false);
                if(!opened)
                {
                    mDrawerLayout.openDrawer(mFragmentContainerView);
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
       // Invalidating the options menu so that when we make changes in settings,
        // the changes will always be updated in the action bar/options menu/
        mActivity.supportInvalidateOptionsMenu();
    }

    @Override
    public void onViewModeChanged(int newMode) {

//        if (!ViewMode.isConversationMode(newMode)) {
//          //  setCurrentConversation(null);
//        }
//
//        // If the viewmode is not set, preserve existing icon.
//        if (newMode != ViewMode.UNKNOWN) {
//          //  resetActionBarIcon();
//        }

        if (isDrawerEnabled()) {
           // final boolean isTopLevel = (mFolder == null) || (mFolder.parent == Uri.EMPTY);
            mDrawerToggle.setDrawerIndicatorEnabled(
                    getShouldShowDrawerIndicator(newMode, true));
        }
        closeDrawerIfOpen();
    }

    @Override
    public boolean onBackPressed(boolean isSystemBackKey) {

        if (isDrawerEnabled() && mDrawerLayout.isDrawerVisible(mFragmentContainerView)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        return handleBackPress(isSystemBackKey);
    }

    @Override
    public final boolean onUpPressed() {
        return handleUpPress();
    }

//    private static boolean getShouldAllowDrawerPull(final int viewMode) {
//        // if search list/conv mode, disable drawer pull
//        // allow drawer pull everywhere except conversation mode where the list is hidden
//        return !ViewMode.isSearchMode(viewMode) && !ViewMode.isConversationMode(viewMode);
//    }

    boolean getShouldShowDrawerIndicator(final int viewMode,
                                         final boolean isTopLevel) {
        return isDrawerEnabled() && !ViewMode.isSearchMode(viewMode)
                && (viewMode == ViewMode.CONVERSATION_LIST  && isTopLevel);
    }

    // </editor-fold>

    // <editor-fold desc="ActionBar ">



    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(mTitle);
    }


    public ActionBar getActionBar() {
        return ((ActionBarActivity) mActivity).getSupportActionBar();
    }


    private void initializeActionBar() {
        final ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }

        final LayoutInflater inflater = LayoutInflater.from(actionBar.getThemedContext());

    }
    // </editor-fold>

    // <editor-fold desc="Fragments ">


    /**
     * Check if the fragment is attached to an activity and has a root view.
     *
     * @param in fragment to be checked
     * @return true if the fragment is valid, false otherwise
     */
    protected static boolean isValidFragment(Fragment in) {
        return !(in == null || in.getActivity() == null || in.getView() == null);
    }

    // the NavigationDrawerFragment is created via the XML
    protected NavigationDrawerFragment getNavigationDrawerFragment() {
        final Fragment fragment = mFragmentManager.findFragmentById(R.id.navigation_drawer);
        if (isValidFragment(fragment)) {
            return (NavigationDrawerFragment) fragment;
        }
        return null;
    }


    // </editor-fold>

    // <editor-fold desc="Option menus">
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        boolean handled = true;
        switch (id) {
//            case R.id.menu_about:
//                HelpUtils.showAbout(this);
//                return true;

            case R.id.action_settings:
                Toast.makeText(mActivity, "Example action.", Toast.LENGTH_SHORT).show();
                return true;
            case  android.R.id.home:
                onUpPressed();
                return true;
            default:
                return handled = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && !isDrawerOpen()) {
            //inflater.inflate(R.menu.home, menu);
            mActivity.getMenuInflater().inflate(R.menu.home, (Menu) menu);
            showGlobalContextActionBar();
        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the name view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mFragmentContainerView);
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return true;
    }


    // </editor-fold>

    // <editor-fold desc="Misc ">

    protected abstract boolean isConversationListVisible();

    @Override
    public GenericListContext getCurrentListContext() {
        return mConvListContext;
    }

    @Override
    public void showConversationList(GenericListContext listContext) {

    }
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /** Returns the context. */
    public final Context getContext() {
        return mContext;
    }
    @Override
    public Folder getFolder() {
        return mFolder;
    }

    @Override
    public final void onMainContentItemSelected(final int position, Items.ListItem listItem) {
        showConversation(position, listItem);

    }

    protected void showConversation(final int position, Items.ListItem listItem) {

// set the current item
        setTitle(listItem.content);
    }

    private void setListContext() {

    }


    @Override
    public void onFolderChanged(Folder folder, final boolean force) {
        changeFolder(folder, force);
    }

    private void changeFolder(Folder folder, final boolean force) {

        GenericListContext viewContext =  GenericListContext.forFolder(folder);
        showConversationList(viewContext);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
    // </editor-fold>

    // <editor-fold desc="State ">

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewMode.handleSaveInstanceState(outState);
        if (mAccount != null) {
            outState.putParcelable(SAVED_ACCOUNT, mAccount);
        }
        if (mFolder != null) {
            outState.putParcelable(SAVED_FOLDER, mFolder);
        }
    }

    // </editor-fold>

    // <editor-fold desc="Register observers ">

    @Override
    public void registerAccountObserver(DataSetObserver obs) {
        mAccountObservers.registerObserver(obs);
    }

    /**
     * Removes a listener from receiving current account changes.
     * Must happen in the UI thread.
     */
    @Override
    public void unregisterAccountObserver(DataSetObserver obs) {
        mAccountObservers.unregisterObserver(obs);
    }

    @Override
    public void registerDrawerClosedObserver(DataSetObserver observer) {
        mDrawerObservers.registerObserver(observer);
    }

    @Override
    public void unregisterDrawerClosedObserver(DataSetObserver observer) {
        mDrawerObservers.unregisterObserver(observer);
    }

    @Override
    public void registerFolderObserver(DataSetObserver observer) {
        mFolderObservers.registerObserver(observer);
    }

    @Override
    public void unregisterFolderObserver(DataSetObserver observer) {
        try {
            mFolderObservers.unregisterObserver(observer);
        } catch (IllegalStateException e) {
            // Log instead of crash

        }
    }

    // </editor-fold>

    // <editor-fold desc="Intents ">


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {




//            if (isConversationMode && mViewMode.getMode() == ViewMode.UNKNOWN) {
//                mViewMode.enterConversationMode();
//            } else {
            mViewMode.enterConversationListMode();
//            }
            // Put the folder and conversation, and ask the loader to create this folder.
            final Bundle args = new Bundle();

            final Uri folderUri;
            if (intent.hasExtra(Utils.EXTRA_FOLDER_URI)) {
                folderUri = (Uri) intent.getParcelableExtra(Utils.EXTRA_FOLDER_URI);
            } else if (intent.hasExtra(Utils.EXTRA_FOLDER)) {
                folderUri = Uri.EMPTY;
            } else {
                final Bundle extras = intent.getExtras();
                // folderUri = Uri.EMPTY;
                folderUri = MockContract.Folders.CONTENT_URI;

            }


            args.putParcelable(Utils.EXTRA_FOLDER_URI, folderUri);
            args.putParcelable(Utils.EXTRA_CONVERSATION,
                    intent.getParcelableExtra(Utils.EXTRA_CONVERSATION));
            //ok all packaged up and ready to start loading, but first we need some fake query data to work with.
            restartOptionalLoader(LOADER_FIRST_FOLDER, mFolderCallbacks, args);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // we are not ready for search yet
        }


    }
    // </editor-fold>

    // <editor-fold desc="Accounts ">


    @Override
    public Account getCurrentAccount() {
        return mAccount;
    }


    @Override
    public Account getAccount() {
        return mAccount;
    }


    private boolean updateAccounts(ObjectCursor<Account> accounts) {
        if (accounts == null || !accounts.moveToFirst()) {
            return false;
        }

        // getAllAccounts() is a static method on the Account class
        final Account[] allAccounts = Account.getAllAccounts(accounts);
        //just get the first one
        Account newAccount = allAccounts[0];
        // assume always changed
        changeAccount(newAccount);
        return true;
    }

    @Override
    public void changeAccount(Account account) {
        // Change the account here
        setAccount(account);
    }

    private void setAccount(Account account) {
        mAccount = account;
        mActivity.supportInvalidateOptionsMenu();
        mAccountObservers.notifyChanged();
    }

    // </editor-fold>

    // <editor-fold desc="Loaders ">

    private class FolderLoads implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            final String[] everything = MockContract.FOLDERS_PROJECTION;

            switch (id) {

                case LOADER_FIRST_FOLDER:

                    final Uri folderUri = args.getParcelable(Utils.EXTRA_FOLDER_URI);

                   // final Uri folderUri = Uri.parse( "content://com.android.mail.mockprovider/account/0/folders");
                    return new CursorLoader(mContext, folderUri,
                            everything , null, null, null);

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
                return;
            }

            switch (loader.getId()) {
                case LOADER_FIRST_FOLDER:

                    int folderId = data.getInt(data.getColumnIndex(MockContract.Folders._ID));
                    String folderName = data.getString(data.getColumnIndex(MockContract.Folders.FOLDER_NAME));

                    Folder folder = new Folder(folderId, folderName);
                    boolean handled = false;
                    if (folder != null) {
                        onFolderChanged(folder, false /* force */);
                        handled = true;
                    }
                    mActivity.getSupportLoaderManager().destroyLoader(LOADER_FIRST_FOLDER);
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


    private class AccountLoads implements LoaderManager.LoaderCallbacks<ObjectCursor<Account>>{

        @Override
        public Loader<ObjectCursor<Account>>  onCreateLoader(int id, Bundle args) {
            //final String[] mProjection = MockContract.ACCOUNTS_PROJECTION;
            final String[] mProjection = UIProvider.ACCOUNTS_PROJECTION;
            final CursorCreator<Account> mFactory = Account.FACTORY;
           // final UnifiedAccountCacheProvider provider = new UnifiedAccountCacheProvider();

            switch (id) {
                case LOADER_ACCOUNT_CURSOR:
                    // NOTE: this calls the MailAppProvider which then calls the MockUiProvider
                    final Uri accountUri =  MailAppProvider.getAccountsUri();
                    return new MyObjectCursorLoader<Account>(mContext,
                            accountUri , mProjection, mFactory);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ObjectCursor<Account>> loader,
                                   ObjectCursor<Account> data) {

            if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
                return;
            }

            switch (loader.getId()) {
                case LOADER_ACCOUNT_CURSOR:
                   final long count = data.getCount();

                    if (count == 0) {


                    } else {
                       // final boolean accountListUpdated = accountsUpdated(data);
                        if (!mHaveAccountList /*|| accountListUpdated*/) {
                            mHaveAccountList = updateAccounts(data);
                        }
                    }


                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<ObjectCursor<Account>> loader) {
            // For whatever reason, the Loader's data is now unavailable.
            // Remove any references to the old data by replacing it with
            // a null Cursor.
            // mAdapter.swapCursor(null);
        }
    }


    private void restartOptionalLoader(int id, LoaderManager.LoaderCallbacks handler, Bundle args) {
        final LoaderManager lm = mActivity.getSupportLoaderManager();
        lm.destroyLoader(id);
        lm.restartLoader(id, args, handler);
    }

    // </editor-fold>

    // <editor-fold desc="Drawer ">

    private void SetupDrawerLayout() {

        mFragmentContainerView = mActivity.findViewById(R.id.navdrawer);

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mTitle = mDrawerTitle =  mActivity.getTitle();

        // this will be the ListFragment for navigation
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                mActivity.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                mActivity,                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                if (!isAdded()) {
//                    return;
//                }
                getActionBar().setTitle(mTitle);

                // When closed, we want to use either the burger, or up, based on where we are
                final int mode = mViewMode.getMode();
                // final boolean isTopLevel = (mFolder == null) || (mFolder.parent == Uri.EMPTY);
                mDrawerToggle.setDrawerIndicatorEnabled(getShouldShowDrawerIndicator(mode, true));


                mActivity.supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                if (opened != null && !opened) {
                    opened = true;
                    if (prefs != null) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(OPENED_KEY, true);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                if (!isAdded()) {
//                    return;
//                }

//                if (!mUserLearnedDrawer) {
//                    // The user manually opened the drawer; store this flag to prevent auto-showing
//                    // the navigation drawer automatically in the future.
//                    mUserLearnedDrawer = true;
//                    SharedPreferences sp = PreferenceManager
//                            .getDefaultSharedPreferences(getActivity());
//                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
//                }
                getActionBar().setTitle(mDrawerTitle);
                mActivity.supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                mActivity.supportInvalidateOptionsMenu();
                //onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //  updateStatusBarForNavDrawerSlide(slideOffset);
                //  onNavDrawerSlide(slideOffset);
                // If we're sliding, we always want to show the burger
                mDrawerToggle.setDrawerIndicatorEnabled(true /* enable */);
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    @Override
    public  void closeDrawer(boolean hasNewFolderOrAccount, Account nextAccount, Folder nextFolder) {
        if (!isDrawerEnabled()) {
            mDrawerObservers.notifyChanged();
            return;
        }

        Log.d(TAG,"CloseDrawer");

        mDrawerObservers.notifyChanged();

    }

    protected void toggleDrawerState() {
        if (!isDrawerEnabled()) {
            return;
        }
        if (mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
    }


    protected void closeDrawerIfOpen() {
        if (!isDrawerEnabled()) {
            return;
        }
        if(mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
            mDrawerLayout.closeDrawers();
        }
    }

    // </editor-fold>

    // <editor-fold desc="Google Play Services">


    /** Returns the Google account manager. */
   // public final GoogleAccountManager getGoogleAccountManager() {
//        return accountManager;
//    }

    /** Returns all Google accounts or {@code null} for none. */
//    public final android.accounts.Account[] getAllAccounts() {
//        return accountManager.getAccounts();
//    }

//    public GoogleApiClient getGoogleApiClient() {
//        return mLoginAndAuthHelper.getGoogleApiClient();
//    }

//    private void startGooglePlayLoginProcess() {
//        Log.d(TAG, "Starting login process.");
//       // AccountUtils.setActiveAccount(mActivity, null); test only
//
//        if (!GoogleAccountUtils.hasActiveAccount(mActivity)) {
//            Log.d(TAG, "No active account, attempting to pick a default.");
//            String defaultAccount = accountManager.getDefaultAccount();
//            if (defaultAccount == null) {
//                Log.e(TAG, "Failed to pick default account (no accounts). Failing.");
//                PlayServicesUtils.complainMustHaveGoogleAccount(mActivity);
//                return;
//            }
//            Log.d(TAG, "Default to: " + defaultAccount);
//            GoogleAccountUtils.setActiveAccount(mActivity, defaultAccount);
//        }
//
//        if (!GoogleAccountUtils.hasActiveAccount(mActivity)) {
//            Log.d(TAG, "Can't proceed with login -- no account chosen.");
//            return;
//        } else {
//            Log.d(TAG, "Chosen account: " + GoogleAccountUtils.getActiveAccountName(mActivity));
//        }
//
//        String accountName = GoogleAccountUtils.getActiveAccountName(mActivity);
//
//
//
//        if (mLoginAndAuthHelper != null && mLoginAndAuthHelper.getAccountName().equals(accountName)) {
//            Log.d(TAG, "Helper already set up; simply starting it.");
//            mLoginAndAuthHelper.start();
//            return;
//        }
//
//        Log.d(TAG, "Creating and starting new mLoginAndAuthHelper with account: " + accountName);
//        mLoginAndAuthHelper = new LoginAndAuthHelper(mActivity, this,  accountName);
//        mLoginAndAuthHelper.start();
//
//    }



    // LoginAndAuthHelper callbacks
    @Override
    public void onPlusInfoLoaded(String accountName) {
        Log.d(TAG, "Plus Info loaded.");
       setupAccountBox();
       // AddDriveFile();
      //  AddFolder();
      //  ListFolder();
    }


//    public void showMessage(String message) {
//        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
//    }

    // LoginAndAuthHelper callbacks
    @Override
    public void onAuthSuccess(String accountName, boolean newlyAuthenticated) {
        android.accounts.Account account = new android.accounts.Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        Log.d(TAG, "onAuthSuccess, account " + accountName + ", newlyAuthenticated=" + newlyAuthenticated);

        //refreshAccountDependantData();

        if (newlyAuthenticated) {
            Log.d(TAG, "Enabling auto sync on content provider for account " + accountName);
           // SyncHelper.updateSyncInterval(this, account);
           // SyncHelper.requestManualSync(account);
        }

        setupAccountBox();
     //   populateNavDrawer();
     //   registerGCMClient();
    }

    // LoginAndAuthHelper callbacks
    @Override
    public void onAuthFailure(String accountName) {
        Log.d(TAG, "Auth failed for account " + accountName);
       // refreshAccountDependantData();
    }

    private void setupAccountBox() {
        mAccountListContainer = (LinearLayout) mActivity.findViewById(R.id.account_list);

        if (mAccountListContainer == null) {
            //This activity does not have an account box
            return;
        }

        final View chosenAccountView = mActivity.findViewById(R.id.chosen_account_view);
        android.accounts.Account chosenAccount = GoogleAccountUtils.getActiveAccount(mActivity);
        if (chosenAccount == null) {
            // No account logged in; hide account box
            chosenAccountView.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            return;
        } else {
            chosenAccountView.setVisibility(View.VISIBLE);
            mAccountListContainer.setVisibility(View.VISIBLE);
        }

        android.accounts.Account[] accountArray = accountManager.getGoogleAccountByType();
        List<android.accounts.Account> accounts = new ArrayList<android.accounts.Account>(Arrays.asList(accountArray));
        accounts.remove(chosenAccount);

        ImageView coverImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_cover_image);
        ImageView profileImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_image);
        TextView nameTextView = (TextView) chosenAccountView.findViewById(R.id.profile_name_text);
        TextView email = (TextView) chosenAccountView.findViewById(R.id.profile_email_text);
        mExpandAccountBoxIndicator = (ImageView) mActivity.findViewById(R.id.expand_account_box_indicator);

        String name = GoogleAccountUtils.getPlusName(mActivity);
        if (name == null) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(name);
        }
        ImageLoader mImageLoader = VolleyController.getInstance(mActivity).getImageLoader();

        String imageUrl = GoogleAccountUtils.getPlusImageUrl(mActivity);
        if (imageUrl != null) {
            mImageLoader.get(imageUrl,
                    ImageLoader.getImageListener(profileImageView,
                            R.drawable.person_image_empty,
                            R.drawable.person_image_empty));
        }

        String coverImageUrl = GoogleAccountUtils.getPlusCoverUrl(mActivity);

        if (coverImageUrl != null) {
            mImageLoader.get(coverImageUrl,
                    ImageLoader.getImageListener(coverImageView,
                            R.drawable.person_image_empty,
                            R.drawable.person_image_empty));
        } else {
            coverImageView.setImageResource(R.drawable.default_cover);
        }

        email.setText(chosenAccount.name);

        if (accounts.isEmpty()) {
            // There's only one account on the device, so no need for a switcher.
            mExpandAccountBoxIndicator.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            chosenAccountView.setEnabled(false);
            return;
        }

        chosenAccountView.setEnabled(true);

        mExpandAccountBoxIndicator.setVisibility(View.VISIBLE);
        chosenAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccountBoxExpanded = !mAccountBoxExpanded;
                setupAccountBoxToggle();
            }
        });
        setupAccountBoxToggle();

        populateAccountList(accounts);

    }

    private void setupAccountBoxToggle() {
        mExpandAccountBoxIndicator.setImageResource(mAccountBoxExpanded
                ? R.drawable.ic_drawer_accounts_collapse
                : R.drawable.ic_drawer_accounts_expand);

        int hideTranslateY = -mAccountListContainer.getHeight() / 4; // last 25% of animation

                mAccountListContainer.setVisibility(mAccountBoxExpanded
                        ? View.VISIBLE : View.INVISIBLE);
        if (mAccountBoxExpanded) {
            mAccountListContainer.setVisibility(View.VISIBLE);
        } else {
            mAccountListContainer.setVisibility(View.GONE);
        }
    }


    private void populateAccountList(List<android.accounts.Account> accounts) {
        mAccountListContainer.removeAllViews();

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        for (android.accounts.Account account : accounts) {
            View itemView = layoutInflater.inflate(R.layout.list_item_account,
                    mAccountListContainer, false);
            ((TextView) itemView.findViewById(R.id.profile_email_text))
                    .setText(account.name);
            final String accountName = account.name;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager cm = (ConnectivityManager)mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork == null || !activeNetwork.isConnected()) {
                        // if there's no network, don't try to change the selected account
//                        Toast.makeText(BaseActivity.this, R.string.no_connection_cant_login,
//                                Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(Gravity.START);
                    } else {
                        Log.d(TAG, "User requested switch to account: " + accountName);
//                        AccountUtils.setActiveAccount(mActivity, accountName);
//                        onAccountChangeRequested();
//                        startLoginProcess();
                        mAccountBoxExpanded = false;
                        setupAccountBoxToggle();
                        mDrawerLayout.closeDrawer(Gravity.START);
//                        setupAccountBox();
                    }
                }
            });
            mAccountListContainer.addView(itemView);
        }
    }

    protected void onAccountChangeRequested() {
        // override if you want to be notified when another account has been selected account has changed
    }





    // </editor-fold>


// end drive methods
    // <editor-fold desc=" ">
    // </editor-fold>




}
