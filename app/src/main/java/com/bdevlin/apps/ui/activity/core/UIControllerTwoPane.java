package com.bdevlin.apps.ui.activity.core;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.bdevlin.apps.pandt.DrawerItem.NavigationDrawerItem;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.ViewMode;

/**
 * Created by brian on 7/20/2014.
 */
public class UIControllerTwoPane extends UIControllerBase {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerTwoPane.class.getSimpleName();
    private boolean mConversationListVisible = false;
    // </editor-fold>

    public UIControllerTwoPane(HomeActivity activity, ViewMode viewMode) {
        super(activity, viewMode);
    }

    @Override
    public boolean onCreate(Bundle savedInstanceState) {

        mActivity.setContentView(R.layout.activity_twopane);


        return super.onCreate(savedInstanceState);
    }


//    @Override
//    protected boolean isConversationListVisible() {
//        return mConversationListVisible;
//    }

    @Override
    public boolean isDrawerEnabled() {
        // The drawer is disabled for two pane mode
        return false;
    }

    @Override
    protected boolean handleBackPress(boolean isSystemBackKey) {

        return false;
    }

    @Override
    protected boolean handleUpPress() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void onNavigationDrawerItemSelected(int position ,  NavigationDrawerItem itemView) {
        // update the main name by replacing fragments
        // fix this so it does nothing if the position is the same
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    @Override
    public void onNavigationDrawerArraySelected(int position, NavigationDrawerItem itemView) {

    }


    @Override
    public void onMainContentItemSelected(int position) {

    }
}
