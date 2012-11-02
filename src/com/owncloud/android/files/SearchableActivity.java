package com.owncloud.android.files;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.owncloud.android.AccountUtils;
import com.owncloud.android.datamodel.FileDataStorageManager;
import com.owncloud.android.datamodel.OCFile;
import com.owncloud.android.ui.activity.FileDetailActivity;
import com.owncloud.android.ui.activity.FileDisplayActivity;
import com.owncloud.android.ui.fragment.FileDetailFragment;

public class SearchableActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account account = AccountUtils.getCurrentOwnCloudAccount(getApplicationContext());
        FileDataStorageManager storage = new FileDataStorageManager(account , getContentResolver());
        OCFile file = storage.getFileByPath(getIntent().getDataString());
        if (file != null) {
            Intent i = null;
            if (!file.isDirectory()) {
                i = new Intent(getApplicationContext(), FileDetailActivity.class);
                i.putExtra(FileDetailFragment.EXTRA_ACCOUNT, account);
                
            } else {
                i = new Intent(getApplicationContext(), FileDisplayActivity.class);
            }
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(FileDetailFragment.EXTRA_FILE, file);
            startActivity(i);
        }
    }
    
    
}
