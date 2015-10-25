package com.bdevlin.apps.pandt.Controllers;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

/**
 * Created by brian on 7/26/2014.
 */
public interface ActionBarController {

    ActionBar getSupportActionBar();

    Toolbar getSupportToolBar();
    /**
     * Registers to receive changes upon drawer closing when a changeAccount is called.
     */
    void registerDrawerClosedObserver(final DataSetObserver observer);

    /**
     * Removes a listener from receiving current account changes.
     */
    void unregisterDrawerClosedObserver(final DataSetObserver observer);



}
