package com.bdevlin.apps.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.ContentUris;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.bdevlin.apps.provider.PandTDatabaseHelper.Tables;



/**
 * Created by brian on 8/24/2014.
 */
public class PandTUiProvider extends ContentProvider {

    private static final String TAG = PandTUiProvider.class.getSimpleName();

    public static final boolean LOGD;

    private PandTDatabaseHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static final String AUTHORITY = PandTContract.CONTENT_AUTHORITY;

    // The starting uri used by the account loader and MyObjectCursorLoader
    private static final Uri MOCK_ACCOUNTS_URI = Uri.parse("name://" + AUTHORITY + "/accounts");

   private static Map<String, List<Map<String, Object>>> MOCK_QUERY_RESULTS;
    private static final String WHERE_ID = PandTContract.RECORD_ID + "=?";


    private static final int FOLDER = 100;
    private static final int FOLDER_ID = 101;
    private static final int ACCOUNT = 102;
    private static final int ACCOUNT_ID = 103;
    private static final int SUBJECT = 104;
    private static final int SESSION = 105;
    private static final int SESSION_ID = 106;



    static {
        LOGD = true;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = AUTHORITY;

        // All folders
        matcher.addURI(authority, Tables.FOLDERS, FOLDER);
        // A specific folder
        matcher.addURI(authority, Tables.FOLDERS + "/*", FOLDER_ID);
        // All accounts
        matcher.addURI(authority, Tables.ACCOUNTS, ACCOUNT);
        // A specific account
        matcher.addURI(authority, Tables.ACCOUNTS + "/#", ACCOUNT_ID);
        // All subjects
        matcher.addURI(authority, "subjects", SUBJECT);
        // All folders
        matcher.addURI(authority, Tables.SESSIONS, SESSION);
        // A specific folder
        matcher.addURI(authority, Tables.SESSIONS + "/*", SESSION_ID);

        return matcher;
    }


    private static PandTUiProvider sInstance;


    @Override
    public boolean onCreate() {
        sInstance = this;
       // initializeMockProvider();

        mOpenHelper = new PandTDatabaseHelper( getContext() );

        return null != mOpenHelper;
    }

    @Override
    public void shutdown() {
        sInstance = null;
    }

    // <editor-fold desc="MockData">
    private static void initializeAccount(int accountId,
                                          Map<String, List<Map<String, Object>>> resultMap) {

    //   final Map<String, Object> accountDetailsMap = createAccountDetailsMap(0, " name://com.android.mail.mockprovider/account/0/folder/0");


        final Map<String, Object> inboxfolderDetailsMap = createFolderDetailsMap(0, accountId, "zero", true, 0, 2);
        final Map<String, Object> accountDetailsMap = createAccountDetailsMap(accountId, (String)inboxfolderDetailsMap.get("folderUri"));


        resultMap.put(inboxfolderDetailsMap.get("folderUri").toString(),
                ImmutableList.of(inboxfolderDetailsMap));

        resultMap.put(((Uri) accountDetailsMap.get("accountUri")).toString(), ImmutableList.of(accountDetailsMap));

        final Map<String, Object> folderDetailsMap1 = createFolderDetailsMap(1, accountId,  "one", 0, 0);
        resultMap.put(folderDetailsMap1.get("folderUri").toString(), ImmutableList.of(folderDetailsMap1));



        Log.d("TAG", "");

    }

    public static void initializeMockProvider() {
        MOCK_QUERY_RESULTS = Maps.newHashMap();

        final int accountId =0;
        initializeAccount(accountId, MOCK_QUERY_RESULTS);


    }


    private static String getMockAccountFolderUri(int accountId, int folderId) {
        return getMockAccountUri(accountId) + "/folder/" + folderId;
    }

    private static Map<String, Object> createFolderDetailsMap(int folderId,
                                                              int accountId, String name, int unread, int total) {
        return createFolderDetailsMap(folderId, accountId, name, false, unread,
                total);
    }

    private static Map<String, Object> createFolderDetailsMap(int folderId,
                                                              int accountId, String name, boolean hasChildren, int unread,
                                                              int total) {

        final String folderUri = getMockAccountFolderUri(accountId, folderId);

        Map<String, Object> folderMap = Maps.newHashMap();

        folderMap.put(BaseColumns._ID, Long.valueOf(folderId));
        folderMap.put("folderUri", folderUri);
        folderMap.put("inbox", "Folder " + name);

        return folderMap;
    }

    public static Map<String, Object> createAccountDetailsMap(int accountId,String defaultInbox) {

        final String accountUri = getMockAccountUri(accountId);

        Map<String, Object> accountMap = Maps.newHashMap();

        accountMap.put(BaseColumns._ID, Long.valueOf(accountId));
//        accountMap.put(AccountColumns.NAME, "account" + accountId + "@mockuiprovider.com");
//        accountMap.put(AccountColumns.TYPE, "com.android.mail.providers.protos.mock");
////	        accountMap.put(AccountColumns.ACCOUNT_MANAGER_NAME,
////	                "account" + accountId + "@mockuiprovider.com");
////	        accountMap.put(AccountColumns.PROVIDER_VERSION, Long.valueOf(1));
        accountMap.put("accountUri", Uri.parse(accountUri));
//
//
//
//        accountMap.put(AccountColumns.FOLDER_LIST_URI, Uri.parse(accountUri + "/folders"));
//        accountMap.put(AccountColumns.FULL_FOLDER_LIST_URI, Uri.parse(accountUri + "/folders"));
//        accountMap.put(AccountColumns.ALL_FOLDER_LIST_URI, Uri.parse(accountUri + "/folders"));
//
//        accountMap.put(AccountColumns.RECENT_FOLDER_LIST_URI,
//                Uri.parse(accountUri + "/recentFolderListUri"));
//
//        accountMap.put(AccountColumns.DEFAULT_RECENT_FOLDER_LIST_URI, Uri.EMPTY);
//
//        accountMap.put(SettingsColumns.DEFAULT_INBOX, defaultInbox);
//        accountMap.put(SettingsColumns.DEFAULT_INBOX_NAME, "Inbox");


        return  accountMap;
    }

    // </editor-fold>

    private static int findMatch(Uri uri, String methodName) {
        int match = sUriMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown uri: " + uri);
        } else if (LOGD) {
            Log.v(TAG, methodName + ": uri=" + uri + ", match is " + match);
        }
        return match;
    }

   // @Override
    public Cursor mockquery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final List<Map<String, Object>> queryResults = MOCK_QUERY_RESULTS.get(uri.toString());

        if (queryResults != null && queryResults.size() > 0) {
            // Get the projection.  If there are rows in the result set, pick the first item to
            // generate the projection
            // TODO (pwestbro): handle the case where we want to return an empty result.\
            if (projection == null) {
                Set<String> keys = queryResults.get(0).keySet();
                projection = keys.toArray(new String[keys.size()]);
            }
            final MatrixCursor matrixCursor =
                    new MatrixCursor(projection, queryResults.size());

            for (Map<String, Object> queryResult : queryResults) {
                MatrixCursor.RowBuilder rowBuilder = matrixCursor.newRow();

                for (String key : projection) {
                    rowBuilder.add(queryResult.get(key));
                }
            }
            return matrixCursor;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection)
                + ")");
        final SQLiteDatabase db = mOpenHelper.getDatabase(false);
       // final int match = sUriMatcher.match(uri);
        final int match = findMatch(uri, "query");
        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);
                Cursor cursor =  builder.where(selection, selectionArgs).query(db,
                        projection, sortOrder);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }

                return cursor;
            }

        }

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOLDER:
                return PandTContract.Folders.CONTENT_TYPE;
            case FOLDER_ID:
                return PandTContract.Folders.CONTENT_ITEM_TYPE;
            case ACCOUNT:
                return PandTContract.Accounts.CONTENT_TYPE;
            case ACCOUNT_ID:
                return PandTContract.Accounts.CONTENT_ITEM_TYPE;
            case SUBJECT    :
                return PandTContract.SubjectManager.CONTENT_TYPE;
            case SESSION:
                return PandTContract.Sessions.CONTENT_TYPE;
            case SESSION_ID:
                return PandTContract.Sessions.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static String getMockAccountUri(int accountId) {
        return "name://" + AUTHORITY + "/account/" + accountId;
    }

    public static Uri getAccountsUri() {
        return MOCK_ACCOUNTS_URI;
    }

    public static int getSubjectManagerCount(Context c, long Id) {
        Cursor cursor = c.getContentResolver().query(
                ContentUris.withAppendedId(PandTContract.SubjectManager.CONTENT_URI, Id), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOLDER: {
                return builder.table(Tables.FOLDERS);
            }

            case ACCOUNT: {
                return builder.table(Tables.ACCOUNTS);
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case FOLDER: {
                return builder.table(Tables.FOLDERS);
            }
            case ACCOUNT: {
                return builder.table(Tables.ACCOUNTS);
            }
            case SUBJECT: {
                return builder.table(Tables.SUBJECTMANAGER);
            }
            case ACCOUNT_ID: {
                final String accoutId = uri.getLastPathSegment();
                return builder.table(Tables.ACCOUNT_JOIN_ICON)
                        .mapToTable(PandTContract.Accounts._ID, Tables.ACCOUNTS)
                        .where(
                                PandTContract.Accounts.FOLDER_ID + "=?", accoutId);
            }
            case SESSION: {

                return builder.table(Tables.SESSIONS);
            }
            case SESSION_ID: {
                final String sessionId = uri.getLastPathSegment();
                return builder.table(Tables.SESSIONS).where(
                        PandTContract.Sessions.SESSION_ID + "=?", sessionId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
}
