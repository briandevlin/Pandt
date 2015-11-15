package com.bdevlin.apps.ui.activity.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.ui.fragments.HelpListFragment;
import com.bdevlin.apps.utils.HelpUtils;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by brian on 11/11/2015.
 */
public class HelpActivity  extends AppCompatActivity {

    // <editor-fold desc="Fields">
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static final String QUERY_NAME = "queryName";
    private static final Random RANDOM = new Random();
    private ViewPager mPager;

    private ArrayList<Fragment> mFragments;
    private Map<ListQuery, Integer> mQueryIndex;
    private Toolbar mToolbar;

    // </editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pager);
        getActionBarToolbar();
        ActionBar ab =  getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 finish();
                }
            });
        }

        initFragments();
        TestFragmentAdapter adapter = new TestFragmentAdapter(
                getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        int position = getRequestedPosition(getIntent());
        mPager.setCurrentItem(position);
    }

    protected Toolbar getActionBarToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    private void initFragments() {
        mFragments = new ArrayList<>();

        String[] helpTitles = getResources().getStringArray(R.array.help_screens);

        int[] helpKeys = getResources().getIntArray(R.array.help_keys);
        int length = helpTitles.length;
        if (helpKeys.length != length) {
            if (DEBUG) Log.e(TAG, "Mismatch between keys length " + helpKeys.length
                    + " and titles " + length);
            length = Math.min(length, helpKeys.length);
        }

        for (int i = 0; i < length; i++) {
            HelpListFragment fragment = new HelpListFragment();

            Bundle args = new Bundle();
            int index = helpKeys[i];
            String idKey = "help" + index;
            int contentId = getResources().getIdentifier(idKey, "string", getPackageName());
            CharSequence content = getText(contentId);
            args.putCharSequence(HelpListFragment.CONTENT, content);
            args.putString(HelpListFragment.TITLE, helpTitles[index]);

            fragment.setArguments(args);
            mFragments.add(fragment);
        }

        // few magic numbers for good luck...
        mQueryIndex = new HashMap<ListQuery, Integer>();

        mQueryIndex.put(ListQuery.inbox, 1);
        mQueryIndex.put(ListQuery.dueToday, 2);
        mQueryIndex.put(ListQuery.dueNextWeek, 2);
        mQueryIndex.put(ListQuery.dueNextMonth, 2);
        mQueryIndex.put(ListQuery.nextTasks, 3);
        mQueryIndex.put(ListQuery.project, 4);
        mQueryIndex.put(ListQuery.context, 5);
        mQueryIndex.put(ListQuery.custom, 6);
        mQueryIndex.put(ListQuery.tickler, 7);
    }

    public enum ListQuery {
        all, inbox, nextTasks, dueToday, dueNextWeek, dueNextMonth, tickler, context, project, custom
    }

    private int getRequestedPosition(Intent intent) {
        int position = 0;
        String queryName = intent.getStringExtra(QUERY_NAME);
        if (queryName != null) {
            ListQuery query = ListQuery.valueOf(queryName);
            position = mQueryIndex.get(query);
            if (position == -1) {

                position = 0;
            }
        }

        return position;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, (Menu) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean handled = true;
        switch (id) {
            case R.id.action_about:
                HelpUtils.showDialog(this);

                return true;
            case R.id.action_terms:

                HelpUtils.showOpenSourceLicenses(this);
                return true;
            case R.id.action_policy:
                HelpUtils.showEula(this);

                return true;
            case R.id.action_settings:
                // Toast.makeText(mActivity, "Example action.", Toast.LENGTH_SHORT).show();
              /*  Intent intentPrefs = new Intent(mActivity,
                        PreferencesActivity.class);
                mActivity.startActivity(intentPrefs);*/
                //  HelpUtils.showDialog(mActivity);
                return true;
            case  android.R.id.home:
               // onUpPressed();
                return true;
            default:
                handled = false;
        }
        return handled;
    }

    class TestFragmentAdapter extends FragmentPagerAdapter /*implements
            IconPagerAdapter*/ {

        public TestFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        /*@Override
        public int getIconResId(int index) {
            // TODO Auto-generated method stub
            return 0;
        }*/

    }


}
