package com.bdevlin.apps.ui.activity.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bdevlin.apps.pandt.accounts.Account;
import com.bdevlin.apps.ui.activity.core.HomeActivity;

/**
 * Created by brian on 9/9/2014.
 */
public class MailActivityEmail extends HomeActivity {

    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_ACCOUNT_URI = "accountUri";
    public static final String EXTRA_FOLDER_URI = "folderUri";
    public static final String EXTRA_FOLDER = "folder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent intent = getIntent();
        final Uri data = intent != null ? intent.getData() : null;
        Intent ViewIntent = createViewFolderIntent(this,null, null);
       // setIntent(ViewIntent);
        super.onCreate(savedInstanceState);
    }

    public static Intent createViewFolderIntent(final Context context, final Uri folderUri,
                                                Account account) {
        if (folderUri == null || account == null) {

          //  return null;
        }
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        //intent.setDataAndType(appendVersionQueryParameter(context, folderUri), account.mimeType);
       // intent.putExtra(EXTRA_ACCOUNT, account.serialize());
       // intent.putExtra(EXTRA_FOLDER_URI, folderUri);
        return intent;
    }
}
