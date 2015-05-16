package com.bdevlin.apps.pandt.accounts;

import android.database.DataSetObserver;

public abstract class AccountObserver extends DataSetObserver {
    /**
     * The AccountController that the observer is registered with.
     */
    private AccountController mController;



    /**
     * The no-argument constructor leaves the object unusable till
     * {@link #initialize(AccountController)} is called.
     */
    public AccountObserver () {
    }

    /**
     * Initializes an {@link AccountObserver} object that receives a call to
     * {@link #onChanged(com.bdevlin.apps.pandt.accounts.Account)} when the controller changes the account.
     *
     * @param controller
     */
    public Account initialize(AccountController controller) {
        if (controller == null) {

        }
        mController = controller;
        mController.registerAccountObserver(this);
        return mController.getAccount();
    }

    @Override
    public final void onChanged() {
        if (mController == null) {
            return;
        }
        onChanged(mController.getAccount());
    }

    /**
     * Callback invoked when the account object is changed.  Since {@link Account} objects are
     * immutable, updates can be received on changes to individual settings (sync on/off)
     * in addition to changes of accounts: alice@example.com -> bob@example.com.
     * The updated account is passed as the argument.
     * @param newAccount
     */
    public abstract void onChanged(Account newAccount);

    /**
     * Return the most current account.
     * @return
     */
    public final Account getAccount() {
        if (mController == null) {
            return null;
        }
        return mController.getAccount();
    }

    /**
     * Unregisters for account changes and makes the object unusable.
     */
    public void unregisterAndDestroy() {
        if (mController == null) {
            return;
        }
        mController.unregisterAccountObserver(this);
    }
}