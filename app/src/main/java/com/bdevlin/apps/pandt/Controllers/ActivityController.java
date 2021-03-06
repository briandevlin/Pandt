package com.bdevlin.apps.pandt.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bdevlin.apps.utils.GenericListContext;
import com.bdevlin.apps.utils.ViewMode;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;
import com.bdevlin.apps.utils.LoginAndAuthHelper;

/**
 * Created by brian on 7/20/2014.
 */
public interface ActivityController extends ActionBarController,
//        FolderChangeListener,/*AccountController,*/
       /* FolderController,*/
        ViewMode.ModeChangeListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        MainContentFragment.MainContentCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener,
        LoginAndAuthHelper.Callbacks
{
    boolean onCreate(Bundle savedState);
    boolean onCreateOptionsMenu(Menu menu);
    boolean onOptionsItemSelected(MenuItem item);
    boolean onPrepareOptionsMenu(Menu menu);
    void onConfigurationChanged(Configuration newConfig);
    boolean isDrawerEnabled();
    void onDestroy();
    //void onNewIntent(Intent intent);
    boolean onBackPressed(boolean isSystemBackKey);
    boolean onUpPressed();

    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle inState);
   // void onPause();
    void onResume();
    void onStart();
    void onStop();
    void onPostCreate(Bundle savedInstanceState);
    //Account getCurrentAccount();
    GenericListContext getCurrentListContext();
    void showMainContentItemsList(GenericListContext listContext);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void createBackStack(Intent intent);
}
