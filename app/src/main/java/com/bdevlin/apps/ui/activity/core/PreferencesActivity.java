package com.bdevlin.apps.ui.activity.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
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
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.annotation.XmlRes;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;

import java.util.List;

/**
 * Created by bdevlin on 8/23/2015.
 */
public class PreferencesActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener, OnPreferenceChangeListener
{
    private static final String TAG = PreferencesActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static final String KEY_MODE_CHECKBOX_PREFERENCE = "clear_chosenaccount";
    Toolbar mToolbar;
    private SharedPreferences prefs;
    private CheckBoxPreference mCheckBoxPreference;

    private Runnable mForceCheckBoxRunnable = new Runnable() {
        public void run() {
            if (mCheckBoxPreference != null) {
                mCheckBoxPreference
                        .setChecked(!mCheckBoxPreference.isChecked());
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this must be set before  super.onCreate(savedInstanceState) android.R.style.Theme_DeviceDefault_Light_NoActionBar
      //  setTheme(android.R.style.Theme_DeviceDefault);

        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
       // setContentView(R.layout.settings);

        // Load the XML preferences file
        addPreferencesFromResource(R.xml.fragmented_preferences);

        // Get a reference to the mode checkbox preference
        mCheckBoxPreference = (CheckBoxPreference) getPreferenceScreen()
                .findPreference(KEY_MODE_CHECKBOX_PREFERENCE);

        mForceCheckBoxRunnable.run();

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

        android.support.design.widget.AppBarLayout barcontainer;
        android.support.design.widget.CoordinatorLayout coordinator;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView list = (ListView) root.findViewById(android.R.id.list);
           // get he lists parent which is the headers linearlayout
            LinearLayout headers = (LinearLayout) list.getParent();
            //((ViewGroup)headers.getParent()).removeView(headers);
            ((ViewGroup)headers.getParent()).removeViewAt(0);
           // headers.removeViewAt(0); //removes the listView. Not what we want.
           // headers.removeViewAt(1);

            // this layout has no parent
//            coordinator = (android.support.design.widget.CoordinatorLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, null, false);
//            bar = (Toolbar) coordinator.findViewById(R.id.toolbar);
//            ViewParent v = bar.getParent();
//            root.removeAllViews();

            android.support.design.widget.AppBarLayout appBarLayout = (android.support.design.widget.AppBarLayout)LayoutInflater.from(this).inflate(R.layout.prefs_bar, null, false);
            mToolbar = (Toolbar) appBarLayout.findViewById(R.id.prefs_toolbar);
            //ImageView img = (ImageView) appBarLayout.findViewById(R.id.tower);
            //img.setMaxHeight(200);
            root.addView(headers, 0);
            headers.addView(appBarLayout,0);
           // root.addView(coordinator, 0); // insert at top
            Log.d(TAG, "something");
        } else {

            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

           // barcontainer = (android.support.design.widget.AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            coordinator = (android.support.design.widget.CoordinatorLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            mToolbar =  (Toolbar) coordinator.findViewById(R.id.toolbar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = mToolbar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(coordinator);
        }

        mToolbar.setClickable(true);
        mToolbar.setTitle("Settings");
        mToolbar.setNavigationIcon(R.drawable.ic_ab_up_ltr);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();

        // Start the force toggle.
        // mForceCheckBoxRunnable.run();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(this, "onSharedPreferenceChanged", Toast.LENGTH_SHORT)
                .show();
    }

}
