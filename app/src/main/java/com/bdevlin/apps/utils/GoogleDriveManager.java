package com.bdevlin.apps.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.bdevlin.apps.utils.GoogleAccountManager;
import com.bdevlin.apps.utils.PlayServicesUtils;
import com.bdevlin.apps.utils.VolleyController;
import com.bdevlin.apps.utils.LoginAndAuthHelper;
//import com.bdevlin.apps.utils.MyVolley;
import com.bdevlin.apps.utils.Utils;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by brian on 11/8/2014.
 */
public class GoogleDriveManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = GoogleDriveManager.class.getSimpleName();
    private final Context mContext;
    private String mNextPageToken;


    // API client to interact with Google services
    private GoogleApiClient mGoogleApiClient;

    public GoogleDriveManager(Context context){
        Log.d(TAG, "GoogleDriveManager created. ");
        mContext = context;

        if (mGoogleApiClient == null) {
            Log.d(TAG, "Creating client.");

            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mContext);
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }


    // <editor-fold desc="Google Drive">


    private void ListFolder()
    {
        // retrieve the results for the next page.
        Query query = new Query.Builder()
                .setPageToken(mNextPageToken)
                .build();
        Drive.DriveApi.query(getGoogleApiClient(), query)
                .setResultCallback(metadataBufferCallback);

    }

    private final ResultCallback<DriveApi.MetadataBufferResult> metadataBufferCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                       // showMessage("Problem while retrieving files");
                        return;
                    }

                    mNextPageToken = result.getMetadataBuffer().getNextPageToken();
                    //mHasMore = mNextPageToken != null;

                    // showMessage("Problem while retrieving files " + result.getMetadataBuffer().getMetadata().describeContents());
                }
            };

    private void AddFolder()
    {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("New folder").build();
        Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(
                getGoogleApiClient(), changeSet).setResultCallback(folderCallback);

    }




    final private ResultCallback<DriveApi.ContentsResult> contentsCallback = new
            ResultCallback<DriveApi.ContentsResult>() {
                @Override
                public void onResult(DriveApi.ContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();
                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, result.getContents())
                            .setResultCallback(fileCallback);
                }
            };



    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    showMessage("Created a file: " + result.getDriveFile().getDriveId());
                }
            };


    private void AddDriveFile() {
        Drive.DriveApi.newContents(getGoogleApiClient())
                .setResultCallback(contentsCallback);
    }

    final ResultCallback<DriveFolder.DriveFolderResult> folderCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            showMessage("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    // </editor-fold>


    public void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }


}
