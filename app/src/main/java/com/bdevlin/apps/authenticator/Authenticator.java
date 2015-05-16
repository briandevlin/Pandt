package com.bdevlin.apps.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import com.bdevlin.apps.Config;
import com.bdevlin.apps.pandt.R;


public class Authenticator extends AbstractAccountAuthenticator {

    /** The tag used to log to adb console. **/
    private static final String TAG = Authenticator.class.getSimpleName();

    /**
     * Authenticator should only be instantiated by AuthenticatorService so this context will be for that, not the application
     * actually making the request.
     **/
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.v(TAG, "editProperties()");
        throw new UnsupportedOperationException();
    }

    // Don't add additional accounts
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse response,
            String accountType,
            String authTokenType,
            String[] requiredFeatures,
            Bundle options) throws NetworkErrorException {

        Bundle b = new Bundle();

        final Intent intent =
                AuthenticatorActivity.actionGetCreateAccountIntent(mContext,
                        accountType);
        //  The activity needs to return the final result when it is complete
        // so the Intent should contain the AccountAuthenticatorResponse as KEY_ACCOUNT_MANAGER_RESPONSE.
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        //If the authenticator needs information from the user to satisfy the request then it will
        // create an Intent to an activity that will prompt the user for the information and then carry out the request.
        // This intent must be returned in a Bundle as key KEY_INTENT.
        b.putParcelable(AccountManager.KEY_INTENT, intent);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return b;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse response, Account account, Bundle options) {
        Log.v(TAG, "confirmCredentials()");
        return null;
    }
    // Getting an authentication token is not supported
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle loginOptions) throws NetworkErrorException {
        Log.v(TAG, "getAuthToken()");

        String authToken = "";

        // If we get an authToken - we return it
       // if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
       // }

    }
    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        // null means we don't support multiple authToken types
        Log.v(TAG, "getAuthTokenLabel()");
        return null;
    }

    // Updating user credentials is not supported
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                    String authTokenType, Bundle loginOptions) throws NetworkErrorException {
        Log.v(TAG, "updateCredentials()");
        throw new UnsupportedOperationException();
    }


    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse response, Account account, String[] features) {
        // This call is used to query whether the Authenticator supports
        // specific features. We don't expect to get called, so we always
        // return false (no) for any queries.
        Log.v(TAG, "hasFeatures()");
        final Bundle result = new Bundle();
        //result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }



    /**
     * Helper function that will check to see if pAccountType is one we handle or not.
     *
     * @param pAccountType
     *            The account type to test.
     * @throws UnSupportedAccountTypeException
     *             is thrown if the provided account type is not one that is handled by this Authenticator. Included is
     *             a failure bundle that can be returned by most of the functions in this class as a failure case.
     */
    private void ValidateAccountType(String pAccountType) throws UnSupportedAccountTypeException
    {
        Log.v(TAG, "ValidateAccountType() " + pAccountType);
        if (!pAccountType.equals(Config.ACCOUNT_TYPE))
        {
            Log.v(TAG, "Failed to ValidateAccountType()");
            throw new UnSupportedAccountTypeException();
        }
    }


    /**
     * Abstract exception for fail cases. Deals with building the failure bundle which can be returned from most
     * AbstractAccountAuthenticator functions.
     */
    private abstract class AuthenticatorException extends Exception
    {
        private static final long serialVersionUID = 1L;

        private Bundle mFailureBundle;

        protected AuthenticatorException(int pErrorCode, int pErrorMessageStringResourceID)
        {
            mFailureBundle = new Bundle();
            mFailureBundle.putInt(AccountManager.KEY_ERROR_CODE, pErrorCode);
            mFailureBundle.putString(AccountManager.KEY_ERROR_MESSAGE, mContext.getResources().getString(pErrorMessageStringResourceID));
        }

        public Bundle GetFailureBundle()
        {
            return mFailureBundle;
        }
    }

    /**
     * Exception to throw when a provided auth token type is not supported by this authenticator.
     */
    private class UnsupportedAuthTokenTypeException extends AuthenticatorException
    {
        private static final long serialVersionUID = 1L;

        public UnsupportedAuthTokenTypeException()
        {
            super(AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, R.string.error_unsupported_auth_token_type);
        }
    }

    /**
     * Exception to throw when a provided account type is not supported by this authenticator.
     */
    private class UnSupportedAccountTypeException extends AuthenticatorException
    {
        private static final long serialVersionUID = 1L;

        public UnSupportedAccountTypeException()
        {
            super(AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, R.string.error_unsupported_account_type);
        }
    }
}