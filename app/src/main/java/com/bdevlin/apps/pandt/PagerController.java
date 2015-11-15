package com.bdevlin.apps.pandt;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.ui.activity.core.HomeActivity;
import com.bdevlin.apps.ui.fragments.PagerFragment;
import com.bdevlin.apps.ui.widgets.PageMarginDrawable;
import com.viewpagerindicator.LinePageIndicator;

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
    private HomeActivity mActivity;
    private LinePageIndicator mIndicator;


    public PagerController(HomeActivity activity,
                                       ActivityController controller, FragmentManager fragmentManager) {
        mActivity = activity;
        mPager = (ViewPager) activity.findViewById(R.id.conversation_pane);

        mActivityController = controller;
        mFragmentManager = fragmentManager;
        setupPageMargin(activity.getActivityContext());
    }
    private void setupPageMargin(Context c) {
        final TypedArray a = c.obtainStyledAttributes(new int[] {android.R.attr.listDivider});
        final Drawable divider = a.getDrawable(0);
        a.recycle();
        final int padding = c.getResources().getDimensionPixelOffset(
                R.dimen.conversation_page_gutter);
        final Drawable gutterDrawable = new PageMarginDrawable(divider, padding, 0, padding, 0,
                c.getResources().getColor(R.color.conversation_view_border_color));
        mPager.setPageMargin(gutterDrawable.getIntrinsicWidth() + 2 * padding);
        mPager.setPageMarginDrawable(gutterDrawable);
    }

    public void show(final int position)
    {
        if (DEBUG) Log.d(TAG, "PagerController: Show");
        int visible = mPager.getVisibility();
        mPager.setVisibility(View.VISIBLE);
         visible = mPager.getVisibility();

        mPagerAdapter = new SlidePagerAdapter(mPager.getResources(), mFragmentManager);

        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(position);

        mIndicator = (LinePageIndicator)mActivity.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setVisibility(View.VISIBLE);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // if (DEBUG) Log.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                if (DEBUG) Log.d(TAG, "PagerController: onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               // if (DEBUG) Log.d(TAG, "onPageScrollStateChanged");
            }
        });

    }

    public void hide(boolean changeVisibility) {
        if (DEBUG) Log.d(TAG, "PagerController: Hide");

        mPager.setVisibility(View.GONE);
        if (mIndicator != null) mIndicator.setVisibility(View.GONE);
       // mPager.setAdapter(null);
    }

    public void onDestroy() {
        // need to release resources before a configuration change kills the activity and controller
        cleanup();
    }

    private void cleanup() {
        if (mPagerAdapter != null) {
            mPagerAdapter = null;
        }
    }

    public class SlidePagerAdapter extends FragmentStatePagerAdapter {

        private  final int NUM_PAGES = 3;

        //  private static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";

        public SlidePagerAdapter(Resources res, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // return new PagerFragment();

            PagerFragment frag = PagerFragment.newInstance("item1");

            Bundle args = new Bundle();
            args.putInt(PagerFragment.ARG_INDEX, position);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }
}
