package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bdevlin.apps.authenticator.SetupData;

/**
 * Created by brian on 10/18/2014.
 */
public abstract class AccountServerBaseFragment extends android.support.v4.app.Fragment
        implements  AccountCheckSettingsFragment.Callbacks,  View.OnClickListener {
    protected SetupData mSetupData;
    protected Activity mContext;
    protected Callback mCallback = EmptyCallback.INSTANCE;
    /**
     * Callback interface that owning activities must provide
     */
    public interface Callback {

        public void onEnableProceedButtons(boolean enable);

        public void onProceedNext(int checkMode, AccountServerBaseFragment target);

        public void onCheckSettingsComplete(int result, SetupData setupData);
    }

    private static class EmptyCallback implements Callback {
        public static final Callback INSTANCE = new EmptyCallback();
        @Override public void onEnableProceedButtons(boolean enable) { }
        @Override public void onProceedNext(int checkMode, AccountServerBaseFragment target) { }
        @Override public void onCheckSettingsComplete(int result, SetupData setupData) { }
    }

    public AccountServerBaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        SetupData.SetupDataContainer container = (SetupData.SetupDataContainer) mContext;
        mSetupData = container.getSetupData();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        // Hide the soft keyboard if we lose focus
        final InputMethodManager imm =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onPause();
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mCallback = EmptyCallback.INSTANCE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /**
     * Activity provides callbacks here.
     */
    public void setCallback(Callback callback) {
        mCallback = (callback == null) ? EmptyCallback.INSTANCE : callback;
        mContext = getActivity();
    }


    @Override
    public void onCheckSettingsComplete(final int settingsResult, SetupData setupData) {
        mSetupData = setupData;
        mCallback.onCheckSettingsComplete(settingsResult, mSetupData);
    }

    public abstract void onNext();
}
