package com.bdevlin.apps.pandt.folders;

import android.database.DataSetObserver;

public abstract class FolderObserver extends DataSetObserver {
    /**
     * The FolderController that the observer is registered with.
     */
    private FolderController mController;



    /**
     * The no-argument constructor leaves the object unusable till
     * {@link #initialize(FolderController)} is called.
     */
    public FolderObserver () {
    }

    /**
     * Initializes an {@link FolderObserver} object that receives a call to
     * {@link #onChanged(com.bdevlin.apps.pandt.folders.Folder)} when the controller changes the Folder.
     *
     * @param controller
     */
    public Folder initialize(FolderController controller) {
        if (controller == null) {

        }
        mController = controller;
        mController.registerFolderObserver(this);
        return mController.getFolder();
    }

    @Override
    public final void onChanged() {
        if (mController == null) {
            return;
        }
        onChanged(mController.getFolder());
    }

    /**
     * Callback invoked when the Folder object is changed.  Since {@link Folder} objects are
     * immutable, updates can be received on changes to individual settings (sync on/off)
     * in addition to changes of Folders: alice@example.com -> bob@example.com.
     * The updated Folder is passed as the argument.
     * @param newFolder
     */
    public abstract void onChanged(Folder newFolder);

    /**
     * Return the current folder.
     * @return
     */
    public final Folder getFolder() {
        if (mController == null) {
            return null;
        }
        return mController.getFolder();
    }

    /**
     * Unregisters for Folder changes and makes the object unusable.
     */
    public void unregisterAndDestroy() {
        if (mController == null) {
            return;
        }
        mController.unregisterFolderObserver(this);
    }
}
