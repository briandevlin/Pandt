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

    public SessionData(String sessionName, String details, String sessionId, String imageUrl,
                       String mainTag) {
        updateData(sessionName, details, sessionId,imageUrl,mainTag);
    }

    public void updateData(String sessionName, String details, String sessionId, String imageUrl,
                           String mainTag) {
        mSessionName = sessionName;
        mDetails = details;
        mSessionId = sessionId;
        mImageUrl = imageUrl;
        mMainTag = mainTag;
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
}
