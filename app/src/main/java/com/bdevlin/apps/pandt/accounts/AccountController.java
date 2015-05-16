package com.bdevlin.apps.pandt.accounts;

import android.database.DataSetObserver;

import com.bdevlin.apps.pandt.folders.Folder;

/**
 * Created by brian on 9/1/2014.
 */
public interface AccountController {

    void changeAccount(Account account);

    void registerAccountObserver(DataSetObserver observer);

    /**
     * Removes a listener from receiving current account changes.
     */
    void unregisterAccountObserver(DataSetObserver observer);

    /**
     * Returns the current account in use by the controller. Instead of calling this method,
     * consider registering for account changes using
     * {@link AccountObserver#initialize(AccountController)}, which not only provides the current
     * account, but also updates to the account, in case of settings changes.
     */
    Account getAccount();

    /**
     * When the {@link com.bdevlin.apps.ui.fragments.NavigationDrawerFragment} has a new account ready for changing to,
     * close the drawer and then wait for {@link android.database.DataSetObservable#notifyChanged()}.
     * @param hasNewFolderOrAccount true if we need to load conversations for a different folder
     *            or account, false otherwise.
     */
    void closeDrawer(boolean hasNewFolderOrAccount, Account nextAccount, Folder nextFolder);

}
