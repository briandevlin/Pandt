package com.bdevlin.apps.pandt.folders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;


/**
 * Created by bdevlin on 9/25/2015.
 */
public class FolderItemView  extends LinearLayout {

    private TextView mFolderTextView;
    private TextView mUnreadCountTextView;
    private ImageView mFolderParentIcon;

    public FolderItemView(Context context) {
        super(context);
    }


    public FolderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public FolderItemView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mFolderTextView = (TextView)findViewById(R.id.baseName);
        mUnreadCountTextView = (TextView)findViewById(R.id.id);
        mFolderParentIcon = (ImageView) findViewById(R.id.imageview2);
    }
}
