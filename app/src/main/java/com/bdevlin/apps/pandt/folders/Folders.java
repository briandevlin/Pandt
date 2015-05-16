package com.bdevlin.apps.pandt.folders;

/**
 * Created by brian on 7/27/2014.
 */

import com.bdevlin.apps.pandt.folders.Folder;

import java.util.ArrayList;
import java.util.List;

public class Folders {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Folder> ITEMS = new ArrayList<Folder>();

    /**
     * A map of sample (dummy) items, by ID.
     */
  //  public static Map<int, Folder> ITEM_MAP = new HashMap<int, Folder>();

    static {
        // Add 3 sample items.
        addItem(new Folder(1, "Folder 1"));
        addItem(new Folder(2, "Folder 2"));
        addItem(new Folder(3, "Folder 3"));
    }

    private static void addItem(Folder item) {
        ITEMS.add(item);
       // ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of name.
     */
//    public static class Folder implements Parcelable {
//        public String id;
//        public String name;
//
//        public Folder(String id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//
//        @Override
//        public int describeContents() {
//            // Return a sort of version number for this parcelable folder. Starting with zero.
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//
//        }
//
//        @Override
//        public String toString() {
//            return name;
//        }
//    }
}
