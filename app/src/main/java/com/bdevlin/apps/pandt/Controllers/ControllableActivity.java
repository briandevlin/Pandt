package com.bdevlin.apps.pandt.Controllers;

import android.content.Context;
import android.content.Intent;

import com.bdevlin.apps.utils.ViewMode;
//import com.bdevlin.apps.pandt.folders.FolderChangeListener;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;

/**
 * Created by brian on 7/20/2014.
 */
public interface ControllableActivity {

    ActionBarController getActionBarController();
    NavigationDrawerFragment.NavigationDrawerCallbacks getNavigationDrawerCallbacks();
    MainContentFragment.MainContentCallbacks getMainContentCallbacks();
    Context getActivityContext();
    Context getApplicationContext();
    ViewMode getViewMode();
    void createBackStack(Intent intent);

}
