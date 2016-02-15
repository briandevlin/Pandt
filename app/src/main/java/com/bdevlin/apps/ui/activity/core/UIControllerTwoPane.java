package com.bdevlin.apps.ui.activity.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.bdevlin.apps.pandt.DrawerItem.IDrawerItem;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.utils.GenericListContext;
import com.bdevlin.apps.utils.ViewMode;

/**
 * Created by brian on 7/20/2014.
 */
public class UIControllerTwoPane extends UIControllerBase {

    // <editor-fold desc="Fields">
    private static final String TAG = UIControllerTwoPane.class.getSimpleName();
    private boolean mConversationListVisible = false;
    // </editor-fold>

    public UIControllerTwoPane(PandtActivity activity, ViewMode viewMode) {
        super(activity, viewMode);
    }

    @Override
    public boolean onCreate(Bundle savedInstanceState) {

        mActivity.setContentView(R.layout.activity_twopane);

        SetupDrawerLayout();
        FrameLayout frameLayout = (FrameLayout)mActivity.findViewById(R.id.main_content);
        int padding = (int)mActivity.getResources().getDimension(R.dimen.navdrawer_width);
        frameLayout.setPadding(padding,0,0,0);
        if(frameLayout.getPaddingLeft() == padding) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        mDrawerLayout.setScrimColor(mActivity.getResources().getColor(R.color.transparent));
        boolean isDrawerLocked = true;
        }
        SetUpMainFragment();

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
    public void onNavigationDrawerItemSelected(View view, int position ,  IDrawerItem itemView) {
        // update the main baseName by replacing fragments
        // fix this so it does nothing if the position is the same
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.baseId.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    @Override
    public void onNavigationDrawerArraySelected(View view, int position, IDrawerItem itemView) {

    }


    @Override
    public void onMainContentItemSelected(int position) {

    }
}
