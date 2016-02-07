package com.bdevlin.apps.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.bdevlin.apps.provider.MockContract.Accounts;
import com.bdevlin.apps.provider.MockContract.Folders;


import java.util.HashSet;


/**
 * Created by brian on 8/29/2014.
 */
public class MockDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = MockDatabaseHelper.class.getSimpleName();

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

    }

    // constructor
    public MockDatabaseHelper(Context context) {
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
        String s = " (" + Accounts._ID + " integer primary key autoincrement, "
                + Accounts.ACCOUNT_NAME + " text, "
                + Accounts.FOLDER_URI + " text, "
                + Accounts.FOLDER_LIST_URI + " text, "
                + Accounts.TYPE + " text "
                + ");";

        db.execSQL("create table " + Tables.ACCOUNTS + s);

        db.execSQL("INSERT INTO accounts VALUES(1,'Nouns', 'ic_speaker_notes_black_24dp', 'Nouns are the names of persons, places or things.', null)");
        db.execSQL("INSERT INTO accounts VALUES(2,'Verbs', null, 'Verbs show action or state of being.', null)");
        db.execSQL("INSERT INTO accounts VALUES(3,'Adjectives', '', 'Adjectives (And adjectival phrases) modify ONLY nouns or pronouns and tell WHICH, WHOSE, WHAT KIND, and HOW MANY.', null)");
        db.execSQL("INSERT INTO accounts VALUES(4,'Pronouns', '', 'Pronouns take the place of nouns.', null)");
        db.execSQL("INSERT INTO accounts VALUES(5,'Adverbs', '', 'Adverbs (And adverbial phrases) modify verbs, adjectives, and other adverbs and tell HOW, WHEN, WHERE, and HOW MUCH.', null)");
        db.execSQL("INSERT INTO accounts VALUES(6,'Prepositions', '', 'Prepositions must have an object and show a relationship between its object and some other word in the sentence.', null)");
        db.execSQL("INSERT INTO accounts VALUES(7,'Conjunctions', '', 'Conjunctions join words, phrases, and clauses.', null)");
        db.execSQL("INSERT INTO accounts VALUES(8,'Interjections', '', 'Interjections show feeling and are punctuated with either a comma or an exclamation point.', null)");
//        db.execSQL("INSERT INTO accounts VALUES(9,'account nine', null, null, null)");
//        db.execSQL("INSERT INTO accounts VALUES(10,'account ten', null, null, null)");
//        db.execSQL("INSERT INTO accounts VALUES(11,'account eleven', null, null, null)");
//        db.execSQL("INSERT INTO accounts VALUES(12,'account twelve', null, null, null)");
    }


    static void createFolderTable(SQLiteDatabase db) {
        String s = " (" + Folders._ID + " integer primary key autoincrement, "
                + Folders.FOLDER_NAME + " text, "
                + Folders.FOLDER_ICON + " text, "
                + Folders.FOLDER_URI + " text "
                + ");";

        db.execSQL("create table " + Tables.FOLDERS + s);

        db.execSQL("INSERT INTO folders VALUES(1,'Grammar', 'ic_speaker_notes_black_24dp', 'content://com.bdevlin.apps.pandt.MockUiProvider/folder/1/subject/1' )");
        db.execSQL("INSERT INTO folders VALUES(2,'Practise', 'ic_store_black_24dp', 'content://com.bdevlin.apps.pandt.MockUiProvider/folder/2/subject/2' )");
        db.execSQL("INSERT INTO folders VALUES(3,'Elementary', 'ic_work_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/folder/3/subject/3' )");
        db.execSQL("INSERT INTO folders VALUES(4,'Intermediate', 'ic_speaker_notes_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/folder/4/subject/4' )");
        db.execSQL("INSERT INTO folders VALUES(5,'Advanced', 'ic_store_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/folder/5/subject/5' )");
        db.execSQL("INSERT INTO folders VALUES(6,'Quiz', 'ic_work_black_24dp','content://com.bdevlin.apps.pandt.MockUiProvider/folder/6/subject/6' )");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Bootstrapping database version: " + DATABASE_VERSION);

        createAccountTable(db);
        createFolderTable(db);

//        db.execSQL("CREATE TABLE " + Tables.FOLDERS + " (" +
//                MockContract.Folders._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                MockContract.Folders.FOLDER_NAME + " TEXT " +
//                ");");
//
//        db.execSQL("INSERT INTO folders VALUES(1,'folder one')");
//        db.execSQL("INSERT INTO folders VALUES(2,'folder two')");
//        db.execSQL("INSERT INTO folders VALUES(3,'folder three')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MockDatabaseHelper.class.getName(),
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
