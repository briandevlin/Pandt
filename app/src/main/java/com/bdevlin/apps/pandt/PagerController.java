package com.bdevlin.apps.pandt;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.ui.activity.core.HomeActivity;

/**
 * Created by brian on 7/26/2014.
 */
public class PagerController {

    private static final String TAG = PagerController.class.getSimpleName();
    private static final boolean DEBUG = true;
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
        int visible = mPager.getVisibility();
        mPager.setVisibility(View.VISIBLE);
         visible = mPager.getVisibility();

        mPagerAdapter = new SlidePagerAdapter(mPager.getResources(), mFragmentManager);

        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(position);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (DEBUG) Log.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                if (DEBUG) Log.d(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (DEBUG) Log.d(TAG, "onPageScrollStateChanged");
            }
        });

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
