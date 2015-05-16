package com.bdevlin.apps.pandt;

import android.database.Cursor;

public interface CursorCreator<T> {

    /**
     * Creates an object using the current row of the cursor given here. The implementation should
     * not advance/rewind the cursor, and is only allowed to read the existing row.
     * @param c
     * @return a real object of the implementing class.
     */
    T createFromCursor(Cursor c);
}

