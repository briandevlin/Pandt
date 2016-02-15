package com.bdevlin.apps.ui.activity.core;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;

import android.util.Log;
import android.util.TypedValue;
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
import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;
import com.bdevlin.apps.utils.GenericListContext;

import com.bdevlin.apps.pandt.Controllers.PagerController;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.ViewMode;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.utils.GoogleAccountUtils;

import com.bdevlin.apps.utils.GoogleAccountManager;
import com.bdevlin.apps.utils.VolleyController;
import com.bdevlin.apps.utils.LoginAndAuthHelper;

import com.bdevlin.apps.utils.Utils;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import android.view.animation.AccelerateDecelerateInterpolator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by brian on 7/20/2014.
 */
public abstract class UIControllerBase implements ActivityController {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerBase.class.getSimpleName();
    private static final boolean DEBUG = true;
   // private static final String SAVED_ACCOUNT = "saved-account";
    private static final String SAVED_FOLDER = "saved-folder";
   // private static final String SAVED_ACTION = "saved-action";
   // private static final int ADD_ACCOUNT_REQUEST_CODE = 1;
    private static final String OPENED_KEY = "OPENED_KEY";
   // private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;
   // private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    private final GoogleAccountManager accountManager;
   // private final GoogleDriveManager driveManager;

    public static final String TAG_MAIN_LIST = "tag-main-list";
    protected final HomeActivity mActivity;
    protected final Context mContext;
    private final FragmentManager mFragmentManager;
    private final LoaderManager mLoaderManager;
    private final ActionBar mActionBar;


    protected View mSliderLayout;
    //private ViewGroup mDrawerItemsListContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    protected CharSequence mTitle;
    protected PagerController mPagerController;
    //private Account mAccount;
    protected Folder mFolder;
   // private boolean mFolderChanged = false;
    protected final ViewMode mViewMode;
    protected ContentResolver mResolver;
    //private ImageLoader mImageLoader;
    private boolean mAccountBoxExpanded = false;

//    private final DataSetObservable mDrawerObservers = new DataSetObservable();
//    private final DataSetObservable mFolderObservers = new DataSetObservable();
//    private final DataSetObservable mAccountObservers = new DataSetObservable();

//    private static final int LOADER_ACCOUNT_CURSOR = 0;
//    public static final int LOADER_FIRST_FOLDER = 1;

    //private final FolderLoads mFolderCallbacks = new FolderLoads();

    //private boolean mHaveAccountList = false;

    protected GenericListContext mConvListContext;

    // the LoginAndAuthHelper handles signing in to Google Play Services and OAuth
    private LoginAndAuthHelper mLoginAndAuthHelper;

    private CharSequence mDrawerTitle;
    private Boolean opened = null;
    private SharedPreferences prefs = null;
    private LinearLayout mAccountListContainer;
    private ImageView mExpandAccountBoxIndicator;

    // private String mNextPageToken;
    public DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
   // private AppBarLayout appBarLayout;
    private ViewGroup mRootView;
    private android.support.design.widget.CoordinatorLayout mCoordLayout;
    protected CollapsingToolbarLayout mCollapsingToolbar;
    protected boolean mActionBarDrawerToggleEnabled = true;
    protected boolean mAnimateActionBarDrawerToggle = false;
    private boolean mShowDrawerOnFirstLaunch = true;
   // protected Drawable mSliderBackgroundDrawable = null;
    //private NavigationCursorRecyclerAdapter mRecycleCursorAdapter;
   // private int mThemedStatusBarColor;

    private int mNormalStatusBarColor;
    KenBurnsView kenBurnsView;
   // private ImageView mHeaderLogo;
    private ImageView icon;
    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    int[] photos={R.drawable.photo1};
    private int mActionBarHeight;
    private int mMinHeaderHeight;
   // private int mHeaderHeight;
    //private int mMinHeaderTranslation;
    private TypedValue mTypedValue = new TypedValue();

    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();


// </editor-fold>

    // <editor-fold desc="Constructor">

    public UIControllerBase(HomeActivity activity, ViewMode viewMode) {
        mActivity = activity;

        mContext = activity.getApplicationContext();
        final Resources r = mContext != null ? mContext.getResources() : null;
        mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        mFragmentManager = activity.getSupportFragmentManager();
        mLoaderManager = mActivity.getSupportLoaderManager();
        mResolver = mActivity.getContentResolver();

        boolean mIsTablet = Utils.useTabletUI(r);
        mViewMode = viewMode;
        mActionBar = getSupportActionBar();
        accountManager = new GoogleAccountManager(mContext);
       // driveManager = new GoogleDriveManager(mContext);//FIXME - not really implemented fully

    }
    // </editor-fold>

    // <editor-fold desc="life cycle methods">

    // method defined in ActivityController
    @Override
    public boolean onCreate(Bundle savedInstanceState) {

        // Allow shortcut keys to function for the ActionBar and menus.
        mActivity.setDefaultKeyMode(Activity.DEFAULT_KEYS_SHORTCUT);

        mViewMode.addListener(this);
        mPagerController = new PagerController(mActivity, this, mFragmentManager);
        // mPagerController.show(1,null);// used this for testing

        final Intent intent = mActivity.getIntent();
       // prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        if (savedInstanceState != null) {

        } else if (intent != null) {
           // handleIntent(intent);
        }
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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
    public void onStart() {
        if (DEBUG) Log.d(TAG, "onStart");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // startGooglePlayLoginProcess();
        }
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.d(TAG, "onStop");
        if (mLoginAndAuthHelper != null) {
            mLoginAndAuthHelper.stop();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        if (mDrawerToggle != null) {
           // mDrawerToggle.setDrawerIndicatorEnabled(isDrawerEnabled());
            mDrawerToggle.syncState();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                opened = preferences.getBoolean(OPENED_KEY, false);
                if (!opened) {
                    if (mDrawerLayout != null) {
                        mDrawerLayout.openDrawer(mSliderLayout);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        // Invalidating the options menu so that when we make changes in settings,
        // the changes will always be updated in the action bar/options menu/
        mActivity.supportInvalidateOptionsMenu();

        // Verifies the proper version of Google Play Services exists on the device.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //PlayServicesUtils.checkGooglePlaySevices(mActivity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mLoginAndAuthHelper != null) {
            mLoginAndAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == mActivity.RESULT_OK) {
                if (DEBUG) Log.d(TAG, "onActivityResult OK");
                closeDrawerIfOpen();
            }
        }
    }

    @Override
    public void onViewModeChanged(int newMode) {
        // we get here from viewmode.dispatchModeChange()-> UIControllerOnePane.onViewModeChanged
        closeDrawerIfOpen();
    }

    @Override
    public boolean onBackPressed(boolean isSystemBackKey) {

        if (mDrawerLayout != null) {
            if (isDrawerEnabled() && mSliderLayout != null && mDrawerLayout.isDrawerVisible(mSliderLayout)) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        }
        return handleBackPress(isSystemBackKey);
    }

    @Override
    public final boolean onUpPressed() {
        return handleUpPress();
    }


    // </editor-fold>

    // <editor-fold desc="Getters ">

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public ActionBar getSupportActionBar() {
       // return ((AppCompatActivity) mActivity).getSupportActionBar();
      return  mActivity.getSupportActionBar();
    }

    public Toolbar getSupportToolBar() {
        if (mToolbar != null) {
            return mToolbar;
        }
        return null;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        if (mDrawerToggle != null) {
            return mDrawerToggle;
        }
        return null;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        if (mCollapsingToolbar != null) {
            return mCollapsingToolbar;
        }
        return null;
    }

    private ImageView getActionBarIconView() {
        return icon;
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

    protected NavigationDrawerFragment getNavigationDrawerFragment() {
        final Fragment fragment = mFragmentManager.findFragmentById(R.id.navigation_drawer_fragment);
        if (isValidFragment(fragment)) {
            return (NavigationDrawerFragment) fragment;
        }
        return null;
    }

    protected MainContentFragment getMainContentFragment() {
        final Fragment fragment = mFragmentManager.findFragmentById(R.id.main_content_fragment);
        if (isValidFragment(fragment)) {
            return (MainContentFragment) fragment;
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
//            case R.baseId.menu_about:
//                HelpUtils.showAbout(this);
//                return true;

            case R.id.action_settings:
                Toast.makeText(mActivity, "Example action.", Toast.LENGTH_SHORT).show();
              /*  Intent intentPrefs = new Intent(mActivity,
                        PreferencesActivity.class);
                mActivity.startActivity(intentPrefs);*/
                //  HelpUtils.showDialog(mActivity);
                return true;
            case android.R.id.home:
                onUpPressed();
                return true;
            default:
                handled = false;
        }
        return handled;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mDrawerLayout != null && !isDrawerOpen()) {

            //showGlobalContextActionBar();
        }
        mActivity.getMenuInflater().inflate(R.menu.home, (Menu) menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the baseName view
        if (mDrawerLayout != null) {
            boolean drawerOpen = isDrawerOpen();
        }
        // menu.findItem(R.baseId.action_websearch).setVisible(!drawerOpen);
        return true;
    }


    public void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(mActivity);
           // builder.addNextIntentWithParentStack(intent);
            builder.addNextIntent(intent);
            builder.startActivities();

        } else {
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    // </editor-fold>

    // <editor-fold desc="Misc ">

    // protected abstract boolean isConversationListVisible();

    @Override
    public GenericListContext getCurrentListContext() {
        return mConvListContext;
    }

    @Override
    public void showMainContentItemsList(GenericListContext listContext) {

    }

    /**
     * Returns the context.
     */
    public final Context getContext() {
        return mContext;
    }

    protected void showMainContentItemPager(final int position) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mActivity.supportInvalidateOptionsMenu();
    }
    // </editor-fold>

    // <editor-fold desc="State ">

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewMode.handleSaveInstanceState(outState);

        if (mFolder != null) {
            outState.putParcelable(SAVED_FOLDER, mFolder);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {


    }

    // </editor-fold>

    // <editor-fold desc="Intents ">
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String component = intent.getComponent().getClassName();
        Log.d(TAG, "component: " + component);
        Log.d(TAG, "action: " + action);

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {


//            if (isConversationMode && mViewMode.getMode() == ViewMode.UNKNOWN) {
//                mViewMode.enterMainContentItemPagerMode();
//            } else {
            mViewMode.enterMainContentListMode();
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
           // restartOptionalLoader(LOADER_FIRST_FOLDER, mFolderCallbacks, args);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // we are not ready for search yet
        }


    }
    // </editor-fold>

    // <editor-fold desc="Drawer ">

    protected void handleDrawerNavigation() {

        final View.OnClickListener toolbarNavigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean handled = false;


                if ((mDrawerToggle != null && !mDrawerToggle.isDrawerIndicatorEnabled())) {
                    onUpPressed();
                    handled = true;
                }

                if (!handled) {
                    if (isDrawerOpen()) {
                        mDrawerLayout.closeDrawer(mSliderLayout);
                    } else {
                        mDrawerLayout.openDrawer(mSliderLayout);
                    }
                }
            }
        };


        // create the ActionBarDrawerToggle if not set and enabled and if we have a toolbar
        if (mActionBarDrawerToggleEnabled && mDrawerToggle == null && mToolbar != null) {

            this.mDrawerToggle = new ActionBarDrawerToggle(
                    mActivity,
                    mDrawerLayout,
                    mToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                 /*   if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerOpened(drawerView);
                    }*/
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                   /* if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerClosed(drawerView);
                    }*/
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                   /* if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }*/

                    if (!mAnimateActionBarDrawerToggle) {
                        super.onDrawerSlide(drawerView, 0);
                    } else {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            this.mDrawerToggle.syncState();
        }

        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(toolbarNavigationListener);
        }

        //handle the ActionBarDrawerToggle
        if (mDrawerToggle != null) {
            mDrawerToggle.setToolbarNavigationClickListener(toolbarNavigationListener);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        } else {
            mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                   /* if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }*/
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                   /* if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerOpened(drawerView);
                    }*/
                    if (DEBUG) Log.d(TAG, "onDrawerOpened");
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                   /* if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerClosed(drawerView);
                    }*/
                    if (DEBUG) Log.d(TAG, "onDrawerClosed");
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    if (DEBUG) Log.d(TAG, "onDrawerStateChanged");
                }
            });
        }


    }

    private void handleShowOnFirstLaunch() {
        //check if it should be shown on first launch (and we have a drawerLayout)
        if (mActivity != null && mDrawerLayout != null && mShowDrawerOnFirstLaunch) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
            //if it was not shown yet
            if (!preferences.getBoolean(OPENED_KEY, false)) {
                //open the drawer
                mDrawerLayout.openDrawer(mSliderLayout);

                //save that it showed up once ;)
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(OPENED_KEY, true);
                editor.apply();
            }
        }
    }

    private Toolbar initActionBarToolbar() {

        if (mToolbar == null) {
            mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
            if (mToolbar != null) {
                mActivity.setSupportActionBar(mToolbar);
            }
        }

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeButtonEnabled(false);
            // actionBar.setDisplayShowHomeEnabled(true);
        }

        return mToolbar;
    }

    public void SetUpActionBarAndFAB()
    {
        initActionBarToolbar();
        initFab();
    }

    public void SetupDrawerLayout() {

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                mActivity.getResources().getColor(R.color.colorPrimaryDark));

        mCoordLayout = (CoordinatorLayout) mActivity.findViewById(R.id.coordinatorLayout);
        mCoordLayout.setStatusBarBackgroundColor(mActivity.getResources().getColor(R.color.accent_material_light));
       // appBarLayout = (AppBarLayout) mActivity.findViewById(R.id.toolbar_container);
        mCollapsingToolbar = (CollapsingToolbarLayout) mActivity.findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle("Main");
        icon = (ImageView) mActivity.findViewById(R.id.icon);
        ViewCompat.setAlpha(getActionBarIconView(), 0f);


        kenBurnsView =(KenBurnsView) mCollapsingToolbar.findViewById(R.id.header_picture);
       // mHeaderLogo = (ImageView) mActivity.findViewById(R.id.header_thumbnail);

        mMinHeaderHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.min_header_height);
       // mHeaderHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.header_height);
       // mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();


      //  getSupportActionBar().setBackgroundDrawable(null);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                // change images randomly
                Random ran=new Random();
                int i=ran.nextInt(photos.length);
                //set image resources
                kenBurnsView.setImageResource(photos[i]);
                i++;
                if(i>photos.length-1)
                {
                    i=0;
                }
                handler.postDelayed(this, 7000);  //for interval...
            }
        };
       // handler.postDelayed(runnable, 7000); //for initial delay..

        mSliderLayout = mActivity.findViewById(R.id.navdrawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setStatusBarBackgroundColor(mActivity.getResources().getColor(R.color.materialize_primary_light));
        //mDrawerLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.accent_material_light));
        //mSliderLayout.setBackgroundResource(R.drawable.default_cover);
        mTitle = mDrawerTitle = mActivity.getTitle();


        handleDrawerNavigation();
        handleShowOnFirstLaunch();

    }

    boolean isDrawerOpen() {
        return mDrawerLayout != null && mSliderLayout != null && mDrawerLayout.isDrawerOpen(mSliderLayout);
    }



    protected void toggleDrawerState() {
        if (!isDrawerEnabled()) {
            return;
        }
        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(mSliderLayout);
        }
    }

    protected void closeDrawerIfOpen() {
        if (!isDrawerEnabled()) {
            return;
        }
        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onMainContentScrolled(RecyclerView recyclerView, int dx, int dy) {
    }

    @Override
    public void onMainContentItemSwipe(final CardView cardView, final SwipeDismissBehavior<CardView> swipe) {

   // Log.i(TAG, "onMainContentItemSwipe");

    }
    // </editor-fold>

    // <editor-fold desc="Google Play Services">

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }


    /**
     * Returns the Google account manager.
     */
    public final GoogleAccountManager getGoogleAccountManager() {
        return accountManager;
    }

    /**
     * Returns all Google accounts or {@code null} for none.
     */
    public final android.accounts.Account[] getAllAccounts() {
        return accountManager.getAccounts();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mLoginAndAuthHelper.getGoogleApiClient();
    }

    private void startGooglePlayLoginProcess() {
        if (DEBUG) Log.d(TAG, "Starting login process.");
        //GoogleAccountUtils.setActiveAccount(mActivity, null); //test only
        String accountName = accountManager.getActiveOrDefaultAccount(mActivity);

        if (mLoginAndAuthHelper != null && mLoginAndAuthHelper.getAccountName().equals(accountName)) {
            if (DEBUG) Log.d(TAG, "Helper already set up; simply starting it.");
            mLoginAndAuthHelper.start();
            return;
        }

        if (DEBUG)
            Log.d(TAG, "Creating and starting new mLoginAndAuthHelper with account: " + accountName);
        mLoginAndAuthHelper = new LoginAndAuthHelper(mActivity, this, accountName);
        mLoginAndAuthHelper.start();

    }


    // LoginAndAuthHelper callbacks
    @Override
    public void onPlusInfoLoaded(String accountName) {
        if (DEBUG) Log.d(TAG, "Plus Info loaded.");
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
        if (DEBUG)
            Log.d(TAG, "onAuthSuccess, account " + accountName + ", newlyAuthenticated=" + newlyAuthenticated);

        //refreshAccountDependantData();

        if (newlyAuthenticated) {
            if (DEBUG)
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
        if (DEBUG) Log.d(TAG, "Auth failed for account " + accountName);
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

        // testing only
        Bitmap bitmap = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {

                    int vibrant = palette.getVibrantColor(0x000000);
                    int vibrantLight = palette.getLightVibrantColor(0x000000);
                    int vibrantDark = palette.getDarkVibrantColor(0x000000);
                    int muted = palette.getMutedColor(0x000000);
                    int mutedLight = palette.getLightMutedColor(0x000000);
                    int mutedDark = palette.getDarkMutedColor(0x000000);
                }
            });
        }
        // Asynchronous


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
                    ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork == null || !activeNetwork.isConnected()) {
                        // if there's no network, don't try to change the selected account
//                        Toast.makeText(BaseActivity.this, R.string.no_connection_cant_login,
//                                Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        if (DEBUG) Log.d(TAG, "User requested switch to account: " + accountName);
//                        AccountUtils.setActiveAccount(mActivity, accountName);
//                        onAccountChangeRequested();
//                        startLoginProcess();
                        mAccountBoxExpanded = false;
                        setupAccountBoxToggle();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
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

}
