package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.ActionBarController;
import com.bdevlin.apps.pandt.ControllableActivity;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 7/26/2014.
 */

public class PagerFragment extends Fragment {

    public static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";

    private ControllableActivity mActivity;
    private ActionBarController controller = null;
    private  int  mParam1;

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

        parseArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_view, container, false);

        TextView text1 = (TextView)rootView.findViewById(R.id.text1);
        text1.setText("fragment " + mParam1);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


       final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        mActivity = (ControllableActivity) activity;
        controller = mActivity.getActionBarController();
        ActionBar ab = controller.getSupportActionBar();
       // ab.setDisplayHomeAsUpEnabled(true);
      //  ab.setDisplayShowHomeEnabled(true);

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
