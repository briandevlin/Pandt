package com.bdevlin.apps.pandt.folders;

import android.database.DataSetObserver;

import com.bdevlin.apps.pandt.folders.Folder;

/**
 * Created by brian on 7/21/2014.
 */
public interface FolderController {

   // void closeDrawer(Folder folder);
  //  void onSectionAttached(int number);
    Folder getFolder();

    void registerFolderObserver(DataSetObserver observer);
    void unregisterFolderObserver(DataSetObserver observer);
}
