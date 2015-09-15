package com.bdevlin.apps.ui.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdevlin.apps.pandt.R;
import com.bdevlin.apps.pandt.chooseAccountActivity;
import com.bdevlin.apps.provider.MockUiProvider;

import java.util.Arrays;
import java.util.List;

/**
 * Created by brian on 10/26/2014.
 */
public class ChooseAccountFragment extends ListFragment {
    private static final String TAG = ChooseAccountFragment.class
            .getSimpleName();

    private static final int REQUEST_AUTHENTICATE = 1;
    private AccountManager mAccountManager;
    private chooseAccountActivity mActivity;
   // private AccountListAdapter mAccountListAdapter;
    private AuthenticatorListAdapter mAuthenticatorListAdapter;
    private AuthenticatorDescription[] mAuthenticatorDescs;
    private Callback mCallback = EmptyCallback.INSTANCE;

    public ChooseAccountFragment() {
    }

    public interface Callback {

        public void reloadAccountsList();

    }

    private static class EmptyCallback implements Callback {
        public static final Callback INSTANCE = new EmptyCallback();

        @Override
        public void reloadAccountsList() {
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (chooseAccountActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAccountList("");
    }

    public void reloadAccountList(String type) {
        if (mAuthenticatorListAdapter != null) {
            mAuthenticatorListAdapter = null;
        }
        if (getActivity() == null) return;

        mAccountManager = AccountManager.get(getActivity());

        //Account[] accounts = am.getAccountsByType(type);
       // Account[] accounts = mAccountManager.getAccounts();
        mAuthenticatorDescs = mAccountManager.getAuthenticatorTypes();

        mAuthenticatorListAdapter = new AuthenticatorListAdapter(getActivity(),
                Arrays.asList(mAuthenticatorDescs));
        setListAdapter(mAuthenticatorListAdapter);
    }


    // @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_login_choose_account, container, false);
//        TextView descriptionView = (TextView) rootView
//                .findViewById(R.id.choose_account_intro);
//        descriptionView.setText(Html
//                .fromHtml(getString(R.string.description_choose_account)));

        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // from the options menu we started and intent to add an account
        // we the get here when finished
        if (requestCode == REQUEST_AUTHENTICATE) {
            if (resultCode == mActivity.RESULT_OK) {
                // tryAuthenticate();
            } else {
                // go back to previous step
//	                mHandler.post(new Runnable() {
//	                    @Override
//	                    public void run() {
//	                        getSupportFragmentManager().popBackStack();
//	                    }
//	                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_account) {
            Intent addAccountIntent = new Intent(Settings.ACTION_ADD_ACCOUNT);
            addAccountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            addAccountIntent.putExtra(Settings.EXTRA_AUTHORITIES,
                    new String[] { MockUiProvider.AUTHORITY });
            startActivityForResult(addAccountIntent, REQUEST_AUTHENTICATE);



//			AccountManager acm = AccountManager.get(getActivity());
//		      acm.addAccount("com.devlin.pmos.pmossync", Constants.AUTHTOKEN_TYPE, null, null, null, null, null);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//
//    public class AccountListAdapter extends ArrayAdapter<Account> {
//        private final int LAYOUT_RESOURCE = R.layout.account_list_item;
//
//        public AccountListAdapter(Context context, List<Account> accounts) {
//            super(context, R.layout.account_list_item, accounts);
//        }
//
//        public class ListItemViewHolder {
//            TextView name;
//            ImageView icon;
//            Account account;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // A ListItemViewHolder keeps references to children views to avoid
//            // unneccessary calls
//            // to findViewById() on each row.
//            ListItemViewHolder holder;
//
//            // When convertView is not null, we can reuse it directly, there is
//            // no need
//            // to reinflate it. We only inflate a new View when the convertView
//            // supplied
//            // by ListView is null.
//            if (convertView == null) {
//                convertView = getLayoutInflater(null).inflate(LAYOUT_RESOURCE,
//                        null);
//
//                // Creates a ListItemViewHolder and store references to the children
//                // views
//                // we want to bind data to.
//                holder = new ListItemViewHolder();
//                holder.name = (TextView) convertView
//                        .findViewById(R.id.accounts_tester_account_name);
//                holder.icon = (ImageView) convertView
//                        .findViewById(R.id.accounts_tester_account_type_icon);
//
//                convertView.setTag(holder);
//            } else {
//                // Get the ListItemViewHolder back to get fast access to the TextView
//                holder = (ListItemViewHolder) convertView.getTag();
//            }
//
//            final Account account = getItem(position);
//
//            if (account != null) {
//                holder.name.setText(account.name);
//                holder.account = account;
//                holder.icon.setVisibility(View.INVISIBLE);
//
//                // now we use the auth descriptors to find an icon for the type
//                for (AuthenticatorDescription desc : mAccountManager.getAuthenticatorTypes()) {
//                    if (desc.type.equals(account.type)) {
//                        final String packageName = desc.packageName;
//                        try {
//                            final Context authContext = getContext()
//                                    .createPackageContext(packageName, 0);
//                            holder.icon.setImageDrawable(authContext
//                                    .getResources().getDrawable(desc.iconId));
//                            holder.icon.setVisibility(View.VISIBLE);
//                        } catch (PackageManager.NameNotFoundException e) {
//                            Log.d(TAG, "error getting the Package Context for "
//                                    + packageName, e);
//                        }
//                    }
//                }
//            } else {
//                holder.name.setText("");
//            }
//
//            return convertView;
//        }
//    }

    public class AuthenticatorListAdapter extends ArrayAdapter<AuthenticatorDescription> {
        private final int LAYOUT_RESOURCE = R.layout.account_list_item;

        public AuthenticatorListAdapter(Context context, List<AuthenticatorDescription> accounts) {
            super(context, R.layout.account_list_item, accounts);
        }

        public class ViewHolder {
            TextView name;
            ImageView icon;
            Account account;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // A ListItemViewHolder keeps references to children views to avoid
            // unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is
            // no need
            // to reinflate it. We only inflate a new View when the convertView
            // supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = getLayoutInflater(null).inflate(LAYOUT_RESOURCE,
                        null);

                // Creates a ListItemViewHolder and store references to the children
                // views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.name = (TextView) convertView
                        .findViewById(R.id.accounts_tester_account_name);
                holder.icon = (ImageView) convertView
                        .findViewById(R.id.accounts_tester_account_type_icon);

                convertView.setTag(holder);
            } else {
                // Get the ListItemViewHolder back to get fast access to the TextView
                holder = (ViewHolder) convertView.getTag();
            }

            final AuthenticatorDescription account = getItem(position);

            if (account != null) {
                holder.name.setText(account.type);
               // holder.account = account;
                holder.icon.setVisibility(View.INVISIBLE);

                // now we use the auth descriptors to find an icon for the type
                for (AuthenticatorDescription desc : mAccountManager.getAuthenticatorTypes()) {
                    if (desc.type.equals(account.type)) {
                        final String packageName = desc.packageName;
                        try {
                            final Context authContext = getContext()
                                    .createPackageContext(packageName, 0);
                            holder.icon.setImageDrawable(authContext
                                    .getResources().getDrawable(desc.iconId));
                            holder.icon.setVisibility(View.VISIBLE);
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.d(TAG, "error getting the Package Context for "
                                    + packageName, e);
                        }
                    }
                }
            } else {
                holder.name.setText("");
            }

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        chooseAccountActivity activity = (chooseAccountActivity) getActivity();

        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork == null || !activeNetwork.isConnected()) {
//            Toast.makeText(activity, R.string.no_connection_cant_login,
//                    Toast.LENGTH_SHORT).show();
            return;
        }

//        activity.mCancelAuth = false;
        activity.mChosenDescription = mAuthenticatorListAdapter.getItem(position);
//        activity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, new AuthProgressFragment(),
//                        "loading").addToBackStack("choose_account").commit();
//
//        // This is the critical method to start our authentication for the account type
        activity.getAuthToken();
    }
}
