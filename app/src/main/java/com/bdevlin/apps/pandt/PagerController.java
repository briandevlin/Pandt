package com.bdevlin.apps.pandt;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by brian on 7/26/2014.
 */
public class PagerController {

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;
    private ActivityController mActivityController;

    public PagerController(HomeActivity activity,
                                       ActivityController controller, FragmentManager fragmentManager) {
        mPager = (ViewPager) activity.findViewById(R.id.conversation_pane);
        mActivityController = controller;
        mFragmentManager = fragmentManager;
    }


    public void show(final int position, Items.ListItem listItem)
    {
        mPager.setVisibility(View.VISIBLE);

        mPagerAdapter = new SlidePagerAdapter(mPager.getResources(), mFragmentManager);

        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(position);

    }

    public void hide(boolean changeVisibility) {

        mPager.setAdapter(null);

    }

    public void onDestroy() {
        // need to release resources before a configuration change kills the activity and controller
        cleanup();
    }

    private void cleanup() {
        if (mPagerAdapter != null) {
            // stop observing the conversation list
          //  mPagerAdapter.setActivityController(null);
          //  mPagerAdapter.setPager(null);
            mPagerAdapter = null;
        }
    }
}
