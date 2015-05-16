package com.bdevlin.apps.pandt;

import android.database.Cursor;
//import android.database.CursorWrapper;

public class ObjectCursor<T> extends CursorWrapper {

    CursorCreator<T> mFactory;

    public ObjectCursor(Cursor cursor, CursorCreator<T> factory) {
        super(cursor);
        mFactory = factory;
    }

    public final T getModel() {
        final Cursor c = getWrappedCursor();
        if (c == null ) {
            return null;
        }
        final int currentPosition = c.getPosition();

        final T model = mFactory.createFromCursor(c);

        return model;
    }

    @Override
    public void close() {
        super.close();

    }

}
