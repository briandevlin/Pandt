package com.bdevlin.apps.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 3/13/2016.
 */
public class SessionDetailFragment extends Fragment {

    private static final String TAG = SessionDetailFragment.class.getSimpleName();

    public SessionDetailFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.session_detail_frag, container, false);
    }
}
