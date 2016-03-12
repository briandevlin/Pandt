package com.bdevlin.apps.pandt.Controllers;

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

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.ui.activity.core.PandtActivity;
import com.bdevlin.apps.ui.fragments.PagerFragment;
import com.bdevlin.apps.ui.widgets.PageMarginDrawable;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brian on 7/26/2014.
 */
public class PagerController {

    // <editor-fold desc="Fields">

    private static final String TAG = PagerController.class.getSimpleName();
    private static final boolean DEBUG = true;

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;
    private ActivityController mActivityController;
    private PandtActivity mActivity;
    private LinePageIndicator mIndicator;
    private TabPageIndicator mIndicatorTab;
    private Map<String, Collection<String>> mTopics = new HashMap<>();
    private ArrayList<String> listArray = new ArrayList<String>();
    public interface QueryEnum {


        public int getId();
        public int getTabCount();
        public String getTopic();

        public String[] getProjection();

    }
    public static enum ExploreQueryEnum implements QueryEnum {

        SESSIONONE(0x1, "Nouns", 6, new String[]{
                "Proper", "Common", "Material", "Countable", "Uncountable", "Collective"
        }),

        SESSIONTWO(0x2, "Verbs", 4, new String[]{
            "Regular", "Irregular", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),

        SESSIONTHREE(0x3, "Adjectives", 2, new String[]{
            "Articles", "Possessives"/*, "MaterialThree", "CountableThree", "UncountableThree", "CollectiveThree"*/
        });


//        TAGS(0x2, new String[] {
//                ScheduleContract.Tags.TAG_ID,
//                ScheduleContract.Tags.TAG_NAME,
//        });


        private int id;
        private int tabCount;
        String topic;
        private String[] projection;

        ExploreQueryEnum(int id, String topic, int tabCount, String[] projection) {
            this.id = id;
            this.topic = topic;
            this.tabCount = tabCount;
            this.projection = projection;

        }

        @Override
        public int getId() {
            return id;
        }
        @Override
        public int getTabCount() {
            return tabCount;
        }
        @Override
        public String getTopic() {
            return topic;
        }
        @Override
        public String[] getProjection() {
            return projection;
        }
    }

    QueryEnum[] qe;
    // </editor-fold>

    // <editor-fold desc="Constructor">

    public PagerController(PandtActivity activity,
                                       ActivityController controller, FragmentManager fragmentManager) {
        mActivity = activity;
        mPager = (ViewPager) activity.findViewById(R.id.conversation_pane);

        mActivityController = controller;
        mFragmentManager = fragmentManager;
        setupPageMargin(activity.getActivityContext());

         qe = new QueryEnum[]{
                ExploreQueryEnum.SESSIONONE,
                ExploreQueryEnum.SESSIONTWO,
                ExploreQueryEnum.SESSIONTHREE};
    }

    // </editor-fold>

    // <editor-fold desc="setupPageMargin">
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
    // </editor-fold>

    // <editor-fold desc="show/hide">
    public void show(final int selectedCard, final int count)
    {
        if (DEBUG) Log.d(TAG, "PagerController: Show");

        mPager.setVisibility(View.VISIBLE);
        mPager.setCurrentItem(0);
        mPagerAdapter = new SlidePagerAdapter(mPager.getResources(), mFragmentManager, qe[selectedCard].getTabCount(), qe[selectedCard].getProjection(), qe[selectedCard].getTopic());
       // mPagerAdapter.setCount(qe[position].getTabCount());

        mPager.setAdapter(mPagerAdapter);

        mIndicatorTab = (TabPageIndicator)mActivity.findViewById(R.id.indicatorTab);
        mIndicatorTab.setViewPager(mPager);
        mIndicatorTab.notifyDataSetChanged();
        mIndicatorTab.setCurrentItem(0);
        mIndicatorTab.setVisibility(View.VISIBLE);





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
        mPager.setCurrentItem(0);
        mPager.setVisibility(View.GONE);
       // if (mIndicator != null) mIndicator.setVisibility(View.GONE);
        if (mIndicatorTab != null && mIndicatorTab.getVisibility() == View.VISIBLE ) {
            mPagerAdapter.setCount(0);
            mIndicatorTab.notifyDataSetChanged();
            mIndicatorTab.setVisibility(View.GONE);
        }

        mPager.setAdapter(null);


    }

    // </editor-fold>

    public void onDestroy() {
        // need to release resources before a configuration change kills the activity and controller
        cleanup();
    }

    private void cleanup() {
        if (mPagerAdapter != null) {
            mPagerAdapter = null;

        }
    }

    // <editor-fold desc="SlidePagerAdapter">

    public class SlidePagerAdapter extends FragmentStatePagerAdapter {

        private  int NUM_PAGES = 0;
        private QueryEnum[] qe;
        private String[] projection;
        private String topic;

        public SlidePagerAdapter(Resources res, FragmentManager fm, int count, String[] projection, final String topic) {
            super(fm);
            NUM_PAGES = count;
            this.projection =  projection;
            this.topic = topic;
        }

        @Override
        public Fragment getItem(int position) {

            String content = projection[position % projection.length];

            PagerFragment frag = PagerFragment.newInstance(content, topic);

            Bundle args = new Bundle();
            args.putInt(PagerFragment.ARG_INDEX, position);
            frag.setArguments(args);
            return frag;
        }
        @Override
        public CharSequence getPageTitle(int position) {
//            if (projection == null){
//                projection = qe[position].getProjection();
//            }

            return projection[position % projection.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public void setCount(final int count) {
            NUM_PAGES = count;
        }


    }
    // </editor-fold>
}
