package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.ViewMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by brian on 7/26/2014.
 */

public class PagerFragment extends Fragment {

    // <editor-fold desc="Fields">
    private static final String TAG = PagerFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";

    private ControllableActivity mActivity;
    private ActionBarController controller = null;
    private  int  mParam1;
    // </editor-fold>

    // <editor-fold desc="New Instance">
    public static PagerFragment newInstance(String content) {
        PagerFragment fragment = new PagerFragment();

        return fragment;
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public PagerFragment() {
    }

    protected void parseArguments() {
        final Bundle args = getArguments();
        if (args != null) {
            mParam1 = args.getInt(ARG_INDEX);
        }
    }
// </editor-fold>

    // <editor-fold desc="Create">

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Pager Fragment: onCreate");
        parseArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_view, container, false);

        TextView name1 = (TextView)rootView.findViewById(R.id.info_text);
        TextView name2 = (TextView)rootView.findViewById(R.id.textView2);

        name1.setText("pager fagment: " + mParam1);
        name2.setText("This fragment uses fragment_slide_view.xml which contains layouts for NestedScrollView -> CardView -> RelativeLayout ->TextView(s) ");
        //ScrollView scrolled = (ScrollView)rootView.findViewById(R.baseId.scrollView);
       // scrolled.addView(name1);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Pager Fragment: onActivityCreated ");

       final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        mActivity = (ControllableActivity) activity;

        ViewMode mode = mActivity.getViewMode();
        mode.enterMainContentItemPagerMode();

//        controller = mActivity.getActionBarController();
//        ActionBar ab = controller.getSupportActionBar();
//        Toolbar toolbar = controller.getSupportToolBar();
//        if (toolbar != null) {
//            toolbar.setSubtitle("ViewPager " + mParam1);
//        }
//
//        if (ab != null) {
//            ab.setDisplayHomeAsUpEnabled(true);
//            ab.setDisplayShowHomeEnabled(false);
//        }

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
// </editor-fold>

    // <editor-fold desc="Option menus">

    // remember these methods don't get called if the activity handles it and then return true
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

             switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }



    @Override
    public void  onPrepareOptionsMenu(Menu menu) {

    }


    // </editor-fold>

}
