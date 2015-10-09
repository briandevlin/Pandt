package com.bdevlin.apps.pandt.Cursors;

//import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
//import android.database.Cursor;

import android.support.v4.content.AsyncTaskLoader;

import com.bdevlin.apps.pandt.Cursors.CursorCreator;
import com.bdevlin.apps.pandt.Cursors.ObjectCursor;

public class MyObjectCursorLoader<T> extends AsyncTaskLoader<ObjectCursor<T>>{

    private Uri mUri;
    final String[] mProjection;

    final String mSelection = null;
    final String[] mSelectionArgs = null;
    final String mSortOrder = null;

    /** The underlying cursor that contains the data. */
    ObjectCursor<T> mCursor;

    /** The factory that knows how to create T objects from cursors: one object per row. */
    private final CursorCreator<T> mFactory;


    public MyObjectCursorLoader(Context context, Uri uri, String[] projection,
                                CursorCreator<T> factory) {
        super(context);

        /*
         * If these are null, it's going to crash anyway in loadInBackground(), but this stack trace
         * is much more useful.
         */
        if (factory == null) {
            throw new NullPointerException("The factory cannot be null");
        }

//        mObserver = new ForceLoadContentObserver();
        setUri(uri);
        mProjection = projection;
        mFactory = factory;
    }

    @Override
    public ObjectCursor<T> loadInBackground() {
        final Cursor inner = (Cursor)getContext().getContentResolver().query(mUri, mProjection,
                mSelection, mSelectionArgs, mSortOrder);

        if (inner == null) {
            // If there's no underlying cursor, there's nothing to do.
            return null;
        }

        inner.getCount();
        // ObjectCursor<T> extends Cursor (actually via CursorWrapper) and adds method getModel()
        final ObjectCursor<T> cursor = getObjectCursor(inner);

        return cursor;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(ObjectCursor<T> cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        final Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        // cancelLoad();
    }

    @Override
    public void onCanceled(ObjectCursor<T> cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

    protected ObjectCursor<T> getObjectCursor(Cursor inner) {
        return new ObjectCursor<T>(inner, mFactory);
    }


    public final Uri getUri() {
        return mUri;
    }

    public final void setUri(Uri uri) {
        if (uri == null) {
            throw new NullPointerException("The uri cannot be null");
        }
        mUri = uri;
    }
}
