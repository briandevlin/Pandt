package com.bdevlin.apps.pandt.folders;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Parcel;
//import android.os.Parcelable;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.provider.MockUiProvider;

/**
 * Created by brian on 8/26/2014.
 */
public class Folder   implements Parcelable, Comparable<Folder> {
    private static final boolean DEBUG = true;
    private static final String FOLDER_UNINITIALIZED = "Uninitialized!";
    public int id;
    public String name;
    public String uri;

    public String bgColor;
    public String fgColor;

    public int bgColorInt;
    public int fgColorInt;


    /**
     * Public object that knows how to construct Folders given Cursors.
     */
    public static final CursorCreator<Folder> FACTORY = new CursorCreator<Folder>() {
        @Override
        public Folder createFromCursor(Cursor c) {
            return new Folder(c);
        }

        @Override
        public String toString() {
            return "Folder CursorCreator";
        }
    };


    public static final Parcelable.Creator<Folder> CREATOR
            = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in, ClassLoader loader) {
            return new Folder(in, loader);
        }
        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    });

    private Folder() {
        name = FOLDER_UNINITIALIZED;
    }

    public Folder(int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public Folder(Cursor cursor) {
         id = cursor.getInt(MockContract.Folders.FOLDER_ID_COLUMN);
        name = cursor.getString(MockContract.Folders.FOLDER_NAME_COLUMN);
        uri = cursor.getString(MockContract.Folders.FOLDER_URI_COLUMN);
    }

    public static Folder newUnsafeInstance() {
        return new Folder();
    }

    public Folder(Parcel in, ClassLoader loader) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public int compareTo(Folder another) {
        return name.compareToIgnoreCase(another.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
    }

     @Override
     public String toString() {
        return name;
     }
}
