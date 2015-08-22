package com.bdevlin.apps.ui.activity.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.bdevlin.apps.pandt.accounts.Account;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.HomeActivity;
import com.bdevlin.apps.pandt.Items;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;


/**
 * Created by brian on 7/20/2014.
 */
public class UIControllerOnePane extends UIControllerBase
       /* implements NavigationDrawerFragment.NavigationDrawerCallbacks*/ {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerOnePane.class.getSimpleName();

   // private NavigationDrawerFragment mNavigationDrawerFragment;
   // private CharSequence mDrawerTitle;
    private CharSequence mTitle;
//    private static final String OPENED_KEY = "OPENED_KEY";
   // private SharedPreferences prefs = null;
    private Boolean opened = null;

    private boolean mConversationListVisible = false;

   // </editor-fold>

    public UIControllerOnePane(HomeActivity activity, ViewMode viewMode) {
        super(activity, viewMode);
    }

    // <editor-fold desc="life cycle methods">
    @Override
    public boolean onCreate(Bundle savedInstanceState) {
        // gets the one pane activity layout
        mActivity.setContentView(R.layout.activity_home);

   //   Cursor c =  mActivity.getContentResolver().query(MockContract.Accounts.CONTENT_URI, new String[] {MockContract.AccountColumns.ACCOUNT_NAME}, null,null,null);
//String foldename = c.getString(1);

        if (isDrawerEnabled()) {
            SetupDrawerLayout();
        }
        // The parent class sets the correct viewmode and starts the application off.
        return super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
       // GenericListContext viewContext =  GenericListContext.forFolder(null);
       // showConversationList(viewContext);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    protected boolean isConversationListVisible() {
        return mConversationListVisible;
    }

    // </editor-fold>

    // when the main content fragment list item is selected we end up here
    @Override
    protected void showConversation(final int position, Items.ListItem listItem) {
        super.showConversation(position, listItem);

        mViewMode.enterConversationMode();

        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
       // remove main content fragment to reveal the viewpager
        final Fragment f = fm.findFragmentById(R.id.main_content);
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
    public void onNavigationDrawerItemSelected(int position, Folder folder) {

        // FIXME
        GenericListContext viewContext =  GenericListContext.forFolder(folder);

        // update the main name (R.id.container) by replacing fragments
        Fragment mainFragment = MainContentFragment.newInstance(viewContext, position + 1);
       // Fragment mainFragment = AccountSetupIncomingFragment.newInstance(viewContext, position + 1);

        replaceFragment(mainFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, TAG_MAIN_LIST,
                R.id.main_content);

        mFolder = folder;
    }

    /**
     * Replace the content_pane with the fragment specified here. The tag is specified so that
     * the {@link com.bdevlin.apps.pandt.ActivityController} can look up the fragments through the
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
       // fm.executePendingTransactions(); // had to remove this as it crashes because of the layout fragment in the authenticator
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


        // When entering conversation list mode, hide and clean up any currently visible
        // conversation.
        if (ViewMode.isListMode(newMode)) {
            mPagerController.hide(true /* changeVisibility */);
        }
        // When we step away from the conversation mode, we don't have a current conversation
        // anymore. Let's blank it out so clients calling getCurrentConversation are not misled.
        if (!ViewMode.isConversationMode(newMode)) {
          //  setCurrentConversation(null);
        }

    }

    @Override
    public boolean isDrawerEnabled() {
        // The drawer is enabled for one pane mode
        return true;
    }

    @Override
    public void changeAccount(Account account) {
        super.changeAccount(account);
     //   mConversationListNeverShown = true;
      //  closeDrawerIfOpen();
    }
    @Override
    protected boolean handleBackPress(boolean isSystemBackKey) {
        final int mode = mViewMode.getMode();

        if (mode == ViewMode.SEARCH_RESULTS_LIST) {
            mActivity.finish();
        } else if (mViewMode.isListMode() ) {
            Log.d(TAG, "isListMode");
            // navigateUpFolderHierarchy();
            mActivity.finish();
        } else if (mViewMode.isConversationMode() ) {
            Log.d(TAG, "isConversationMode");
            transitionBackToConversationListMode();
            mViewMode.enterConversationListMode();
        } else {
            Log.d(TAG, "default mode");
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
        } else if (mode == ViewMode.CONVERSATION_LIST
                || mode == ViewMode.WAITING_FOR_ACCOUNT_INITIALIZATION) {
            final boolean isTopLevel = (mFolder == null) /*|| (mFolder.parent == Uri.EMPTY)*/;

            if (isTopLevel) {
                // Show the drawer.
                toggleDrawerState();
            } else {
                Log.d(TAG, "go up");
                //navigateUpFolderHierarchy();
            }
        } else if (mode == ViewMode.CONVERSATION || mode == ViewMode.SEARCH_RESULTS_CONVERSATION
                || mode == ViewMode.AD) {
            // Same as go back.
            Log.d(TAG, "Same as go back");
            handleBackPress(false);
        }
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }



    private void transitionBackToConversationListMode() {
        final int mode = mViewMode.getMode();
//        enableCabMode();
        mConversationListVisible = true;
        if (mode == ViewMode.SEARCH_RESULTS_CONVERSATION) {
            mViewMode.enterSearchResultsListMode();
        } else {
            mViewMode.enterConversationListMode();
        }

        final Folder folder = mFolder != null ? mFolder : null;
        onFolderChanged(folder, true /* force */);

       // onConversationVisibilityChanged(false);
       // onConversationListVisibilityChanged(true);

    }



    @Override
    public void showConversationList(GenericListContext listContext) {
        super.showConversationList(listContext);
        mViewMode.enterConversationListMode();


        GenericListContext viewContext =  GenericListContext.forFolder(null);
// update the main name (R.id.container) by replacing fragments
        Fragment mainFragment = MainContentFragment.newInstance(viewContext, 0);
        // Fragment mainFragment = AccountSetupIncomingFragment.newInstance(viewContext, position + 1);

        replaceFragment(mainFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, TAG_MAIN_LIST,
                R.id.main_content);

    }
}
