package com.bdevlin.apps.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bdevlin.apps.pandt.R;

/**
 * Created by brian on 11/11/2015.
 */
public class HelpListFragment extends Fragment {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

   // private TextView mHelpText;
    private WebView mHelpText;
    private CharSequence mContent;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.help, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisibilityChange();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // ActionBar ab = getActivity().getSupportActionBar();
        updateText();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        onVisibilityChange();
    }

    private void onVisibilityChange() {
        if (getUserVisibleHint()) {
            updateTitle();
        }
    }

    private void updateText() {
       // mHelpText = (TextView) getView().findViewById(R.id.help_text);
        mHelpText = (WebView) getView().findViewById(R.id.webView1);
        mHelpText.getSettings().setJavaScriptEnabled(false);
        
        CharSequence content = getContent();
        SpannedString sstr = SpannedString.valueOf(content);

        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(Html.fromHtml(Html.toHtml(sstr)));
        aboutBody.setSpan(new StyleSpan(Typeface.ITALIC), 0, aboutBody.length(), 0);
        //mHelpText.setText(aboutBody);
        mHelpText.loadData(Html.toHtml(sstr), "text/html", null);
        //mHelpText.loadUrl("http://www.choosemyplate.gov/tools-supertracker");

    }
    private void updateTitle() {
        getActivity().setTitle(getString(R.string.title_help) + " > " + getTitle());
    }

    private void initializeArgCache() {
        if (mTitle != null) return;
        Bundle args = getArguments();
        mTitle = args.getString(TITLE);
        mContent = args.getCharSequence(CONTENT);
    }
    private String getTitle() {
        initializeArgCache();
        return mTitle;
    }

    private CharSequence getContent() {
        initializeArgCache();
        return mContent;
    }

}
