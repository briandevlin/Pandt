package com.bdevlin.apps.authenticator;

import android.accounts.AccountAuthenticatorResponse;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brian on 10/13/2014.
 */
public class SetupData implements Parcelable {

    public static final String EXTRA_SETUP_DATA = "com.android.email.setupdata";

    // NORMAL is the standard entry from the Email app; EAS and POP_IMAP are used when entering via
    // Settings -> Accounts
    public static final int FLOW_MODE_UNSPECIFIED = -1;
    public static final int FLOW_MODE_NORMAL = 0;
    public static final int FLOW_MODE_ACCOUNT_MANAGER = 1;
    public static final int FLOW_MODE_EDIT = 3;
    public static final int FLOW_MODE_FORCE_CREATE = 4;
    // The following two modes are used to "pop the stack" and return from the setup flow.  We
    // either return to the caller (if we're in an account type flow) or go to the message list
    public static final int FLOW_MODE_RETURN_TO_CALLER = 5;
    public static final int FLOW_MODE_RETURN_TO_MESSAGE_LIST = 6;
    public static final int FLOW_MODE_RETURN_NO_ACCOUNTS_RESULT = 7;
    public static final int FLOW_MODE_NO_ACCOUNTS = 8;

    public static final int CHECK_INCOMING = 1;
    public static final int CHECK_OUTGOING = 2;
    public static final int CHECK_AUTODISCOVER = 4;

    private int mFlowMode = FLOW_MODE_NORMAL;
    private String mFlowAccountType;
    private String mUsername;
    private String mPassword;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;


    public interface SetupDataContainer {
        public SetupData getSetupData();
        public void setSetupData(SetupData setupData);
    }

    public SetupData() {
        mUsername = null;
        mPassword = null;
    }

    public SetupData(int flowMode) {
        this();
        mFlowMode = flowMode;
    }

    public SetupData(int flowMode, String accountType) {
        this(flowMode);
        mFlowAccountType = accountType;
    }

//    public SetupData(int flowMode, Account account) {
//        this(flowMode);
//        mAccount = account;
//    }

    public int getFlowMode() {
        return mFlowMode;
    }

    public String getFlowAccountType() {
        return mFlowAccountType;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }


    public AccountAuthenticatorResponse getAccountAuthenticatorResponse() {
        return mAccountAuthenticatorResponse;
    }

    public void setAccountAuthenticatorResponse(AccountAuthenticatorResponse response) {
        mAccountAuthenticatorResponse = response;
    }


    public static final Parcelable.Creator<SetupData> CREATOR =
            new Parcelable.Creator<SetupData>() {
                @Override
                public SetupData createFromParcel(Parcel in) {
                    return new SetupData(in);
                }

                @Override
                public SetupData[] newArray(int size) {
                    return new SetupData[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mFlowMode);
        //dest.writeParcelable(mAccount, 0);
        dest.writeString(mUsername);
        dest.writeString(mPassword);
       // dest.writeInt(mCheckSettingsMode);
       // dest.writeInt(mAllowAutodiscover ? 1 : 0);
       // dest.writeParcelable(mPolicy, 0);
        dest.writeParcelable(mAccountAuthenticatorResponse, 0);
    }

    public SetupData(Parcel in) {
       // final ClassLoader loader = getClass().getClassLoader();
        mFlowMode = in.readInt();
       // mAccount = in.readParcelable(loader);
        mUsername = in.readString();
        mPassword = in.readString();
       // mCheckSettingsMode = in.readInt();
       // mAllowAutodiscover = in.readInt() == 1;
       // mPolicy = in.readParcelable(loader);
      //  mAccountAuthenticatorResponse = in.readParcelable(loader);
    }

}
