package com.bdevlin.apps.pandt.accounts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//import com.bdevlin.apps.provider.MockContract.Accounts;

import com.bdevlin.apps.pandt.CursorCreator;
import com.bdevlin.apps.pandt.ObjectCursor;
import com.bdevlin.apps.providers2.UIProvider.AccountColumns;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brian on 8/31/2014.
 */
public class Account  implements Parcelable {

    private static final String TAG = Account.class.getSimpleName();

    public final Uri uri;
    public final Uri folderListUri;
    public final String account_name;
    public final String type;



    public static Account newinstance(String serializedAccount) {
        // The heavy lifting is done by Account(name, type, serializedAccount). This method
        // is a wrapper to check for errors and exceptions and return back a null in cases
        // something breaks.
        JSONObject json;
        try {
            json = new JSONObject(serializedAccount);
            final String name = (String) json.get(AccountColumns.NAME);
            final String uri = (String) json.get(AccountColumns.URI);
            final String folderListUri = (String) json.get(AccountColumns.FOLDER_LIST_URI);
            final String type = (String) json.get(AccountColumns.TYPE);

            return new Account(name, type, serializedAccount);
        } catch (JSONException e) {

            return null;
        }
    }


    private Account(String acctName,  String acctType,  String jsonAccount) throws JSONException {
        account_name = acctName;
        type = acctType;

        final JSONObject json = new JSONObject(jsonAccount);

        uri = Uri.parse(json.optString(AccountColumns.URI));
        folderListUri = Uri.parse(json.optString(AccountColumns.FOLDER_LIST_URI));

    }

    public Account(Parcel in, ClassLoader loader) {
        account_name = in.readString();
        uri = in.readParcelable(null);
        folderListUri = in.readParcelable(null);
        type = in.readString();
    }

    public Account(Parcel in) {
        account_name = in.readString();
        uri = in.readParcelable(null);
        folderListUri = in.readParcelable(null);
        type = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account_name);
        dest.writeParcelable(uri, 0);
        dest.writeParcelable(folderListUri, 0);
        dest.writeString(type);

    }


    public synchronized String serialize() {
        JSONObject json = new JSONObject();
        try {
            Log.d(TAG, "serialize:");

            json.put(AccountColumns.NAME, account_name);
            json.put(AccountColumns.URI, uri);
            json.put(AccountColumns.FOLDER_LIST_URI, folderListUri);
            json.put(AccountColumns.TYPE, type);

        } catch (JSONException e) {
            Log.d(TAG, "exception: " + e);
        }
        return json.toString();
    }


    public Account(Cursor cursor) {

        account_name = cursor.getString(cursor.getColumnIndex(AccountColumns.NAME));
        uri = Uri.parse(cursor.getString(cursor.getColumnIndex(AccountColumns.URI)));
        folderListUri = Uri.parse(cursor.getString(cursor.getColumnIndex(AccountColumns.FOLDER_LIST_URI)));
        type = cursor.getString(cursor.getColumnIndex(AccountColumns.TYPE));


    }

    public static Account[] getAllAccounts(ObjectCursor<Account> cursor) {
        final int initialLength = cursor.getCount();
        if (initialLength <= 0 || !cursor.moveToFirst()) {
            // Return zero length account array rather than null
            return new Account[0];
        }

        final Account[] allAccounts = new Account[initialLength];
        int i = 0;
        do {
            allAccounts[i++] = cursor.getModel();
        } while (cursor.moveToNext());
        // Ensure that the length of the array is accurate
        assert (i == initialLength);
        return allAccounts;
    }

    public Map<String, Object> getValueMap() {
        // ImmutableMap.Builder does not allow null values
        final Map<String, Object> map = new HashMap<String, Object>();

        map.put(AccountColumns._ID, 0);
        map.put(AccountColumns.NAME, account_name);

        map.put(AccountColumns.TYPE, type);

        map.put(AccountColumns.URI, uri);

        map.put(AccountColumns.FOLDER_LIST_URI, folderListUri);

       // settings.getValueMap(map);

        return map;
    }



    public final static CursorCreator<Account> FACTORY = new CursorCreator<Account>() {
        @Override
        public Account createFromCursor(Cursor c) {
            return new Account(c);
        }

        @Override
        public String toString() {
            return "Account CursorCreator";
        }
    };


    public static final Parcelable.Creator<Account> CREATOR = new Creator<Account>() {

        public Account createFromParcel(Parcel source) {

            return new Account(source);
        }

        public Account[] newArray(int size) {

            return new Account[size];
        }

    };
}
