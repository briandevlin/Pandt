package com.bdevlin.apps.pandt.Controllers;

import android.content.Context;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.ViewMode;
import com.bdevlin.apps.pandt.accounts.AccountController;
import com.bdevlin.apps.pandt.folders.FolderChangeListener;
import com.bdevlin.apps.pandt.folders.FolderController;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;

/**
 * Created by brian on 7/20/2014.
 */
public interface ControllableActivity {

    ActionBarController getActionBarController();
    //FolderController getFolderController();
    //AccountController getAccountController();
    FolderChangeListener getFolderChangeListener();
    NavigationDrawerFragment.NavigationDrawerCallbacks getNavigationDrawerCallbacks();
    MainContentFragment.MainContentCallbacks getMainContentCallbacks();
    Context getActivityContext();
    Context getApplicationContext();
    ViewMode getViewMode();

}
