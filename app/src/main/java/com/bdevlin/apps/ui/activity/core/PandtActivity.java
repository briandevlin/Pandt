package com.bdevlin.apps.ui.activity.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bdevlin.apps.Config;
import com.bdevlin.apps.pandt.Controllers.ActionBarController;
import com.bdevlin.apps.pandt.Controllers.ActivityController;
import com.bdevlin.apps.pandt.Controllers.ControllableActivity;
import com.bdevlin.apps.utils.ViewMode;
//import com.bdevlin.mymodule.lib.gcm.ServerUtilities;
import com.bdevlin.apps.ui.fragments.MainContentFragment;
import com.bdevlin.apps.ui.fragments.NavigationDrawerFragment;
import com.bdevlin.apps.utils.Utils;

import com.google.android.gcm.GCMRegistrar;


/* The main  activity, which is used on both the tablet and the phone.
 *
 * Because this activity is device agnostic, so most of the UI aren't owned by this, but by
 * the UIController.
 */
public class PandtActivity extends AppCompatActivity implements ControllableActivity {

    // <editor-fold desc="Fields">
    private static final String TAG = PandtActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    /**
     * The activity controller to which we delegate most Activity lifecycle events.
     */
    private ActivityController mUIController;
    private ViewMode mViewMode;
    private AsyncTask<Void, Void, Void> mGCMRegisterTask;


    // </editor-fold>

    // <editor-fold desc="life cycle methods">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"Starting PandtActivity");
        if (isFinishing()) {
            return;
        }
        // just testing the lib module
        //Intent intent = new Intent(this, Libactivity.class);

//        if (!PandtAccountUtils.isAuthenticated(this)) {
//            PandtAccountUtils.startAuthenticationFlow(this, getIntent());
//			finish();
//		}
        mViewMode = new ViewMode();
        final boolean tabletUi = Utils.useTabletUI(this.getResources());

        // determine the controller to use based on tabletUi value
        mUIController = ControllerFactory
                .forActivity(this, mViewMode,  tabletUi);

        // on one pane controller this sets the main view, sets up the navigation drawer and the toolbar
        // and loads the main content fragment
        // on two pane controller? who knows
        mUIController.onCreate(savedInstanceState);


        if (savedInstanceState == null && Utils.isNetworkConnected(this)) {
            Log.d(TAG,"Trigger refresh");
            // triggerRefresh();
           // registerGCMClient();
        }

        if (savedInstanceState != null) {
           //
        } else {
            final Intent intent = getIntent();

            }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUIController.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        mUIController.onActivityResult(requestCode, resultCode, data);
    }

  //  @Override
    public Context getActivityContext() {
        return this;
    }
    // </editor-fold>

    // <editor-fold desc="ControllableActivity implementations">

    @Override
    public ActionBarController getActionBarController() {
        return mUIController;
    }

    @Override
    public NavigationDrawerFragment.NavigationDrawerCallbacks getNavigationDrawerCallbacks() {
        return mUIController;
    }

    @Override
    public MainContentFragment.MainContentCallbacks getMainContentCallbacks() {
        return mUIController;
    }

    @Override
    public void onBackPressed() {
        // this is the system back pressed
        if (!mUIController.onBackPressed(true)) {
            super.onBackPressed();
        }
    }

    @Override
    public void createBackStack(Intent intent) {
        mUIController.createBackStack(intent);
    }

    // </editor-fold>

    // <editor-fold desc="Life Cycle">

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        mUIController.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mUIController.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        mUIController.onStop();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
         mUIController.onPostCreate(savedInstanceState);

    }

    // </editor-fold>

    // <editor-fold desc="option menus">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mUIController.onCreateOptionsMenu(menu)
                || super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return mUIController.onPrepareOptionsMenu(menu)
                || super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Toast.makeText(this, "Selected Item: " + item.getTitle(),
        // Toast.LENGTH_SHORT).show();

        return mUIController.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        mUIController.onSaveInstanceState(outState);

    }

    /**
     * Default implementation returns a null view mode.
     */
    @Override
    public ViewMode getViewMode() {
        return mViewMode;
    }



    // </editor-fold>

    // <editor-fold desc="ControllerFactory">

    public static class ControllerFactory {

        /**
         * Create the appropriate type of ActivityController.
         *
         * @return the appropriate {@link ActivityController} to control
         * {@link PandtActivity}.
         */

        public static ActivityController forActivity(PandtActivity activity, ViewMode mViewMode, boolean isTabletDevice) {

            return isTabletDevice ? new UIControllerTwoPane(activity, mViewMode)
                    : new UIControllerOnePane(activity, mViewMode);

        }
    }

    private void registerGCMClient() {
        GCMRegistrar.checkDevice(this);

        // if (BuildConfig.DEBUG) {
        GCMRegistrar.checkManifest(this);
        // }

        final String regId = GCMRegistrar.getRegistrationId(this);

        if ((TextUtils.isEmpty(regId))) {

            try {
                Log.d(TAG, "registering device (regId = " + regId + ")");
                // Automatically registers application on startup.
                GCMRegistrar.register(this, Config.GCM_SENDER_ID);
                Log.d(TAG, "registered device (regId = " + regId + ")");
            } catch (Exception e) {
                Log.d(TAG, "GCM registration error", e);
            }

        } else {
            Log.d(TAG,
                    "Device is already registered on GCM, check server. ");
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration
                Log.d(TAG, "Already registered on the GCM server");

            } else {
                // Try to register again, but not on the UI thread.
                // It's also necessary to cancel the task in onDestroy().
                mGCMRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.d(TAG, "ServerUtilities.register");
                        //boolean registered = ServerUtilities.register(
                        //        HomeActivity.this, regId);
                        boolean registered = true;
                        if (!registered) {
                            // At this point all attempts to register with the
                            // app
                            // server failed, so we need to unregister the
                            // device
                            // from GCM - the app will try to register again
                            // when
                            // it is restarted. Note that GCM will send an
                            // unregistered callback upon completion, but
                            // GCMIntentService.onUnregistered() will ignore it.
                            GCMRegistrar.unregister(PandtActivity.this);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mGCMRegisterTask = null;
                    }
                };
                mGCMRegisterTask.execute(null, null, null);
            }
        }
    }

    // </editor-fold>
    public void sessionDetailItemClicked(View viewClicked) {
        Log.d(TAG, "clicked: " + viewClicked + " " +
                ((viewClicked != null) ? viewClicked.getTag() : ""));
//        Object tag = null;
//        if (viewClicked != null) {
//            tag = viewClicked.getTag();
//        }
//        if (tag instanceof SessionData) {
//            SessionData sessionData = (SessionData)viewClicked.getTag();
//            if (!TextUtils.isEmpty(sessionData.getSessionId())) {
//                Intent intent = new Intent(getApplicationContext(), SessionDetailActivity.class);
//                Uri sessionUri = ScheduleContract.Sessions.buildSessionUri(sessionData.getSessionId());
//                intent.setData(sessionUri);
//                startActivity(intent);
//            } else {
//                LOGE(TAG, "Theme item clicked but session data was null:" + sessionData);
//                Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
//            }
//        }
    }
    public void cardHeaderClicked(View viewClicked) {
        Log.d(TAG, "clicked: " + viewClicked + " " +
                ((viewClicked != null) ? viewClicked.getTag() : ""));
//        View moreButton = viewClicked.findViewById(android.R.id.button1);
//        Object tag = moreButton != null ? moreButton.getTag() : null;
//        Intent intent = new Intent(getApplicationContext(), ExploreSessionsActivity.class);
//        if (tag instanceof LiveStreamData) {
//            intent.setData(ScheduleContract.Sessions.buildSessionsAfterUri(UIUtils.getCurrentTime(this)));
//            intent.putExtra(ExploreSessionsActivity.EXTRA_SHOW_LIVE_STREAM_SESSIONS, true);
//        } else if (tag instanceof ItemGroup) {
//            intent.putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, ((ItemGroup)tag).getId());
//        }
//        startActivity(intent);
    }

}
