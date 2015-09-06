package com.bdevlin.apps.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.IOException;


/**
 * Created by brian on 9/21/2014.
 */
public class LoginAndAuthHelper  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {

    private static final String TAG = LoginAndAuthHelper.class.getSimpleName();
    Context mAppContext;
    Activity mActivity;
    boolean mStarted = false;
    boolean mResolving = false;
    private static boolean sCanShowSignInUi = true;
    private static boolean sCanShowAuthUi = true;

    // Name of the account to log in as (e.g. "foo@example.com")
    String mAccountName;

    // API client to interact with Google services
    private GoogleApiClient mGoogleApiClient;


    // Request codes for the UIs that we show
    private static final int REQUEST_AUTHENTICATE = 100;
    private static final int REQUEST_RECOVER_FROM_AUTH_ERROR = 101;
    private static final int REQUEST_RECOVER_FROM_PLAY_SERVICES_ERROR = 102;
    private static final int REQUEST_PLAY_SERVICES_ERROR_DIALOG = 103;

    // Auth scopes we need
    public static final String AUTH_SCOPES[] = {
            Scopes.PLUS_LOGIN,
           Scopes.DRIVE_FILE//,

           // "https://www.googleapis.com/auth/plus.profile.emails.read"
    };

    static final String AUTH_TOKEN_TYPE;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (String scope : AUTH_SCOPES) {
            sb.append(scope);
            sb.append(" ");
        }
        AUTH_TOKEN_TYPE = sb.toString();
    }

    public interface Callbacks {
        void onPlusInfoLoaded(String accountName);
        void onAuthSuccess(String accountName, boolean newlyAuthenticated);
        void onAuthFailure(String accountName);
    }

    Callbacks mCallbacksRef;
    GetTokenTask mTokenTask = null;

    public LoginAndAuthHelper(Activity activity, Callbacks callbacks, String accountName) {
        Log.d(TAG, "Helper created. Account: " + mAccountName);
       // mActivityRef = new WeakReference<Activity>(activity);
        mCallbacksRef = callbacks;
        mActivity = activity;
        mAppContext = activity.getApplicationContext();
        mAccountName = accountName;

    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public String getAccountName() {
        return mAccountName;
    }


    public void start() {
        if (mStarted) {
            Log.w(TAG, "Helper already started. Ignoring redundant call.");
            return;
        }

        mStarted = true;
        if (mResolving) {
            // if resolving, don't reconnect the plus client
            Log.d(TAG, "Helper ignoring signal to start because we're resolving a failure.");
            return;
        }

        Log.d(TAG, "Helper starting. Connecting [" + mAccountName + "]");
        if (mGoogleApiClient == null) {
            Log.d(TAG, "Creating client.");

            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mActivity);
            for (String scope : AUTH_SCOPES) {
                builder.addScope(new Scope(scope));
            }
            mGoogleApiClient = builder
                    .addApi(Plus.API)
                     .addApi(Drive.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .setAccountName(mAccountName)
                    .build();

//            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
//                    .addApi(Drive.API)
//                    .addScope(Drive.SCOPE_FILE)
//                    .addApi(Plus.API)
//                    .addScope(Plus.SCOPE_PLUS_LOGIN)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .build();
        }
        Log.d(TAG, "Connecting client.");
        // Every time we start we want to try to connect. If it
        // succeeds we'll get an onConnected() callback. If it
        // fails we'll get onConnectionFailed(), with a result!
        mGoogleApiClient.connect();

    }

    public void stop() {
        if (!mStarted) {
            Log.w(TAG, "Helper already stopped. Ignoring redundant call.");
            return;
        }

        Log.d(TAG, "Helper stopping.");
        if (mTokenTask != null) {
            Log.d(TAG, "Helper cancelling token task.");
            mTokenTask.cancel(false);
        }
        mStarted = false;
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Helper disconnecting client.");
            mGoogleApiClient.disconnect();
        }
       mResolving = false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Helper connected, account " + mAccountName);
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.

        // load user's Google+ profile, if we don't have it yet
        if (!GoogleAccountUtils.hasPlusInfo(mActivity, mAccountName)) {
            Log.d(TAG, "We don't have Google+ info for " + mAccountName + " yet, so loading.");
            PendingResult<People.LoadPeopleResult> result = Plus.PeopleApi.load(mGoogleApiClient, "me");
            result.setResultCallback(this);

        } else {
            Log.d(TAG, "No need for Google+ info, we already have it.");
        }

        // try to authenticate, if we don't have a token yet
        if (!GoogleAccountUtils.hasToken(mActivity, mAccountName)) {
            Log.d(TAG, "We don't have auth token for " + mAccountName + " yet, so getting it.");
            mTokenTask = new GetTokenTask();
            mTokenTask.execute();
        } else {
            Log.d(TAG, "No need for auth token, we already have it.");
            reportAuthSuccess(false);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());

        if (connectionResult.hasResolution()) {
            if (sCanShowSignInUi) {
                Log.d(TAG, "onConnectionFailed, with resolution. Attempting to resolve.");
                sCanShowSignInUi = false;
                try {
                    mResolving = true;
                    connectionResult.startResolutionForResult(mActivity,
                            REQUEST_RECOVER_FROM_PLAY_SERVICES_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "SendIntentException occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "onConnectionFailed with resolution but sCanShowSignInUi==false.");
                reportAuthFailure();
            }
            return;
        }

        Log.d(TAG, "onConnectionFailed, no resolution.");
        final int errorCode = connectionResult.getErrorCode();
        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode) && sCanShowSignInUi) {
            sCanShowSignInUi = false;
            GooglePlayServicesUtil.getErrorDialog(errorCode, mActivity,
                    REQUEST_PLAY_SERVICES_ERROR_DIALOG).show();
        } else {
            reportAuthFailure();
        }

    }


    private void showRecoveryDialog(int statusCode) {
//        Activity activity = getActivity("showRecoveryDialog()");
//        if (activity == null) {
//            return;
//        }

        if (sCanShowAuthUi) {
            sCanShowAuthUi = false;
            Log.d(TAG, "Showing recovery dialog for status code " + statusCode);
            final Dialog d = GooglePlayServicesUtil.getErrorDialog(
                    statusCode, mActivity, REQUEST_RECOVER_FROM_PLAY_SERVICES_ERROR);
            d.show();
        } else {
            Log.d(TAG, "Not showing Play Services recovery dialog because sCanShowSignInUi==false.");
            reportAuthFailure();
        }
    }


    private void showAuthRecoveryFlow(Intent intent) {
//        Activity activity = getActivity("showAuthRecoveryFlow()");
//        if (activity == null) {
//            return;
//        }

        if (sCanShowAuthUi) {
            sCanShowAuthUi = false;
            Log.d(TAG, "Starting auth recovery Intent.");
            mActivity.startActivityForResult(intent, REQUEST_RECOVER_FROM_AUTH_ERROR);
        } else {
            Log.d(TAG, "Not showing auth recovery flow because sCanShowSignInUi==false.");
            reportAuthFailure();
        }
    }

    private void reportAuthSuccess(boolean newlyAuthenticated) {
        Log.d(TAG, "Auth success for account " + mAccountName + ", newlyAuthenticated=" + newlyAuthenticated);
        Callbacks callbacks;
        if (null != (callbacks = mCallbacksRef)) {
            callbacks.onAuthSuccess(mAccountName, newlyAuthenticated);
        }
    }

    private void reportAuthFailure() {
        Log.d(TAG, "Auth FAILURE for account " + mAccountName);
        Callbacks callbacks;
        if (null != (callbacks = mCallbacksRef)) {
            callbacks.onAuthFailure(mAccountName);
        }
    }


    // Called asynchronously -- result of loadPeople() call
    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {

        Log.d(TAG, "onPeopleLoaded, status=" + loadPeopleResult.getStatus().toString());
        if (loadPeopleResult.getStatus().isSuccess()) {
            PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
            if (personBuffer != null && personBuffer.getCount() > 0) {
                Log.d(TAG, "Got plus profile for account " + mAccountName);
                Person currentUser = personBuffer.get(0);
                personBuffer.close();

                // Record profile ID, image URL and name
                Log.d(TAG, "Saving plus profile ID: " + currentUser.getId());
                GoogleAccountUtils.setPlusProfileId(mAppContext, mAccountName, currentUser.getId());
                Log.d(TAG, "Saving plus image URL: " + currentUser.getImage().getUrl());
                GoogleAccountUtils.setPlusImageUrl(mAppContext, mAccountName, currentUser.getImage().getUrl());
                Log.d(TAG, "Saving plus display name: " + currentUser.getDisplayName());
                GoogleAccountUtils.setPlusName(mAppContext, mAccountName, currentUser.getDisplayName());
                Person.Cover cover = currentUser.getCover();
                if (cover != null) {
                    Person.Cover.CoverPhoto coverPhoto = cover.getCoverPhoto();
                    if (coverPhoto != null) {
                        Log.d(TAG, "Saving plus cover URL: " + coverPhoto.getUrl());
                        GoogleAccountUtils.setPlusCoverUrl(mAppContext, mAccountName, coverPhoto.getUrl());
                    }
                } else {
                    Log.d(TAG, "Profile has no cover.");
                }

                Callbacks callbacks;
                if (null != (callbacks = mCallbacksRef)) {
                    callbacks.onPlusInfoLoaded(mAccountName);
                }
            } else {
                Log.e(TAG, "Plus response was empty! Failed to load profile.");
            }
        } else {
            Log.e(TAG, "Failed to load plus proflie, error " + loadPeopleResult.getStatus().getStatusCode());
        }

    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "ActivityResult: " + requestCode);

        if (requestCode == REQUEST_AUTHENTICATE ||
                requestCode == REQUEST_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_PLAY_SERVICES_ERROR_DIALOG) {

            Log.d(TAG, "onActivityResult, req=" + requestCode + ", result=" + resultCode);
            if (requestCode == REQUEST_RECOVER_FROM_PLAY_SERVICES_ERROR) {
                mResolving = false;
            }

            if (resultCode == Activity.RESULT_OK) {
                if (mGoogleApiClient != null) {
                    Log.d(TAG, "Since activity result was RESULT_OK, reconnecting client.");
                    mGoogleApiClient.connect();
                } else {
                    Log.d(TAG, "Activity result was RESULT_OK, but we have no client to reconnect.");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "User explicitly cancelled sign-in/auth flow.");
                // save this as a preference so we don't annoy the user again
                //PrefUtils.markUserRefusedSignIn(mAppContext);
            } else {
                Log.w(TAG, "Failed to recover from a login/auth failure, resultCode=" + resultCode);
            }
            return true;
        }
        return false;
    }

    private class GetTokenTask extends AsyncTask<Void, Void, String> {
        public GetTokenTask() {}

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (isCancelled()) {
                    Log.d(TAG, "doInBackground: task cancelled, so giving up on auth.");
                    return null;
                }

                Log.d(TAG, "Starting background auth for " + mAccountName);
                final String token = GoogleAuthUtil.getToken(mAppContext, mAccountName, AUTH_TOKEN_TYPE);

                // Save auth token.
                Log.d(TAG, "Saving token: " + (token == null ? "(null)" : "(length " +
                        token.length() + ")") + " for account "  + mAccountName);
                GoogleAccountUtils.setAuthToken(mAppContext, mAccountName, token);
                return token;
            } catch (GooglePlayServicesAvailabilityException e) {
                postShowRecoveryDialog(e.getConnectionStatusCode());
            } catch (UserRecoverableAuthException e) {
                postShowAuthRecoveryFlow(e.getIntent());
            } catch (IOException e) {
                Log.e(TAG, "IOException encountered: " + e.getMessage());
            } catch (GoogleAuthException e) {
                Log.e(TAG, "GoogleAuthException encountered: " + e.getMessage());
            } catch (RuntimeException e) {
                Log.e(TAG, "RuntimeException encountered: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (isCancelled()) {
                Log.d(TAG, "Task cancelled, so not reporting auth success.");
            } else if (!mStarted) {
                Log.e(TAG, "Activity not started, so not reporting auth success.");
            } else {
                Log.e(TAG, "GetTokenTask reporting auth success.");
                reportAuthSuccess(true);
            }
        }

        private void postShowRecoveryDialog(final int statusCode) {
//            Activity activity = getActivity("postShowRecoveryDialog()");
//            if (activity == null) {
//                return;
//            }

            if (isCancelled()) {
                Log.d(TAG, "Task cancelled, so not showing recovery dialog.");
                return;
            }

            Log.d(TAG, "Requesting display of recovery dialog for status code " + statusCode);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mStarted) {
                        showRecoveryDialog(statusCode);
                    } else {
                        Log.e(TAG, "Activity not started, so not showing recovery dialog.");
                    }
                }
            });
        }

        private void postShowAuthRecoveryFlow(final Intent intent) {
//            Activity activity = getActivity("postShowAuthRecoveryFlow()");
//            if (activity == null) {
//                return;
//           }

            if (isCancelled()) {
                Log.d(TAG, "Task cancelled, so not showing auth recovery flow.");
                return;
            }

           // Log.d(TAG, "Requesting display of auth recovery flow.");

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mStarted) {
                        showAuthRecoveryFlow(intent);
                    } else {
                        Log.e(TAG, "Activity not started, so not showing auth recovery flow.");
                    }
                }
            });
        }
    }




}
