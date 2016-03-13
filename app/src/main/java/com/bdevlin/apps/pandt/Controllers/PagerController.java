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
import java.util.EnumMap;
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
    //private Map<String, Collection<String>> mTopics = new HashMap<>();
    private Map<String, ArrayList> mEnums = new HashMap<>();
    private ArrayList<QueryEnum> posArray = new ArrayList<QueryEnum>();
    private ArrayList<QueryEnum> pracArray = new ArrayList<QueryEnum>();
    private ArrayList<QueryEnum> elemArray = new ArrayList<QueryEnum>();
    private ArrayList<QueryEnum> intermArray = new ArrayList<QueryEnum>();
    private ArrayList<QueryEnum> advanArray = new ArrayList<QueryEnum>();
    private ArrayList<QueryEnum> quizArray = new ArrayList<QueryEnum>();
    EnumMap<Grammar, ArrayList<QueryEnum>> subjectsMap = new EnumMap<Grammar, ArrayList<QueryEnum>>(Grammar.class);

    enum Grammar {
        PartsOfSpeech, Practice, Elementary, Intermediate, Advanced, Quiz
    }

    public interface QueryEnum {


        public int getId();

        public int getTabCount();

        public String getTopic();

        public String[] getProjection();

    }

    public static enum PartsOfSpeechEnum implements QueryEnum {

        SESSIONONE(0x1, "Nouns", 6, new String[]{
                "Proper", "Common", "Material", "Countable", "Uncountable", "Collective"
        }),

        SESSIONTWO(0x2, "Verbs", 4, new String[]{
                "Regular", "Irregular", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONTHREE(0x3, "Adjectives", 4, new String[]{
                "Articles", "Possessives", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONFOUR(0x4, "Pronouns", 4, new String[]{
                "Articles", "Possessives", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONFIVE(0x5, "Adverbs", 4, new String[]{
                "Articles", "Possessives", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONSIX(0x6, "Prepositions", 4, new String[]{
                "Articles", "Possessives", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONSEVEN(0x7, "Conjunctions", 4, new String[]{
                "Articles", "Possessives", "Modal", "Auxillary"/*, "UncountableTwo", "CollectiveTwo"*/
        }),
        SESSIONEIGHT(0x8, "Interjections", 2, new String[]{
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
        QueryEnum[] qe;

        PartsOfSpeechEnum(int id, String topic, int tabCount, String[] projection) {
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

    public static enum PracticeEnum implements QueryEnum {

        SESSIONONE(0x1, "PracticeOne", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTWO(0x1, "PracticeTwo", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTHREE(0x2, "PracticeThree", 2, new String[]{
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
        QueryEnum[] qe;

        PracticeEnum(int id, String topic, int tabCount, String[] projection) {
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

    public static enum ElementaryEnum implements QueryEnum {

        SESSIONONE(0x1, "ElementaryOne", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTWO(0x1, "ElementaryTwo", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTHREE(0x2, "ElementaryThree", 2, new String[]{
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
        QueryEnum[] qe;

        ElementaryEnum(int id, String topic, int tabCount, String[] projection) {
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

    public static enum IntermediateEnum implements QueryEnum {

        SESSIONONE(0x1, "IntermediateOne", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTWO(0x1, "IntermediateTwo", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTHREE(0x2, "IntermediateThree", 2, new String[]{
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
        QueryEnum[] qe;

        IntermediateEnum(int id, String topic, int tabCount, String[] projection) {
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

    public static enum AdvancedEnum implements QueryEnum {

        SESSIONONE(0x1, "AdvancedOne", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTWO(0x1, "AdvancedTwo", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTHREE(0x2, "AdvancedThree", 2, new String[]{
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
        QueryEnum[] qe;

        AdvancedEnum(int id, String topic, int tabCount, String[] projection) {
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

    public static enum QuizEnum implements QueryEnum {

        SESSIONONE(0x1, "QuizOne", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTWO(0x1, "QuizTwo", 2, new String[]{
                "Proper", "Common", /*"Material", "Countable", "Uncountable", "Collective"*/
        }),
        SESSIONTHREE(0x2, "QuizThree", 2, new String[]{
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
        QueryEnum[] qe;

        QuizEnum(int id, String topic, int tabCount, String[] projection) {
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

    // </editor-fold>

    // <editor-fold desc="Constructor">

    public PagerController(PandtActivity activity,
                           ActivityController controller, FragmentManager fragmentManager) {
        mActivity = activity;
        mPager = (ViewPager) activity.findViewById(R.id.conversation_pane);

        mActivityController = controller;
        mFragmentManager = fragmentManager;
        setupPageMargin(activity.getActivityContext());
        initializeEnums();
    }

    // </editor-fold>

    // <editor-fold desc="Constructor">
    private void initializeEnums() {
        posArray.add(PartsOfSpeechEnum.SESSIONONE);
        posArray.add(PartsOfSpeechEnum.SESSIONTWO);
        posArray.add(PartsOfSpeechEnum.SESSIONTHREE);
        posArray.add(PartsOfSpeechEnum.SESSIONFOUR);
        posArray.add(PartsOfSpeechEnum.SESSIONFIVE);
        posArray.add(PartsOfSpeechEnum.SESSIONSIX);
        posArray.add(PartsOfSpeechEnum.SESSIONSEVEN);
        posArray.add(PartsOfSpeechEnum.SESSIONEIGHT);

        pracArray.add(PracticeEnum.SESSIONONE);
        pracArray.add(PracticeEnum.SESSIONTWO);
        pracArray.add(PracticeEnum.SESSIONTHREE);

        elemArray.add(ElementaryEnum.SESSIONONE);
        elemArray.add(ElementaryEnum.SESSIONTWO);
        elemArray.add(ElementaryEnum.SESSIONTHREE);

        intermArray.add(IntermediateEnum.SESSIONONE);
        intermArray.add(IntermediateEnum.SESSIONTWO);
        intermArray.add(IntermediateEnum.SESSIONTHREE);

        advanArray.add(AdvancedEnum.SESSIONONE);
        advanArray.add(AdvancedEnum.SESSIONTWO);
        advanArray.add(AdvancedEnum.SESSIONTHREE);

        quizArray.add(QuizEnum.SESSIONONE);
        quizArray.add(QuizEnum.SESSIONTWO);
        quizArray.add(QuizEnum.SESSIONTHREE);

        subjectsMap.put(Grammar.PartsOfSpeech, posArray);
        subjectsMap.put(Grammar.Practice, pracArray);
        subjectsMap.put(Grammar.Elementary, elemArray);
        subjectsMap.put(Grammar.Intermediate, intermArray);
        subjectsMap.put(Grammar.Advanced, advanArray);
        subjectsMap.put(Grammar.Quiz, quizArray);
    }
    // </editor-fold>

    // <editor-fold desc="setupPageMargin">
    private void setupPageMargin(Context c) {
        final TypedArray a = c.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
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
    public void show(final int selectedCard, final int count, int folderId) {
        if (DEBUG) Log.d(TAG, "PagerController: Show");

        Grammar selected = Grammar.PartsOfSpeech;
        folderId = folderId - 1;
        for (Grammar item : Grammar.values()
                ) {
            if (item.ordinal() == folderId) {
                selected = item;
                break;
            }
        }
        ArrayList list = subjectsMap.get(selected);
        QueryEnum e = (QueryEnum) list.get(selectedCard);
        mPager.setVisibility(View.VISIBLE);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(1);
        mPagerAdapter = new SlidePagerAdapter(mPager.getResources(), mFragmentManager, e.getTabCount(), e.getProjection(), e.getTopic());
        // mPagerAdapter.setCount(e.getTabCount());

        mPager.setAdapter(mPagerAdapter);

        mIndicatorTab = (TabPageIndicator) mActivity.findViewById(R.id.indicatorTab);
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
        if (mIndicatorTab != null && mIndicatorTab.getVisibility() == View.VISIBLE) {
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

        private int NUM_PAGES = 0;
        private QueryEnum[] qe;
        private String[] projection;
        private String topic;

        public SlidePagerAdapter(Resources res, FragmentManager fm, int count, String[] projection, final String topic) {
            super(fm);
            NUM_PAGES = count;
            this.projection = projection;
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
//                projection = posEnum[position].getProjection();
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
