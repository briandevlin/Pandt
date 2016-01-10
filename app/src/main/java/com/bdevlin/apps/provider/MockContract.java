package com.bdevlin.apps.provider;

import android.content.ContentResolver;
import android.net.Uri;

import android.provider.BaseColumns;

/**
 * Created by brian on 8/24/2014.
 */
public final class MockContract {

    private static final String TAG = MockContract.class.getSimpleName();

    // private constructor i.e this class cannot be instantiated
    private MockContract() {
    }

    public static final int VERSION = 1;

    // this is what goes into the manifest to identify the provider
    public static final String CONTENT_AUTHORITY = "com.bdevlin.apps.pandt.MockUiProvider";

    /**
     * A content:// style uri to the authority for the provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final Uri URI_BASE  = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(CONTENT_AUTHORITY)
            .build();



    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";


    protected interface BaseSyncColumns {

        /**
         * Generic column for use by sync adapters.
         */
        public static final String SYNC1 = "sync1";
        /**
         * Generic column for use by sync adapters.
         */
        public static final String SYNC2 = "sync2";
        /**
         * Generic column for use by sync adapters.
         */
        public static final String SYNC3 = "sync3";
        /**
         * Generic column for use by sync adapters.
         */
        public static final String SYNC4 = "sync4";
    }

    protected interface SyncColumns extends BaseSyncColumns {
        /**
         * The baseName of the account instance to which this row belongs, which when paired with
         * {@link #ACCOUNT_TYPE} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final String ACCOUNT_NAME = "account_name";

        /**
         * The type of account to which this row belongs, which when paired with
         * {@link #ACCOUNT_NAME} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final String ACCOUNT_TYPE = "account_type";

        /**
         * String that uniquely identifies this row to its source account.
         * <P>Type: TEXT</P>
         */
        public static final String SOURCE_ID = "sourceid";

        /**
         * Version number that is updated whenever this row or its related data
         * changes.
         * <P>Type: INTEGER</P>
         */
        public static final String VERSION = "version";

        /**
         * Flag indicating that {@link #VERSION} has changed, and this row needs
         * to be synchronized by its owning account.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final String DIRTY = "dirty";
    }

    protected interface FolderColumns {
        /**
         * The display baseName for the
         * <P>Type: TEXT</P>
         */
        public static final String FOLDER_NAME = "name";
        public static final String FOLDER_URI = "uri";



    }

    public static final String[] FOLDERS_PROJECTION = {
            BaseColumns._ID,
            FolderColumns.FOLDER_NAME,
            FolderColumns.FOLDER_URI

    };



    public static class Folders implements BaseColumns, FolderColumns {

        private Folders() {
        }

        public static final String TABLE_NAME = "folders";

        public static final Uri CONTENT_URI3 = BASE_CONTENT_URI.buildUpon().appendPath("folders").build();
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "folders");
        public static final Uri CONTENT_URI2 = BASE_CONTENT_URI.buildUpon().appendPath("folders").build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/folder";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/folder";
    }



    public interface AccountColumns {
        /**
         * The display baseName for the
         * <P>Type: TEXT</P>
         */
        public static final String ACCOUNT_NAME = "account_name";
        public static  final String FOLDER_LIST_URI = "folderListUri";
        public static  final String FOLDER_URI = "uri";
        public static  final String TYPE = "type";


    }


    public static final String[] ACCOUNTS_PROJECTION = {
            BaseColumns._ID,
            AccountColumns.ACCOUNT_NAME,
            AccountColumns.FOLDER_URI,
            AccountColumns.FOLDER_LIST_URI,
            AccountColumns.TYPE

    };




    public static class Accounts implements BaseColumns, AccountColumns {

        private Accounts() {
        }

        public static final String TABLE_NAME = "accounts";

        public static final Uri CONTENT_URI3 = BASE_CONTENT_URI.buildUpon().appendPath("accounts").build();
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "accounts");
        public static final Uri CONTENT_URI2 = BASE_CONTENT_URI.buildUpon().appendPath("accounts").build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/account";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/account";
    }


}
