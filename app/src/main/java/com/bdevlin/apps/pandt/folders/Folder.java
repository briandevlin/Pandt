package com.bdevlin.apps.pandt.folders;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.provider.MockUiProvider;

/**
 * Created by brian on 8/26/2014.
 */
public class Folder   implements Parcelable, Comparable<Folder> {
    private static final String FOLDER_UNINITIALIZED = "Uninitialized!";
    public int id;
    public String name;

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

    public static final ClassLoaderCreator<Folder> CREATOR = new ClassLoaderCreator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source, null);
        }

        @Override
        public Folder createFromParcel(Parcel source, ClassLoader loader) {
            return new Folder(source, loader);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };


    private Folder() {
        name = FOLDER_UNINITIALIZED;
    }

    public Folder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Folder(Cursor cursor) {
        id = cursor.getInt(MockUiProvider.FOLDER_ID_COLUMN);
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
