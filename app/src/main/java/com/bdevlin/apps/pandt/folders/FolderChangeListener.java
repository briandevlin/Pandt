package com.bdevlin.apps.pandt.folders;

/**
 * Created by brian on 8/31/2014.
 */

public interface FolderChangeListener {
    /**
     * Handles selecting a folder from within the {@link com.bdevlin.apps.ui.fragments.NavigationDrawerFragment}.
     *
     * @param folder the selected folder
     * @param force <code>true</code> to force a folder change, <code>false</code> to disallow
     *          changing to the current folder
     */
    void onFolderChanged(Folder folder, boolean force);
}
