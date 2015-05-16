package com.bdevlin.apps.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdevlin.apps.authenticator.SetupData;
import com.bdevlin.apps.pandt.GenericListContext;
import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 10/20/2014.
 */
public class AccountSetupOutgoingFragment  extends AccountServerBaseFragment {

    // Public no-args constructor needed for fragment re-instantiation
    public AccountSetupOutgoingFragment() {}


    public static AccountSetupOutgoingFragment newInstance(GenericListContext viewContext,int sectionNumber) {
        AccountSetupOutgoingFragment fragment = new AccountSetupOutgoingFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment2, container, false);


        // not setting the mCursorAdapter here waiting til we create the actionBarController
        return rootView;
    }

    @Override
    public void onNext() {
        mCallback.onProceedNext(SetupData.CHECK_OUTGOING, this);
    }

    @Override
    public void onAutoDiscoverComplete(int result, SetupData setupData) {

    }

    /**
     * Activity provides callbacks here.  This also triggers loading and setting up the UX
     */
    @Override
    public void setCallback(Callback callback) {
        super.setCallback(callback);

    }
}
