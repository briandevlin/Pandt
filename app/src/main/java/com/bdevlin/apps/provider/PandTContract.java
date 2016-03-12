package com.bdevlin.apps.provider;

import android.content.ContentResolver;
import android.net.Uri;

import android.provider.BaseColumns;

/**
 * Created by brian on 8/24/2014.
 * Contract class for interacting with {@link PandTUiProvider}.
 */
public final class PandTContract {

    // <editor-fold desc="Fields">
    private static final String TAG = PandTContract.class.getSimpleName();


    public static final int VERSION = 1;

    // this is what goes into the manifest to identify the provider
    public static final String CONTENT_AUTHORITY = "com.bdevlin.apps.pandt.MockUiProvider";

    /**
     * A content:// style uri to the authority for the provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final Uri URI_BASE  = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(CONTENT_AUTHORITY)
            .build();

    public static final String RECORD_ID = BaseColumns._ID;


    // define all the uri paths here, these will be used in the definitions of
    // the CONTENT_URI for each class
    private static final String PATH_SUBJECTS = "subjects";
    private static final String PATH_FOLDERS = "folders";
    private static final String PATH_ACCOUNTS = "accounts";
    private static final String PATH_ICONS = "icons";
    private static final String PATH_SESSIONS = "sessions";
    public static final String[] PATHS = {
            PATH_SUBJECTS,
            PATH_FOLDERS,
            PATH_ACCOUNTS,
            PATH_ICONS,
            PATH_SESSIONS
    };


    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";

    // </editor-fold>

    // <editor-fold desc="Constructor">
    // private constructor i.e this class cannot be instantiated
    private PandTContract() {
    }
    // </editor-fold>

    // <editor-fold desc="Interfaces">
    protected interface FolderColumns {
        /**
         * The display baseName for the
         * <P>Type: TEXT</P>
         */
          String FOLDER_ID = "id";
           String FOLDER_NAME = "name";
           String FOLDER_ICON = "icon";
           String FOLDER_URI = "uri";
    }

    public interface SubjectManagerColumns {
         String SUBJECTMANAGER_SID = "subjectmanagersid";
          String SUBJECTMANAGER_DESC = "subjectmanagerdescription";
    }

    public interface AccountColumns {
        /**
         * The display baseName for the
         * <P>Type: TEXT</P>
         */
          String FOLDER_ID = "folder_id";
          String ACCOUNT_ID = "account_id";
          String ACCOUNT_NAME = "account_name";
          String FOLDER_LIST_URI = "folderListUri";
          String FOLDER_URI = "subjectmangersid";
          String FOLDER_TYPE = "type";
    }

    protected interface IconColumns {
        /**
         * The display baseName for the
         * <P>Type: TEXT</P>
         */
        String ICON_ID = "iconId";
        String ICON_NAME = "iconName";
    }

    protected interface SessionsColumns {

        /** Unique string identifying this session. */
        String SESSION_ID = "session_id";
        /** Difficulty level of the session. */
        String SESSION_LEVEL = "session_level";
        /** Start time of this track. */
        String SESSION_START = "session_start";
        /** End time of this track. */
        String SESSION_END = "session_end";
        /** Title describing this track. */
        String SESSION_TITLE = "session_title";
        /** Body of text explaining this session in detail. */
        String SESSION_ABSTRACT = "session_abstract";
        /** Requirements that attendees should meet. */
        String SESSION_REQUIREMENTS = "session_requirements";
        /** Kewords/tags for this session. */
        String SESSION_KEYWORDS = "session_keywords";
        /** Hashtag for this session. */
        String SESSION_HASHTAG = "session_hashtag";
        /** Full URL to session online. */
        String SESSION_URL = "session_url";
        /** Full URL to YouTube. */
        String SESSION_YOUTUBE_URL = "session_youtube_url";
        /** Full URL to PDF. */
        String SESSION_PDF_URL = "session_pdf_url";
        /** Full URL to official session notes. */
        String SESSION_NOTES_URL = "session_notes_url";
        /** User-specific flag indicating starred status. */
        String SESSION_IN_MY_SCHEDULE = "session_in_my_schedule";
        /** Key for session Calendar event. (Used in ICS or above) */
        String SESSION_CAL_EVENT_ID = "session_cal_event_id";
        /** The YouTube live stream URL. */
        String SESSION_LIVESTREAM_ID = "session_livestream_url";
        /** The Moderator URL. */
        String SESSION_MODERATOR_URL = "session_moderator_url";
        /** The set of tags the session has. This is a comma-separated list of tags. */
        String SESSION_TAGS = "session_tags";
        /** The names of the speakers on this session, formatted for display. */
        String SESSION_SPEAKER_NAMES = "session_speaker_names";
        /** The order (for sorting) of this session's type. */
        String SESSION_GROUPING_ORDER = "session_grouping_order";
        /** The hashcode of the data used to create this record. */
        String SESSION_IMPORT_HASHCODE = "session_import_hashcode";
        /** The session's main tag. */
        String SESSION_MAIN_TAG = "session_main_tag";
        /** The session's branding color. */
        String SESSION_COLOR = "session_color";
        /** The session's captions URL (for livestreamed sessions). */
        String SESSION_CAPTIONS_URL = "session_captions_url";
        /** The session interval when using the interval counter query. */
        String SESSION_INTERVAL_COUNT = "session_interval_count";
        /** The session's photo URL. */
        String SESSION_PHOTO_URL = "session_photo_url";
        /** The session's related content (videos and call to action links). */
        String SESSION_RELATED_CONTENT = "session_related_content";
    }

    // </editor-fold>

    // <editor-fold desc="Projections">

    public static final String[] FOLDERS_PROJECTION = {
            BaseColumns._ID,
            FolderColumns.FOLDER_ID,
            FolderColumns.FOLDER_NAME,
            FolderColumns.FOLDER_ICON,
            FolderColumns.FOLDER_URI

    };

    public static final String[] ACCOUNTS_PROJECTION = {
            BaseColumns._ID,
            AccountColumns.FOLDER_ID,
            AccountColumns.ACCOUNT_ID,
            AccountColumns.ACCOUNT_NAME,
            AccountColumns.FOLDER_URI,
            AccountColumns.FOLDER_LIST_URI,
            IconColumns.ICON_NAME,
            SubjectManager.SUBJECTMANAGER_SID

    };

    public static final String[] SUBJECTMANAGER_PROJECTION = {
            BaseColumns._ID,
            SubjectManagerColumns.SUBJECTMANAGER_SID,
            SubjectManagerColumns.SUBJECTMANAGER_DESC

    };

    public static final String[] ICON_PROJECTION = {
            BaseColumns._ID,
            IconColumns.ICON_ID,
            IconColumns.ICON_NAME

    };

    public static final String[] SESSIONS_PROJECTION = {
            BaseColumns._ID,
            SessionsColumns.SESSION_ID,
            SessionsColumns.SESSION_TITLE,
            SessionsColumns.SESSION_TAGS,
            SessionsColumns.SESSION_MAIN_TAG,
           // SessionsColumns.FOLDER_LIST_URI,
           // SessionsColumns.ICON_NAME

    };

    // </editor-fold>

    // <editor-fold desc="Classes">
    public static  class SubjectManager  implements BaseColumns, SubjectManagerColumns {
        private SubjectManager() {
        }

        public static final String TABLE_NAME = PandTDatabaseHelper.Tables.SUBJECTMANAGER;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBJECTS);

        public static final Uri COUNT_URI = Uri.withAppendedPath(CONTENT_URI, "count");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/subject";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/subject";

        public static final int SUBJECTMANGER_ID_COLUMN = 0;
        public static final int SUBJECTMANGER_SID_COLUMN = 1;
        public static final int SUBJECTMANGER_DESC_COLUMN = 2;
    }

    public static class Folders implements BaseColumns, FolderColumns {

        private Folders() {
        }

        public static final String TABLE_NAME = PandTDatabaseHelper.Tables.FOLDERS;

        //"content://com.bdevlin.apps.pandt.MockUiProvider/folders"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FOLDERS);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/folder";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/folder";

        public static final int FOLDER_ID_COLUMN = 1;
        public static final int FOLDER_NAME_COLUMN = 2;
        public static final int FOLDER_ICON_COLUMN = 3;
        public static final int FOLDER_URI_COLUMN = 4;
    }

    public static class Accounts implements BaseColumns, AccountColumns {

        private Accounts() {
        }

        public static final String TABLE_NAME = PandTDatabaseHelper.Tables.ACCOUNTS;

        //"content://com.bdevlin.apps.pandt.MockUiProvider/accounts"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACCOUNTS);


        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/account";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/account";

        public static final int ACCOUNT_FOLDER_ID_COLUMN = 1;
        public static final int ACCOUNT_ID_COLUMN = 2;
        public static final int ACCOUNT_NAME_COLUMN = 3;
        public static final int ACCOUNT_URI_COLUMN = 4;
        public static final int ACCOUNT_LISTURI_COLUMN = 5;

        public static Uri buildAccountDirUri(String accountId) {
            return CONTENT_URI.buildUpon().appendPath(accountId).build();

        }
        public static String getRoomId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static  class Icons  implements BaseColumns, IconColumns {
        private Icons() {
        }

        public static final String TABLE_NAME = PandTDatabaseHelper.Tables.ICONLOOKUP;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ICONS);



        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/icon";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/icon";

        public static final int ICON_ID_COLUMN = 0;
        public static final int ICON_NAME_COLUMN = 1;

    }

    public static class Sessions implements BaseColumns, SessionsColumns {

        private Sessions() {
        }

        public static final String TABLE_NAME = PandTDatabaseHelper.Tables.SESSIONS;

        //"content://com.bdevlin.apps.pandt.MockUiProvider/folders"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SESSIONS);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/session";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/session";

        public static final int SESSION_ID_COLUMN = 1;
        public static final int SESSION_TITLE_COLUMN = 2;
        public static final int SESSION_TAGS_COLUMN = 3;
        public static final int SESSION_MAIN_TAGS_COLUMN = 4;
    }

    // </editor-fold>

}
