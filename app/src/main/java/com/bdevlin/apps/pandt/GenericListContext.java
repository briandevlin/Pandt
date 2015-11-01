package com.bdevlin.apps.pandt;

import android.os.Bundle;

import com.bdevlin.apps.pandt.accounts.Account;
import com.bdevlin.apps.pandt.folders.Folder;
import com.bdevlin.apps.utils.Utils;

/**
 * Created by brian on 8/10/2014.
 */
public class GenericListContext {
    private static final String EXTRA_FOLDER = "com.bdevlin.apps.pandt.folder";

    public final Folder folder;

    private GenericListContext(Folder f) {

        folder = f;
    }

    public static GenericListContext forFolder(Folder folder) {

        return new GenericListContext( folder);

    }

    public static GenericListContext forBundle(Bundle bundle) {

        Folder folder = bundle.getParcelable(Utils.CONVERSATION_LIST_KEY);
        return new GenericListContext(folder);
    }

    public Bundle toBundle() {

        Bundle result = new Bundle();

         result.putParcelable(Utils.CONVERSATION_LIST_KEY,  folder);

        return result;

    }
}
