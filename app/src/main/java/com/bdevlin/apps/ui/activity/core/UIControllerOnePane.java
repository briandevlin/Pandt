package com.bdevlin.apps.ui.activity.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.ui.fragments.BlankFragment;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;


/**
 * Created by brian on 7/20/2014.
 */
public class UIControllerOnePane extends UIControllerBase
        {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerOnePane.class.getSimpleName();
    private static final boolean DEBUG = true;
    private ViewPager mViewPager;



   // </editor-fold>

   // <editor-fold desc="life cycle methods">
    public UIControllerOnePane(HomeActivity activity, ViewMode viewMode) {
        super(activity, viewMode);
    }// </editor-fold>


    // <editor-fold desc="life cycle methods">
    @Override
    public boolean onCreate(Bundle savedInstanceState) {

        // gets the one pane activity layout
        mActivity.setContentView(R.layout.activity_home);

        View v = (FrameLayout)mActivity.findViewById(R.id.main_content);

        final MainContentFragment itemListFragment = MainContentFragment.newInstance( GenericListContext.forFolder(null),0);

        replaceFragment(itemListFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                TAG_MAIN_LIST, R.id.main_content);

//        Toolbar toolbar = getActionBarToolbar();
//        toolbar.setSubtitle("This is the main screen");

        if (isDrawerEnabled()) {
            SetupDrawerLayout();
        }

        // The parent class sets the correct viewmode and starts the application off.
        // also instantiates the viewpager controller
        return super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
       // GenericListContext viewContext =  GenericListContext.forFolder(null);
        //showConversationList(/*viewContext*/);
        super.onStart();

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
    // </editor-fold>


 @Override
 public void showConversationList(GenericListContext listContext) {
        super.showConversationList(listContext);
     if (DEBUG) Log.d(TAG, "showConversationList");
        mViewMode.enterConversationListMode();

        final MainContentFragment itemListFragment = MainContentFragment.newInstance(listContext, 0);

        replaceFragment(itemListFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                TAG_MAIN_LIST, R.id.main_content);

     mActivity.getSupportFragmentManager().executePendingTransactions();

    }


    // when the main content fragment list item is selected we end up here
    @Override
    protected void showConversation(final int position, Items.ListItem listItem) {
        super.showConversation(position, listItem);
        if (DEBUG) Log.d(TAG, "showConversation");
        mViewMode.enterConversationMode();


        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
       // remove main content fragment to reveal the viewpager
        //final Fragment f = fm.findFragmentById(R.id.main_content);
        final Fragment f = fm.findFragmentByTag(TAG_MAIN_LIST);
        if (f != null && f.isAdded()) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.remove(f);
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }


        mPagerController.show(position, listItem);
    }


    /* implements NavigationDrawerFragment.NavigationDrawerCallbacks*/
    @Override
    public void onNavigationDrawerItemSelected(int position, NavigationDrawerItem itemView) {
        if (DEBUG) Log.d(TAG, "onNavigationDrawerItemSelected");
        toggleDrawerState();
        Folder folder = new Folder(itemView.id, itemView.name);

        GenericListContext viewContext =  GenericListContext.forFolder(folder);
        showConversationList(viewContext);

        mFolder = folder;
    }

    public void onNavigationDrawerArraySelected(int position, NavigationDrawerItem itemView) {
        if (DEBUG)  Log.d(TAG, "onNavigationDrawerArraySelected");
       // toggleDrawerState();
        closeDrawerIfOpen();

        String id = itemView.name;
        switch (id) {
//            case R.id.menu_about:
//                HelpUtils.showAbout(this);
//                return true;

            case "Settings":
                // Toast.makeText(mActivity, "Example action.", Toast.LENGTH_SHORT).show();
                Intent intentPrefs = new Intent(mActivity,
                        PreferencesActivity.class);
                mActivity.startActivityForResult(intentPrefs,1);

            case  "help":
               break;

            default:

        }
    }

    @Override
    public final void onMainContentItemSelected(final int position, Items.ListItem listItem) {
        if (DEBUG)  Log.d(TAG,"onMainContentItemSelected");
         showConversation(position, listItem);


    }

    /**
     * Replace the content_pane with the fragment specified here. The tag is specified so that
     * the {@link ActivityController} can look up the fragments through the
     * {@link android.app.FragmentManager}.
     * @param fragment the new fragment to put
     * @param transition the transition to show
     * @param tag a tag for the fragment manager.
     * @param anchor ID of view to replace fragment in
     * @return transaction ID returned when the transition is committed.
     */
    private int replaceFragment(Fragment fragment, int transition, String tag, int anchor) {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(transition);
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

    @Override
    public void onViewModeChanged(int newMode) {
        super.onViewModeChanged(newMode);

        if (ViewMode.isListMode(newMode)) {
            mPagerController.hide(true);
            getDrawerToggle().setDrawerIndicatorEnabled(true);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowHomeEnabled(true);
            getDrawerToggle().syncState();
            closeDrawerIfOpen();
        }

        if (ViewMode.isConversationMode(newMode)) {
            getDrawerToggle().setDrawerIndicatorEnabled(false);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(false);
            getDrawerToggle().syncState();
            closeDrawerIfOpen();
        }

    }

    @Override
    public boolean isDrawerEnabled() {
        // The drawer is enabled for one pane mode
        return true;
    }

    @Override
    protected boolean handleBackPress(boolean isSystemBackKey) {
        final int mode = mViewMode.getMode();

        if (mode == ViewMode.SEARCH_RESULTS_LIST) {
            mActivity.finish();
        } else if (mViewMode.isListMode() ) {
            if (DEBUG)  Log.d(TAG, "isListMode");
            // navigateUpFolderHierarchy();
            mActivity.finish();
        } else if (mViewMode.isConversationMode() ) {
            if (DEBUG)  Log.d(TAG, "isConversationMode");
           // transitionBackToConversationListMode();
            GenericListContext viewContext =  GenericListContext.forFolder(mFolder);
            showConversationList(viewContext);

            mViewMode.enterConversationListMode();
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
        } else if (mode == ViewMode.CONVERSATION_LIST) {
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
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


}
