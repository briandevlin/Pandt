package com.bdevlin.apps.ui.activity.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
//import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
//import android.support.v7.app.
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;

import java.util.List;

/**
 * Created by bdevlin on 8/23/2015.
 */
public class PreferencesActivity extends PreferenceActivity  {
    private static final String TAG = PreferencesActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.settings);
        addPreferencesFromResource(R.xml.fragmented_preferences);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar bar;
        android.support.design.widget.AppBarLayout barcontainer;
        android.support.design.widget.CoordinatorLayout coordinator;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
           // barcontainer = (android.support.design.widget.AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            coordinator = (android.support.design.widget.CoordinatorLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            bar = (Toolbar) coordinator.findViewById(R.id.toolbar);
            Log.d(TAG, "some text");
            root.addView(coordinator, 0); // insert at top
        } else {

            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

           // barcontainer = (android.support.design.widget.AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            coordinator = (android.support.design.widget.CoordinatorLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            bar =  (Toolbar) coordinator.findViewById(R.id.toolbar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(coordinator);
        }
       // bar.setTitleTextColor(R.color.navdrawer_background);
        bar.setTitle("Preferences");

       // bar.getRootView().setBackgroundColor(234567);

        bar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    getParent().setResult(Activity.RESULT_OK, data);
                }
                finish();

            }
        });
    }
}
