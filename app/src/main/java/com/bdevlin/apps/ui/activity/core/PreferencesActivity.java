package com.bdevlin.apps.ui.activity.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
//import android.support.v7.internal.widget.ThemeUtils;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.utils.ThemeUtils;

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
    private static final int[] RES_IDS_ACTION_BAR_SIZE = { R.attr.actionBarSize };
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


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this must be set before  super.onCreate(savedInstanceState) android.R.style.Theme_DeviceDefault_Light_NoActionBar
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)) {
            //setTheme(R.style.Theme.PandT.Lite);
        }

        final String themePrefKey = getString(R.string.pref_theme), defaultTheme=getResources().getString(R.string.pref_theme_default);
        final String themePref = PreferenceManager.getDefaultSharedPreferences(this).getString(themePrefKey,defaultTheme);
        //setTheme(R.style.Theme.PandT.Lite);
        switch(themePref)
        {
            case "dark":
               // setTheme(R.style.Theme_PandT_Dark);
                break;
            case "light":
               // setTheme(R.style.Theme_PandT_Light);
                break;
        }
        super.onCreate(savedInstanceState);

        final Context context = this;
        final Resources  resources = getResources();
        Resources.Theme theme = getTheme();
        Resources.Theme curTheme = context.getTheme();



        int[] attrs = new int[]{
                R.styleable.MaterialDrawer_material_drawer_background//,
               // R.attr.actionBarSize
        };
        final int resId = ThemeUtils.getThemeAttrColor(context, R.attr.colorPrimaryDark);
        // same as
        TypedArray a1 = theme.obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
        int resIdColor = a1.getResourceId(0, 0);
        String colorhexStr = Integer.toHexString(resIdColor);
        int colorPrimaryDark = a1.getColor(0, 0);

//        TypedArray a2 = theme.obtainStyledAttributes( new int[]{R.attr.colorPrimaryDark});
//        int item = a2.getResourceId(0, 0);
//        String itemhexStr = Integer.toHexString(item);

        TypedArray a = context.obtainStyledAttributes(null, R.styleable.NavDrawerItemView);
        if (a.hasValue(R.styleable.NavDrawerItemView_iconTints)) {
            ColorStateList mIconTints = a1.getColorStateList(R.styleable.NavDrawerItemView_iconTints);
        }
        int rid =  a1.getResourceId(R.styleable.MaterialDrawer_material_drawer_background, 0);
        a1.recycle();

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        float size = att.getDimension(0, 0);
        att.recycle();

        final float size2 = ThemeUtils.getThemeAttrDimension(context, R.attr.actionBarSize);

        // int rcId = a1.getColor(0, 9999);

//        Drawable  mActivatedBackgroundDrawable = a1
//                .getDrawable(R.styleable.MaterialDrawer_material_drawer_background);
        //a1.recycle();
        // same as
//        TypedArray a2 =  getTheme().obtainStyledAttributes(attrs);
//        int rcId2 = a2.getColor(0, 0);
//        int rid2 =  a2.getResourceId(0,0);
//        a2.recycle();

//        TypedArray a = getTheme().obtainStyledAttributes(R.style.Theme_PandT, new int[] {R.attr.material_drawer_background});
//        TypedValue outValue = new TypedValue();
//        getTheme().resolveAttribute(R.attr.theme, outValue, true);
//        if ("dark".equals(outValue.string)) {
//
//        }

       // int attributeResourceId = a.getResourceId(0, 0);
        TypedValue outValue = new TypedValue();
        int outdata = 0;
        int colorInt = new ResourcesCompat().getColor(resources, R.color.colorPrimaryDark, theme);
        resources.getValue(R.color.colorPrimaryDark,outValue,true);
        int resourceId = outValue.resourceId;
        String hexStr = Integer.toHexString(resourceId );
        if(outValue.type>=TypedValue.TYPE_FIRST_INT&&outValue.type<=TypedValue.TYPE_LAST_INT)
            outdata =  outValue.data;
        else if(outValue.type==TypedValue.TYPE_STRING)
            resources.getColor(outValue.resourceId);
        //final TypedArray values1 = this.obtainStyledAttributes(attrs);
        //final TypedArray values = getTheme().obtainStyledAttributes(attrs);





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
//            coordinator = (android.support.design.widget.coordinatorlayout) LayoutInflater.from(this).inflate(R.layout.coordinatorlayout, null, false);
//            bar = (Toolbar) coordinator.findViewById(R.id.toolbar);
//            ViewParent v = bar.getParent();
//            root.removeAllViews();

            android.support.design.widget.AppBarLayout appBarLayout = (android.support.design.widget.AppBarLayout)LayoutInflater.from(this).inflate(R.layout.prefs_bar, null, false);
            mToolbar = (Toolbar) appBarLayout.findViewById(R.id.prefs_toolbar);
            //ImageView img = (ImageView) appBarLayout.findViewById(R.id.tower);
            //img.setMaxHeight(200);
            root.addView(headers, 0);
            headers.addView(appBarLayout,0);

            // Check if we're running on Android 5.0 or higher
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            Log.d(TAG, "something");
        } else {

            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

           // barcontainer = (android.support.design.widget.AppBarLayout) LayoutInflater.from(this).inflate(R.layout.coordinatorlayout, root, false);
           // coordinator = (android.support.design.widget.coordinatorlayout) LayoutInflater.from(this).inflate(R.layout.prefs_bar, root, false);
           // mToolbar =  (Toolbar) coordinator.findViewById(R.id.toolbar);
            android.support.design.widget.AppBarLayout appBarLayout = (android.support.design.widget.AppBarLayout)LayoutInflater.from(this).inflate(R.layout.prefs_bar, root, false);

            mToolbar = (Toolbar) appBarLayout.findViewById(R.id.prefs_toolbar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = mToolbar.getHeight();
            }

            content.setPadding(0, height, 0, 0);


            root.addView(appBarLayout,0);
            root.addView(content);
        }

        mToolbar.setClickable(true);
        mToolbar.setTitle("Settings");
        String name = ThemeUtils.getThemeName(this);
        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
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
