package com.bdevlin.apps.pandt;

import android.database.DataSetObserver;

import com.bdevlin.apps.pandt.Controllers.ActionBarController;


/**
 * Observes when the drawer is closed for the purpose of computing after the drawer is,
 * potentially, off-screen.
 */
public abstract class DrawerClosedObserver extends DataSetObserver {
    private ActionBarController mController;

    /**
     * The no-argument constructor leaves the object unusable till
     * {@link #initialize(ActionBarController)} is called.
     */
    public DrawerClosedObserver () {
    }

    /**
     * Initialize the {@link DrawerClosedObserver} object to receive calls when the drawer
     * is closed.
     *
     * @param controller
     */
    public void initialize(ActionBarController controller) {
        mController = controller;
       // mController.registerDrawerClosedObserver(this);
    }

    /**
     * On drawer closed, execute necessary actions. In the case of {@link com.bdevlin.apps.ui.fragments.NavigationDrawerFragment}, this
     * includes changing the accounts and then redrawing.
     */
    public abstract void onDrawerClosed();

    @Override
    public final void onChanged() {
        if (mController != null) {
            onDrawerClosed();
        }
    }

    /**
     * Unregisters the {@link DrawerClosedObserver} and makes it unusable.
     */
    public void unregisterAndDestroy() {
        if (mController != null) {
          //  mController.unregisterDrawerClosedObserver(this);
        }
    }
}