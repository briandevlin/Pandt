    package com.bdevlin.apps.ui.activity.core;

    import android.content.Intent;
    import android.content.pm.ActivityInfo;
    import android.content.res.Resources;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.design.widget.SwipeDismissBehavior;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentTransaction;
    import android.support.v4.view.ViewCompat;
    import android.support.v4.view.ViewPager;
    import android.support.v7.app.ActionBar;
    import android.support.v7.widget.CardView;
    import android.support.v7.widget.RecyclerView;
    import android.util.Log;
    import android.view.View;
    import android.widget.TextView;

    import com.bdevlin.apps.pandt.Controllers.ActivityController;
    import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;
    import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
    import com.bdevlin.apps.provider.PandTContract;
    import com.bdevlin.apps.utils.GenericListContext;
    import com.bdevlin.apps.pandt.folders.Folder;
    import com.bdevlin.apps.ui.fragments.MainContentFragment;
    import com.bdevlin.apps.pandt.R;
    import com.bdevlin.apps.utils.ViewMode;


    /**
     * Created by brian on 7/20/2014.
     */
    public class UIControllerOnePane extends UIControllerBase {

        // <editor-fold desc="Fields">
        private static final String TAG = UIControllerOnePane.class.getSimpleName();
        private static final boolean DEBUG = true;
        private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
        private ViewPager mViewPager;

        // </editor-fold>

        // <editor-fold desc="Constructor">
        public UIControllerOnePane(PandtActivity activity, ViewMode viewMode) {
            super(activity, viewMode);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // </editor-fold>

        // <editor-fold desc="life cycle methods">
        @Override
        public boolean onCreate(Bundle savedInstanceState) {

            // gets the one pane activity layout
            mActivity.setContentView(R.layout.activity_home);

            // base sets this for both one pane and two pane controllers
            SetUpActionBarAndFAB();

            // this is the one pane controller so drawer is enabled
            SetupDrawerLayout();

            // add the fragment to the main content
            SetUpMainFragment();

            // The parent class sets the correct viewmode and starts the application off.
            // also instantiates the viewpager controller
            return super.onCreate(savedInstanceState);
        }

        private void SetUpMainFragment()
        {
            FragmentManager mFragmentManager = mActivity.getSupportFragmentManager();

            MainContentFragment itemListFragment = (MainContentFragment) mFragmentManager.findFragmentByTag(
                    TAG_MAIN_LIST);
            if (itemListFragment == null) {
                itemListFragment = MainContentFragment.newInstance(GenericListContext.forFolder(null), 0);

                replaceFragment(itemListFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        TAG_MAIN_LIST, R.id.main_content);
            }
        }

        @Override
        public void onStart() {
            super.onStart();

         //   final String[] mProjection = MockContract.SUBJECTMANAGER_PROJECTION;
          //  final Uri contentUri = MockContract.SubjectManager.CONTENT_URI;
          //  final Cursor inner = (Cursor)getContext().getContentResolver().query(contentUri, mProjection,null,null,null);
          //  Log.d(TAG,"count: " + inner.getCount());
          //  inner.moveToFirst();
          //  Log.d(TAG, "column 1: " + inner.getInt(MockContract.SUBJECTMANGER_SID_COLUMN));
         //   Log.d(TAG, Utils.dumpCursor(inner));
        final String[] mProjection = PandTContract.SESSIONS_PROJECTION;
       // final Uri contentUri = PandTContract.Accounts.buildAccountDirUri("11");
            final Uri contentUri = PandTContract.Sessions.CONTENT_URI;
       // final Cursor inner = (Cursor)getContext().getContentResolver().query(contentUri, mProjection,null,null,null);
//               // "_id=?", new String[] { String.valueOf(2) }, null);
 //           Log.d(TAG,"count: " + inner.getCount());
//            Log.d(TAG, "position: " + inner.getPosition());
         //   inner.moveToFirst();
//            Log.d(TAG, "position: " + inner.getPosition());
//            int id = inner.getInt(MockContract.Accounts.ACCOUNT_ID_COLUMN);
//            String name = inner.getString(MockContract.Accounts.ACCOUNT_NAME_COLUMN);
//            Log.d(TAG,"column 1: " + id);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

        }

        @Override
        public void onRestoreInstanceState(Bundle inState) {
            super.onRestoreInstanceState(inState);
            if (inState == null) {
                return;
            }
        }


        @Override
        public void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            View mainContent = mActivity.findViewById(R.id.main_content);
            if (mainContent != null) {
                ViewCompat.setAlpha(mainContent,0);
                ViewCompat.animate(mainContent).alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION).start();
            } else {
                Log.w(TAG, "No view with ID main_content to fade in.");
            }
        }

        // </editor-fold>

        // <editor-fold desc="Main content">
        @Override
        public void showMainContentItemsList(GenericListContext listContext) {
            super.showMainContentItemsList(listContext);
            if (DEBUG) Log.d(TAG, "showMainContentItemsList");
            mViewMode.enterMainContentListMode();
            onViewModeChanged(mActivity.getViewMode().getMode());

            final MainContentFragment itemListFragment = MainContentFragment.newInstance(listContext, 0);

            replaceFragment(itemListFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                    TAG_MAIN_LIST, R.id.main_content);

            mActivity.getSupportFragmentManager().executePendingTransactions();

        }


        // when the main content fragment list item is selected we end up here
        @Override
        protected void showMainContentItemPager(final int position, final int count) {
            super.showMainContentItemPager(position, count);
            if (DEBUG) Log.d(TAG, "showMainContentItemPager");
            mViewMode.enterMainContentItemPagerMode();


            final FragmentManager fm = mActivity.getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            // remove main content fragment to reveal the viewpager
            //final Fragment f = fm.findFragmentById(R.baseId.main_content);
            final Fragment f = fm.findFragmentByTag(TAG_MAIN_LIST);
            if (f != null && f.isAdded()) {
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(f);
                ft.commitAllowingStateLoss();
                fm.executePendingTransactions();
            }

            mPagerController.show(position, count);
        }
    // </editor-fold>

        // <editor-fold desc="Navigation">
        /* implements NavigationDrawerFragment.NavigationDrawerCallbacks*/
        @Override
        public void onNavigationDrawerItemSelected(View itemView, int position, IDrawerItem item) {
            if (DEBUG) Log.d(TAG, "onNavigationDrawerItemSelected");
            Folder folder = null;
                    // if (true) return;
           // toggleDrawerState();
            closeDrawerIfOpen();
            final Resources res = mActivity.getResources();
            if (itemView != null) {
                NavigationDrawerItem navItem = (NavigationDrawerItem) item;
               String baseName =  navItem.getBaseName().getText();
                String baseruri = navItem.geturiString();
                if (baseName.equals(res.getString(R.string.Grammar))) {
                    baseName =    baseName.concat(res.getString(R.string.patrsofSpeech));
                }
                folder = new Folder(navItem.getNavId(), baseName, baseruri);
            } else {

                folder = new Folder(1,
                        res.getString(R.string.completepartsofspeech),
                        res.getString(R.string.default_uri));
            }
            GenericListContext viewContext = GenericListContext.forFolder(folder);
            showMainContentItemsList(viewContext);

            mFolder = folder;
        }

        public void onNavigationDrawerArraySelected(View itemView, int position, IDrawerItem item) {
            if (DEBUG) Log.d(TAG, "onNavigationDrawerArraySelected");
           // if (true) return;
            // toggleDrawerState();
            closeDrawerIfOpen();

            TextView baseName = (TextView) itemView.findViewById(R.id.baseName);

            String id = baseName.getText().toString();//FIXME shouldn't need the tostring()

            switch (id) {
    //            case R.baseId.menu_about:
    //                HelpUtils.showAbout(this);
    //                return true;

                case "Settings":

                    Intent intentPrefs = new Intent(mActivity,
                            PreferencesActivity.class);
                    mActivity.startActivityForResult(intentPrefs, 1);
                    break;
    //            case "About":
    //                // HelpUtils.showDialog(mActivity);
    //                break;
                case "Help":
                    // access "About" from the help screen
                    Intent intentHelp = new Intent(mActivity,
                            HelpActivity.class);
                   // mActivity.createBackStack(intentHelp);
                    mActivity.startActivity(intentHelp);
                    break;

                default:

            }
        }

        @Override
        public final void onMainContentItemSelected(final int position, final int count) {
            if (DEBUG) Log.d(TAG, "onMainContentItemSelected");
            showMainContentItemPager(position, count);
        }

        @Override
        public final void onMainContentScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onMainContentScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onMainContentItemSwipe(CardView cardView, SwipeDismissBehavior<CardView> swipe) {
            super.onMainContentItemSwipe(cardView, swipe);
        }


        @Override
        protected boolean handleBackPress(boolean isSystemBackKey) {
            final int mode = mViewMode.getMode();

            if (mode == ViewMode.SEARCH_RESULTS_LIST) {
                mActivity.finish();
            } else if (mViewMode.isListMode()) {
                if (DEBUG) Log.d(TAG, "isListMode");
                // navigateUpFolderHierarchy();
                mActivity.finish();
            } else if (mViewMode.isConversationMode()) {
                if (DEBUG) Log.d(TAG, "isConversationMode");
                // transitionBackToConversationListMode();
                GenericListContext viewContext = GenericListContext.forFolder(mFolder);
                showMainContentItemsList(viewContext);

                mViewMode.enterMainContentListMode();
            } else {
                if (DEBUG) Log.d(TAG, "default mode");
                mActivity.finish();
            }

            return true;
        }

        @Override
        public boolean handleUpPress() {
            final int mode = mViewMode.getMode();
            if (mode == ViewMode.SEARCH_RESULTS_LIST) {
                mActivity.finish();
                // Not needed, the activity is going away anyway.
            } else if (mode == ViewMode.MAINCONTENT_LIST) {
                final boolean isTopLevel = (mFolder == null);

                if (isTopLevel) {
                    // Show the drawer.
                    toggleDrawerState();
                } else {
                    if (DEBUG) Log.d(TAG, "go up");
                    //navigateUpFolderHierarchy();
                }
            } else if (mode == ViewMode.CONVERSATION || mode == ViewMode.SEARCH_RESULTS_CONVERSATION
                    || mode == ViewMode.AD) {
                // Same as go back.
                if (DEBUG) Log.d(TAG, "Same as go back");
                handleBackPress(false);
            }
            return true;
        }

        @Override
        public void createBackStack(Intent intent) {
          super.createBackStack(intent);
        }

        // </editor-fold>

        // <editor-fold desc="fragments">

        /**
         * Replace the content_pane with the fragment specified here. The tag is specified so that
         * the {@link ActivityController} can look up the fragments through the
         * {@link android.app.FragmentManager}.
         *
         * @param fragment   the new fragment to put
         * @param transition the transition to show
         * @param tag        a tag for the fragment manager.
         * @param anchor     ID of view to replace fragment in
         * @return transaction ID returned when the transition is committed.
         */
        private int replaceFragment(Fragment fragment, int transition, String tag, int anchor) {
            final FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // fragmentTransaction.setTransition(transition);
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(anchor, fragment, tag);
            // fragmentTransaction.addToBackStack(tag);
            final int id = fragmentTransaction.commitAllowingStateLoss();
            fm.executePendingTransactions(); // had to remove this as it crashes because of the layout fragment in the authenticator
            return id;
        }

        protected MainContentFragment getMainContentFragment() {
            final FragmentManager fm = mActivity.getSupportFragmentManager();
            final Fragment fragment = fm.findFragmentByTag(TAG_MAIN_LIST);
            if (isValidFragment(fragment)) {
                return (MainContentFragment) fragment;
            }
            return null;
        }

        // </editor-fold>

        // <editor-fold desc="ViewMode">
        @Override
        public void onViewModeChanged(int newMode) {
            // we get here from viewmode.dispatchModeChange()
            super.onViewModeChanged(newMode);

            if (ViewMode.isListMode(newMode)) {
                mPagerController.hide(true);
                if (getDrawerToggle() != null){
                    getDrawerToggle().setDrawerIndicatorEnabled(true);
                    getDrawerToggle().syncState();
                }

                ActionBar ab = getSupportActionBar();
                if (ab != null){
                    ab.setDisplayHomeAsUpEnabled(false);
                    ab.setDisplayShowHomeEnabled(true);
                }


                closeDrawerIfOpen();
            }

            if (ViewMode.isConversationMode(newMode)) {
                if (getDrawerToggle() != null){
                    getDrawerToggle().setDrawerIndicatorEnabled(false);
                    getDrawerToggle().syncState();
                }

                ActionBar ab = getSupportActionBar();
                if (ab != null) {
                    ab.setDisplayHomeAsUpEnabled(true);
                    ab.setDisplayShowHomeEnabled(false);
                }


                closeDrawerIfOpen();
            }

        }

        // </editor-fold>

        @Override
        public boolean isDrawerEnabled() {
            // The drawer is enabled for one pane mode
            return true;
        }


    }
