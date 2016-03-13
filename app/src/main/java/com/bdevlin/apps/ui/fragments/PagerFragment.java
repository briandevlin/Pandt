package com.bdevlin.apps.ui.fragments;

import android.app.Activity;
//import android.app.LoaderManager;
//import android.content.Context;
//import android.content.Loader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.helper.ItemGroup;
import com.bdevlin.apps.pandt.helper.SessionData;
import com.bdevlin.apps.pandt.helper.TopicGroup;
import com.bdevlin.apps.provider.PandTContract;
import com.bdevlin.apps.ui.widgets.CollectionView;
import com.bdevlin.apps.ui.widgets.CollectionViewCallbacks;
import com.bdevlin.apps.utils.ViewMode;
import com.bdevlin.apps.utils.VolleyController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by brian on 7/26/2014.
 */

public class PagerFragment extends Fragment
        implements ViewMode.ModeChangeListener, CollectionViewCallbacks, LoaderManager.LoaderCallbacks<Cursor>{

    // <editor-fold desc="Fields">
    private static final String TAG = PagerFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static final String ARG_INDEX = " com.bdevlin.apps.pandt.arg_position";
    private static final int GROUP_ID_KEYNOTE_STREAM_CARD = 10;

    private static final int GROUP_ID_LIVE_STREAM_CARD = 15;

    private static final int GROUP_ID_MESSAGE_CARDS = 20;

    private static final int GROUP_ID_TOPIC_CARDS = 30;

    private static final int GROUP_ID_THEME_CARDS = 40;

    private ControllableActivity mActivity;
    private ActionBarController controller = null;
    private  int  mPosition;
    private CollectionView mCollectionView = null;
    private ActionBarController actionBarController = null;
    private View mEmptyView = null;
   // private ImageLoader mImageLoader;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String mTopic;
    private  String mContent = "";
    private Map<String, TopicGroup> mTopics = new HashMap<>();


    // </editor-fold>

    // <editor-fold desc="New Instance">
    public static PagerFragment newInstance(String content, String topic) {
        PagerFragment fragment = new PagerFragment();
        fragment.setModel(content);
        fragment.setTopic(topic);
        return fragment;
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public PagerFragment() {
    }
    public void setModel(String content) {
       // if (DEBUG) Log.d(TAG, "Pager Fragment: setModel for : " + content);
        this.mContent = content;
    }
    public void setTopic(String topic) {
      // if (DEBUG) Log.d(TAG, "Pager Fragment: setTopic for : " + topic);
        this.mTopic = topic;
    }
    protected void parseArguments() {
        final Bundle args = getArguments();
        if (args != null) {
            mPosition = args.getInt(ARG_INDEX);
        }
    }
// </editor-fold>

    // <editor-fold desc="Create">
    @Override
    public void onViewModeChanged(int newMode) {
       // Log.v(TAG, "in onViewModeChanged  " + newMode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        if (DEBUG) Log.d(TAG, "Pager Fragment: onCreate : " + mPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_slide_view, container, false);

        mCollectionView = (CollectionView) rootView.findViewById(R.id.videos_collection_view);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.scrollToPosition(0);
        mCollectionView.setLayoutManager(mLayoutManager);
       // ViewCompat.setNestedScrollingEnabled(mCollectionView, true);
        mEmptyView = rootView.findViewById(android.R.id.empty);
       // getActivity().overridePendingTransition(0, 0);

        SetupViewMode();

        SetupActionToolbar();

       // mEmptyView.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void SetupActionToolbar()
    {
        actionBarController = mActivity.getActionBarController();
        Toolbar toolbar = actionBarController.getSupportToolBar();
       // toolbar.setVisibility(View.GONE);
        CollapsingToolbarLayout ctl = actionBarController.getCollapsingToolbarLayout();

        if (ctl != null /*&& mViewContext.folder != null*/ ) {
           // ctl.setVisibility(View.GONE);
          //  ctl.setTitle(topic);
//            ctl.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
            TextView subTitle =  (TextView)ctl.findViewById(R.id.subTitle);
            subTitle.setText(mTopic + " -> " + mContent);
        }
        else {
            if (ctl != null) {
                TextView subTitle =  (TextView)ctl.findViewById(R.id.subTitle);
                subTitle.setText(R.string.empty);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Log.d(TAG, "Pager Fragment: onActivityCreated ");

       final Activity activity = getActivity();

        if (!(activity instanceof ControllableActivity)) {
            return;
        }
        mActivity = (ControllableActivity) activity;

        ViewMode mode = mActivity.getViewMode();
        mode.enterMainContentItemPagerMode();
        // mImageLoader = VolleyController.getInstance(activity).getImageLoader();
        startLoading();

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    private void startLoading() {
        final LoaderManager lm = getLoaderManager();
      //  final Bundle args = createLoaderArgs(getActivity().getIntent());
        // Prepare the loader. starts or reconnects
        lm.initLoader(0, null, this);
    }

    private void setContentTopClearance(int clearance) {
        if (mCollectionView != null) {
            mCollectionView.setContentTopClearance(clearance);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ControllableActivity)) {
            return;
        }

        try {
            mActivity = (ControllableActivity) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDestroyView() {
        mActivity.getViewMode().removeListener(this);
        super.onDestroyView();
    }


    private void SetupViewMode()
    {
        onViewModeChanged(mActivity.getViewMode().getMode());
        ViewMode mode = mActivity.getViewMode();
        //mode.enterMainContentListMode();
        mode.addListener(this);
    }
// </editor-fold>

    // <editor-fold desc="Option menus">

    // remember these methods don't get called if the activity handles it and then return true
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

             switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }



    @Override
    public void  onPrepareOptionsMenu(Menu menu) {

    }


    // </editor-fold>

    // <editor-fold desc="updateCollectionView">
    private void updateCollectionView(/*ExploreModel model*/) {
      //  if (DEBUG) Log.d(TAG, "Updating collection view.");
        CollectionView.Inventory inventory = new CollectionView.Inventory();
        CollectionView.InventoryGroup inventoryGroup;

        // <editor-fold desc="Dummy cursor data">
//        SessionData keynoteData = new SessionData("Session keynote","session keynote details","1", null, null);
//
//        if (keynoteData != null) {
//
//            inventoryGroup = new CollectionView.InventoryGroup
//                    (GROUP_ID_KEYNOTE_STREAM_CARD);
//
//            inventoryGroup.addItemWithTag(keynoteData);
//            inventory.addGroup(inventoryGroup);
//        }


        // lets add some dummy cursor data
//        Map<String, TopicGroup> topicGroupsMap = new HashMap<>();
//
//        SessionData session = new SessionData("session one","session one details","1",null, null);
//        SessionData session2 = new SessionData("session two","session two details","1",null, null);
//
//        TopicGroup topicGroup1 = new TopicGroup();
//        topicGroup1.setTitle("TopicGroup one");
//        topicGroup1.setId("1");
//        topicGroup1.addSessionData(session);
//        topicGroup1.addSessionData(session2);
//        topicGroupsMap.put("TopicGroup one", topicGroup1);
//
//        TopicGroup topicGroup2 = new TopicGroup();
//        topicGroup2.setTitle("TopicGroup two");
//        topicGroup2.setId("2");
//        topicGroup2.addSessionData(session);
//        topicGroup2.addSessionData(session2);
//        topicGroupsMap.put("TopicGroup two", topicGroup2);
//
//        TopicGroup topicGroup3 = new TopicGroup();
//        topicGroup3.setTitle("TopicGroup three");
//        topicGroup3.setId("3");
//        topicGroup3.addSessionData(session);
//        topicGroup3.addSessionData(session2);
//        topicGroupsMap.put("TopicGroup three", topicGroup3);
//
//        TopicGroup topicGroup4 = new TopicGroup();
//        topicGroup4.setTitle("TopicGroup four");
//        topicGroup4.setId("4");
//        topicGroup4.addSessionData(session);
//        topicGroup4.addSessionData(session2);
//        topicGroupsMap.put("TopicGroup four", topicGroup4);
//
//        TopicGroup topicGroup5 = new TopicGroup();
//        topicGroup5.setTitle("TopicGroup five");
//        topicGroup5.setId("4");
//        topicGroup5.addSessionData(session);
//        topicGroup5.addSessionData(session2);
//        topicGroupsMap.put("TopicGroup five", topicGroup5);
        //end dummy data


        // </editor-fold>


        ArrayList<CollectionView.InventoryGroup> themeGroups = new ArrayList<>();
        ArrayList<CollectionView.InventoryGroup> topicGroups = new ArrayList<>();

       for (TopicGroup topic : mTopics.values()) {
       /* for (TopicGroup topic : topicGroupsMap.values()) {*/
         //  if (DEBUG) Log.d(TAG, topic.getTitle() + ": number of sessions = " + topic.getSessions().size());
            if (topic.getSessions().size() > 0) {
                inventoryGroup = new CollectionView.InventoryGroup(GROUP_ID_TOPIC_CARDS);
                inventoryGroup.addItemWithTag(topic);
                //topic.setTitle(getTranslatedTitle(topic.getTitle(), model));
                topicGroups.add(inventoryGroup);
            }
        }

        Iterator<CollectionView.InventoryGroup> themeIterator = themeGroups.iterator();
        int currentTopicNum = 0;
        for (CollectionView.InventoryGroup topicGroup : topicGroups) {
            inventory.addGroup(topicGroup);
            currentTopicNum++;
//            if (currentTopicNum == topicsPerTheme) {
//                if (themeIterator.hasNext()) {
//                    inventory.addGroup(themeIterator.next());
//                }
//                currentTopicNum = 0;
//            }
        }

        mCollectionView.setCollectionAdapter(this);
        mCollectionView.updateInventory(inventory, false);
    }
// </editor-fold>

    // <editor-fold desc="Loader">
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] mProjection = PandTContract.SESSIONS_PROJECTION;

        final Uri contentUri = PandTContract.Sessions.CONTENT_URI;
        return new CursorLoader(getActivity(), contentUri,
                mProjection, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() <=0 || !data.moveToFirst()) {
            return;
        }
        if (DEBUG) Log.d(TAG,"");
        readDataFromCursor(data);
        updateCollectionView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    // </editor-fold>

    // <editor-fold desc="readDataFromCursor">
    public boolean readDataFromCursor(Cursor cursor/*, QueryEnum query*/)
    {
      //  if (DEBUG) Log.d(TAG, "readDataFromCursor");
        if (cursor != null) {
         //   if (DEBUG) Log.d(TAG, "Reading session data from cursor.");

            Map<String, TopicGroup> topicGroups = new HashMap<>();

            // Iterating through rows in Sessions query.
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SessionData session = new SessionData();
                    populateSessionFromCursorRow(session, cursor);

                    String tags = session.getTags();

                    if (!TextUtils.isEmpty(tags)) {
                        StringTokenizer tagsTokenizer = new StringTokenizer(tags, ",");
                        while (tagsTokenizer.hasMoreTokens()) {
                            String rawTag = tagsTokenizer.nextToken();
                            if (rawTag.startsWith("TOPIC_")) {
                                TopicGroup topicGroup = topicGroups.get(rawTag);
                                if (topicGroup == null) {
                                    topicGroup = new TopicGroup();
                                    topicGroup.setTitle(rawTag);
                                    topicGroup.setId(rawTag);
                                    topicGroups.put(rawTag, topicGroup);
                                }
                                topicGroup.addSessionData(session);

                            }
                        }
                    }
                } while (cursor.moveToNext());
            }

            mTopics = topicGroups;
            return true;
        }
        return false;
    }

    private void populateSessionFromCursorRow(SessionData session, Cursor cursor) {
        session.updateData(
                cursor.getString(cursor.getColumnIndex(
                        PandTContract.Sessions.SESSION_ID)),
                cursor.getString(cursor.getColumnIndex(
                        PandTContract.Sessions.SESSION_TITLE)),
                cursor.getString(cursor.getColumnIndex(
                        PandTContract.Sessions.SESSION_TAGS)),
//                cursor.getString(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_ABSTRACT)),
//                cursor.getString(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_ID)),
//                cursor.getString(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_PHOTO_URL)),
                cursor.getString(cursor.getColumnIndex(
                        PandTContract.Sessions.SESSION_MAIN_TAG))//,
//                cursor.getLong(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_START)),
//                cursor.getLong(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_END)),
//                cursor.getString(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_LIVESTREAM_ID)),
//                cursor.getString(cursor.getColumnIndex(
//                        PandTContract.Sessions.SESSION_YOUTUBE_URL)),

                /*cursor.getLong(cursor.getColumnIndex(
                        PandTContract.Sessions.SESSION_IN_MY_SCHEDULE)) == 1L*/);
    }

    // </editor-fold>

    // <editor-fold desc="CollectionView callbacks">
    @Override
    public View newCollectionHeaderView(Context context, int groupId, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel, Object headerTag) {
Log.d(TAG,"");
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // First inflate the card container.
        int containerLayoutId;
        switch (groupId) {
            case GROUP_ID_TOPIC_CARDS:
            case GROUP_ID_THEME_CARDS:
            case GROUP_ID_LIVE_STREAM_CARD:
                containerLayoutId = R.layout.topic_theme_livestream_card_container;
                break;
            default:
                containerLayoutId = R.layout.card_container;
                break;
        }
        ViewGroup containerView = (ViewGroup)inflater.inflate(containerLayoutId, parent, false);

        ViewGroup containerContents = (ViewGroup)containerView.findViewById(
                R.id.explore_io_card_container_contents);


        // Now inflate the header within the container cards.
        int headerLayoutId = -1;
        switch (groupId) {
            case GROUP_ID_THEME_CARDS:
            case GROUP_ID_TOPIC_CARDS:
            case GROUP_ID_LIVE_STREAM_CARD:
                headerLayoutId = R.layout.card_header_with_button;
                break;
        }
        // Inflate the specified number of items.
        if (headerLayoutId > -1) {
            inflater.inflate(headerLayoutId, containerContents, true);
        }
// Now inflate the items within the container cards.
        int itemLayoutId = -1;
        int numItems = 1;
        switch (groupId) {
            case GROUP_ID_KEYNOTE_STREAM_CARD:
                itemLayoutId = R.layout.keynote_stream_item;
                numItems = 1;
                break;
            case GROUP_ID_THEME_CARDS:
              //  itemLayoutId = R.layout.explore_io_topic_theme_livestream_item;
               // numItems = ExploreModel.getThemeSessionLimit(getContext());
                break;
            case GROUP_ID_TOPIC_CARDS:
                itemLayoutId = R.layout.explore_io_topic_theme_livestream_item;
               // numItems = ExploreModel.getTopicSessionLimit(getContext());
                numItems = 2;//FIXME
                break;
            case GROUP_ID_LIVE_STREAM_CARD:
                //itemLayoutId = R.layout.explore_io_topic_theme_livestream_item;
               // numItems = 3;
                break;
            case GROUP_ID_MESSAGE_CARDS:
              //  itemLayoutId = R.layout.explore_io_message_card_item;
              //  numItems = 1;
                break;
        }
        // Inflate the specified number of items.
        if (itemLayoutId > -1) {
            for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {
                inflater.inflate(itemLayoutId, containerContents, true);
            }
        }

        return containerView;
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {
        if (GROUP_ID_KEYNOTE_STREAM_CARD == groupId ||
                GROUP_ID_MESSAGE_CARDS == groupId) {
            // These two group id types don't have child views.
            populateSubItemInfo(context, view, groupId, tag);
            // Set the object's data into the view's tag so that the click listener on the view can
            // extract it and use the data to handle a click.
            View clickableView = view.findViewById(R.id.explore_io_clickable_item);
            if (clickableView != null) {
                clickableView.setTag(tag);
            }
        } else {
            // These group ids have children who are child items.
            ViewGroup viewWithChildrenSubItems = (ViewGroup)(view.findViewById(
                    R.id.explore_io_card_container_contents));
            ItemGroup itemGroup = (ItemGroup) tag;

            // Set Header tag and title.
            viewWithChildrenSubItems.getChildAt(0).setTag(tag);
            TextView titleTextView = ((TextView) view.findViewById(android.R.id.title));
            View headerView = view.findViewById(R.id.explore_io_card_header_layout);
            if (headerView != null) {
                headerView.setContentDescription(
                        getString(R.string.more_items_button_desc_with_label_a11y,
                                itemGroup.getTitle()));
            }

            // Set the tag on the moreButton so it can be accessed by the click listener.
            View moreButton = view.findViewById(android.R.id.button1);
            if (moreButton != null) {
                moreButton.setTag(tag);
            }
            if (titleTextView != null) {
                titleTextView.setText(itemGroup.getTitle());
            }

            // Skipping first child b/c it is a header view.
            for (int viewChildIndex = 1; viewChildIndex < viewWithChildrenSubItems.getChildCount(); viewChildIndex++) {
                View childView = viewWithChildrenSubItems.getChildAt(viewChildIndex);

                int sessionIndex = viewChildIndex - 1;
                int sessionSize = itemGroup.getSessions().size();
                if (childView != null && sessionIndex < sessionSize) {
                    childView.setVisibility(View.VISIBLE);
                    SessionData sessionData = itemGroup.getSessions().get(sessionIndex);
                    childView.setTag(sessionData);
                    populateSubItemInfo(context, childView, groupId, sessionData);
                } else if (childView != null) {
                    childView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void populateSubItemInfo(Context context, View view, int groupId, Object tag) {
        // Locate the views that may be used to configure the item being bound to this view.
        // Not all elements are used in all views so some will be null.
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
       // Button startButton = (Button) view.findViewById(R.id.buttonStart);
       // Button endButton = (Button) view.findViewById(R.id.buttonEnd);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        // Load item elements common to THEME and TOPIC group cards.
        if (tag instanceof SessionData) {
            SessionData sessionData = (SessionData)tag;
            titleView.setText(sessionData.getSessionName());
//            if (!TextUtils.isEmpty(sessionData.getImageUrl())) {
//                ImageView imageView = (ImageView) view.findViewById(R.id.thumbnail);
//                mImageLoader.loadImage(sessionData.getImageUrl(), imageView);
//            }
//            ImageView inScheduleIndicator =
//                    (ImageView) view.findViewById(R.id.indicator_in_schedule);
//            if (inScheduleIndicator != null) {  // check not keynote
//                inScheduleIndicator.setVisibility(
//                        sessionData.isInSchedule() ? View.VISIBLE : View.GONE);
//            }
            if (!TextUtils.isEmpty(sessionData.getDetails())) {
                descriptionView.setText(sessionData.getDetails());
            }
        }

        // Bind message data if this item is meant to be bound as a message card.
        if (GROUP_ID_MESSAGE_CARDS == groupId) {
//            MessageData messageData = (MessageData)tag;
//            descriptionView.setText(messageData.getMessageString(context));
//            if (messageData.getEndButtonStringResourceId() != -1) {
//                endButton.setText(messageData.getEndButtonStringResourceId());
//            } else {
//                endButton.setVisibility(View.GONE);
//            }
//            if (messageData.getStartButtonStringResourceId() != -1) {
//                startButton.setText(messageData.getStartButtonStringResourceId());
//            } else {
//                startButton.setVisibility(View.GONE);
//            }
//            if (messageData.getIconDrawableId() > 0) {
//                iconView.setVisibility(View.VISIBLE);
//                iconView.setImageResource(messageData.getIconDrawableId());
//            } else {
//                iconView.setVisibility(View.GONE);
//            }
//            if (messageData.getStartButtonClickListener() != null) {
//                startButton.setOnClickListener(messageData.getStartButtonClickListener());
//            }
//            if (messageData.getEndButtonClickListener() != null) {
//                endButton.setOnClickListener(messageData.getEndButtonClickListener());
//            }
        }
    }

    // </editor-fold>

}
