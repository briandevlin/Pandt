package com.bdevlin.apps.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.utils.GenericListContext;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/18/2014.
 */
public class AccountSetupIncomingFragment extends AccountServerBaseFragment {

    private static final String TAG = AccountSetupIncomingFragment.class.getSimpleName();

    // Public no-args constructor needed for fragment re-instantiation
    public AccountSetupIncomingFragment() {}

    public static AccountSetupIncomingFragment newInstance(GenericListContext viewContext,int sectionNumber) {
        AccountSetupIncomingFragment fragment = new AccountSetupIncomingFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment, container, false);


        // not setting the mCursorAdapter here waiting til we create the actionBarController
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onNext() {
        mCallback.onProceedNext(SetupData.CHECK_INCOMING, this);
    }

    /**
     * Activity provides callbacks here.  This also triggers loading and setting up the UX
     */
    @Override
    public void setCallback(Callback callback) {
        super.setCallback(callback);

    }

    @Override
    public void onAutoDiscoverComplete(int result, SetupData setupData) {

    }
}
