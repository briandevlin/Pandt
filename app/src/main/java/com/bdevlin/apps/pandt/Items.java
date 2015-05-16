package com.bdevlin.apps.pandt;

/**
 * Created by brian on 7/27/2014.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Items {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ListItem> ITEMS = new ArrayList<ListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ListItem> ITEM_MAP = new HashMap<String, ListItem>();

    static {
        // Add 3 sample items.
        addItem(new ListItem("1", "Item 1"));
        addItem(new ListItem("2", "Item 2"));
        addItem(new ListItem("3", "Item 3"));
    }

    private static void addItem(ListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static int getCount()
    {
       return  ITEMS.size();
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class ListItem {
        public String id;
        public String content;

        public ListItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
