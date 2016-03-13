package com.bdevlin.apps.ui.activity.core;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 3/13/2016.
 */
public class SessionDetailActivity extends AppCompatActivity {
    private static final String TAG = SessionDetailActivity.class.getSimpleName();
    private Uri mSessionUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_act);

        mSessionUri = getIntent().getData();
        if (mSessionUri == null) {
            Log.e(TAG, "SessionDetailActivity started with null session Uri!");
            finish();
            return;
        }
    }
}
