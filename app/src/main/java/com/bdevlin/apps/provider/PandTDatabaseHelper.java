package com.bdevlin.apps.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.bdevlin.apps.provider.PandTContract.Accounts;
import com.bdevlin.apps.provider.PandTContract.Folders;
import com.bdevlin.apps.provider.PandTContract.SubjectManager;


import java.util.HashSet;


/**
 * Created by brian on 8/29/2014.
 */
public class PandTDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = PandTDatabaseHelper.class.getSimpleName();

    protected static final String DATABASE_NAME = "mydatabase.db";

    private static final int VER_LAUNCH = 1;
    private static final int VER_SESSION_TYPE = 1;

    private static final int DATABASE_VERSION = VER_SESSION_TYPE;

    private Context mContext;

    private static final HashSet<String> mValidTables = new HashSet<String>();

    static {
                  mValidTables.add("folders");
                  mValidTables.add("accounts");

              }

    interface Tables {
        String FOLDERS = "folders";
        String ACCOUNTS = "accounts";
        String SUBJECTMANAGER = "SubjectManager";
        String ICONLOOKUP = "iconlookup";
        String ACCOUNT_JOIN_ICON = "accounts "
                + "LEFT OUTER  JOIN iconlookup ON accounts._id=iconlookup.iconid "
                ;
    }

    // constructor
    public PandTDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.v(TAG, "Creating the database " + DATABASE_NAME);

    }

    public SQLiteDatabase getDatabase(boolean writable) {
        return writable ? getWritableDatabase() : getReadableDatabase();
    }

    public static boolean isValidTable(String name) {
        return mValidTables.contains(name);
    }

    static void createAccountTable(SQLiteDatabase db) {
        String s = " ("
                + Accounts._ID + " integer primary key autoincrement, "
                + Accounts.FOLDER_ID + " TEXT NOT NULL,"
                + Accounts.ACCOUNT_ID + " TEXT NOT NULL,"
                + Accounts.ACCOUNT_NAME + " text, "
                + Accounts.FOLDER_URI + " text, "
                + Accounts.FOLDER_LIST_URI + " text, "
                + Accounts.FOLDER_TYPE + " text "
                + ");";

        db.execSQL("create table " + Tables.ACCOUNTS + s);

        db.execSQL("INSERT INTO accounts VALUES(1,11,11,'Nouns', '', 'Nouns are the names of persons, places or things.', null)");
        db.execSQL("INSERT INTO accounts VALUES(2,11,12,'Verbs', null, 'Verbs show action or state of being. But it''s better to think of a verb as something with a subject', null)");
        db.execSQL("INSERT INTO accounts VALUES(3,11,13,'Adjectives', '', 'Adjectives (And adjectival phrases) modify ONLY nouns or pronouns and tell WHICH, WHOSE, WHAT KIND, and HOW MANY.', null)");
        db.execSQL("INSERT INTO accounts VALUES(4,11,14,'Pronouns', '', 'Pronouns take the place of nouns.', null)");
        db.execSQL("INSERT INTO accounts VALUES(5,11,15,'Adverbs', '', 'Adverbs (And adverbial phrases) modify verbs, adjectives, and other adverbs and tell HOW, WHEN, WHERE, and HOW MUCH.', null)");
        db.execSQL("INSERT INTO accounts VALUES(6,11,16,'Prepositions', '', 'Prepositions must have an object and show a relationship between its object and some other word in the sentence.', null)");
        db.execSQL("INSERT INTO accounts VALUES(7,11,17,'Conjunctions', '', 'Conjunctions join words, phrases, and clauses.', null)");
        db.execSQL("INSERT INTO accounts VALUES(8,11, 18,'Interjections', '', 'Interjections show feeling and are punctuated with either a comma or an exclamation point.', null)");

        db.execSQL("INSERT INTO accounts VALUES(9,22,21,'Nouns', '', 'Nouns are the names of persons, places or things.', null)");
        db.execSQL("INSERT INTO accounts VALUES(10,22,22,'Verbs', null, 'Verbs show action or state of being. But it''s better to think of a verb as something with a subject', null)");
        db.execSQL("INSERT INTO accounts VALUES(11,22,23,'Adjectives', '', 'Adjectives (And adjectival phrases) modify ONLY nouns or pronouns and tell WHICH, WHOSE, WHAT KIND, and HOW MANY.', null)");
        db.execSQL("INSERT INTO accounts VALUES(12,33,24,'Pronouns', '', 'Pronouns take the place of nouns.', null)");
        db.execSQL("INSERT INTO accounts VALUES(13,33,25,'Adverbs', '', 'Adverbs (And adverbial phrases) modify verbs, adjectives, and other adverbs and tell HOW, WHEN, WHERE, and HOW MUCH.', null)");
        db.execSQL("INSERT INTO accounts VALUES(14,44,26,'Prepositions', '', 'Prepositions must have an object and show a relationship between its object and some other word in the sentence.', null)");
        db.execSQL("INSERT INTO accounts VALUES(15,44,27,'Conjunctions', '', 'Conjunctions join words, phrases, and clauses.', null)");
        db.execSQL("INSERT INTO accounts VALUES(16,55, 28,'Interjections', '', 'Interjections show feeling and are punctuated with either a comma or an exclamation point.', null)");

    }


    static void createFolderTable(SQLiteDatabase db) {
        String s = " ("
                + Folders._ID + " integer primary key autoincrement, "
                + Folders.FOLDER_ID + " TEXT NOT NULL,"
                + Folders.FOLDER_NAME + " text, "
                + Folders.FOLDER_ICON + " text, "
                + Folders.FOLDER_URI + " text "
                + ");";

        db.execSQL("create table " + Tables.FOLDERS + s);

        db.execSQL("INSERT INTO folders VALUES(1,11,'Grammar', 'ic_speaker_notes_black_24dp', 'content://com.bdevlin.apps.pandt.MockUiProvider/accounts/11' )");
        db.execSQL("INSERT INTO folders VALUES(2,22,'Practice', 'ic_store_black_24dp', 'content://com.bdevlin.apps.pandt.MockUiProvider/accounts/22' )");
        db.execSQL("INSERT INTO folders VALUES(3,33,'Elementary', 'ic_work_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/accounts/33' )");
        db.execSQL("INSERT INTO folders VALUES(4,44,'Intermediate', 'ic_speaker_notes_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/accounts/44' )");
        db.execSQL("INSERT INTO folders VALUES(5,55,'Advanced', 'ic_store_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/accounts/55' )");
        db.execSQL("INSERT INTO folders VALUES(6,66,'Quiz', 'ic_work_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/accounts/11' )");


    }

    static void createSubjectManagerTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + Tables.SUBJECTMANAGER + " ("
                + SubjectManager._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SubjectManager.SUBJECTMANAGER_SID + " INTEGER, "
                + SubjectManager.SUBJECTMANAGER_DESC + " TEXT "

                + ");");

        db.execSQL("INSERT INTO SubjectManager VALUES(1,123456, 'Brian`s subject manager')");
    }

    static void createIconLookupTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + Tables.ICONLOOKUP + " ("
                + PandTContract.Icons._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PandTContract.Icons.ICON_ID + " INTEGER, "
                + PandTContract.Icons.ICON_NAME + " TEXT "

                + ");");

        db.execSQL("INSERT INTO ICONLOOKUP VALUES(1,1, 'ic_speaker_notes_black_24dp')");
        db.execSQL("INSERT INTO ICONLOOKUP VALUES(2,2, 'ic_store_black_24dp')");
        db.execSQL("INSERT INTO ICONLOOKUP VALUES(null,3, 'ic_work_black_24dp')");
        db.execSQL("INSERT INTO ICONLOOKUP VALUES(null,4, 'ic_speaker_notes_black_24dp')");
        db.execSQL("INSERT INTO ICONLOOKUP VALUES(null,5, 'ic_store_black_24dp')");
        db.execSQL("INSERT INTO ICONLOOKUP VALUES(null,6, 'ic_work_black_24dp')");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Bootstrapping database version: " + DATABASE_VERSION);

        createAccountTable(db);
        createFolderTable(db);
        createSubjectManagerTable(db);
        createIconLookupTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PandTDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
//        if (oldVersion == 1) {
//            // Message Tables: Add SyncColumns.SERVER_TIMESTAMP
//            try {
//
//                db.execSQL("CREATE TABLE " + Tables.ACCOUNTS + " (" +
//                        MockContract.Accounts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                        MockContract.Accounts.ACCOUNT_NAME + " TEXT " +
//                        ");");
//
//                db.execSQL("INSERT INTO accounts VALUES(1,'account one')");
//                db.execSQL("INSERT INTO accounts VALUES(2,'account two')");
//                db.execSQL("INSERT INTO accounts VALUES(3,'account three')");
//
//
//            } catch (SQLException e) {
//                // Shouldn't be needed unless we're debugging and interrupt the process
//                Log.w(TAG, "Exception upgrading " + DATABASE_NAME, e);
//            }
//
//            oldVersion = 2;
//
//        }
    }
}
