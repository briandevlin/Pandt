package com.bdevlin.apps.pandt.Cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.provider.MockContract;
import com.bdevlin.apps.provider.MockUiProvider;

/**
 * Created by brian on 11/11/2014.
 */
public class SimpleAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public SimpleAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        if(cursor.getPosition()%2==1) {
//            view.setBackgroundColor(context.getResources().getColor(R.color.accent_material_light));
//        }
//        else {
//            view.setBackgroundColor(context.getResources().getColor(R.color.accent));
//        }

        TextView content = (TextView) view.findViewById(R.id.profile_email_text);
        content.setText(cursor.getString(cursor.getColumnIndex(MockContract.Folders.FOLDER_NAME)));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.list_item_account, parent, false);
    }

}

