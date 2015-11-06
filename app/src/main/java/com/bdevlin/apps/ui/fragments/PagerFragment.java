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
import com.bdevlin.apps.pandt.ViewMode;

/**
 * Created by brian on 7/26/2014.
 */

public class PagerFragment extends Fragment {

    private static final String TAG = PagerFragment.class.getSimpleName();
    public static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";

    private ControllableActivity mActivity;
    private ActionBarController controller = null;
    private  int  mParam1;

    public static PagerFragment newInstance(String content) {
        PagerFragment fragment = new PagerFragment();


        return fragment;
    }

    public PagerFragment() {
    }

    /**
     * Can be overridden in case a subclass needs to get additional arguments.
     */
    protected void parseArguments() {
        final Bundle args = getArguments();
        if (args != null) {
            mParam1 = args.getInt(ARG_INDEX);
        }
    }


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
                R.layout.fragment, container, false);

        TextView text1 = (TextView)rootView.findViewById(R.id.text1);
        text1.setText("fragment " + mParam1);
        Log.d(TAG, "Pager Fragment: onCreateView " + mParam1);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Pager Fragment: onViewCreated " );
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
        mode.enterConversationMode();

        controller = mActivity.getActionBarController();
        ActionBar ab = controller.getSupportActionBar();
        Toolbar toolbar = controller.getSupportToolBar();
        if (toolbar != null) {
            toolbar.setSubtitle("ViewPager");
        }
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);


        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


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
