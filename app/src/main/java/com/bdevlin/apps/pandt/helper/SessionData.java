package com.bdevlin.apps.pandt.helper;

/**
 * Created by brian on 2/20/2016.
 */
public class SessionData {
    private String mSessionName;
    private String mDetails;
    private String mSessionId;
    private String mImageUrl;
    private String mMainTag;
    private String mTags;

    public SessionData() { }
//    BaseColumns._ID,
//    SessionsColumns.SESSION_ID,
//    SessionsColumns.SESSION_TITLE,
//    SessionsColumns.SESSION_TAGS,
//    SessionsColumns.SESSION_MAIN_TAG,
    public SessionData(String sessionId, String sessionName, String tags, String mainTag) {
        updateData(sessionId, sessionName, tags,mainTag);
    }

    public void updateData(String sessionId, String sessionName, String tags, String mainTag) {
        mSessionId = sessionId;
        mSessionName = sessionName;
//        mDetails = details;

//        mImageUrl = imageUrl;
        mMainTag = mainTag;
        mTags = tags;
    }

    public String getSessionName() {
        return mSessionName;
    }

    public String getDetails() {
        return mDetails;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getMainTag() {
        return mMainTag;
    }

    public String getTags() { return mTags; }
}
