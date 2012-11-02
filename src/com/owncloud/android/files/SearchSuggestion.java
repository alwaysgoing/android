package com.owncloud.android.files;

import java.io.File;

import com.owncloud.android.DisplayUtils;
import com.owncloud.android.db.ProviderMeta.ProviderTableMeta;
import com.owncloud.android.ui.activity.FileDetailActivity;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;

public class SearchSuggestion extends ContentProvider {


    private static final String[] COLUMNS = {
        "_id", // must include this column
        SearchManager.SUGGEST_COLUMN_TEXT_1,
        SearchManager.SUGGEST_COLUMN_TEXT_2,
        SearchManager.SUGGEST_COLUMN_INTENT_DATA,
        SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
        SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
        SearchManager.SUGGEST_COLUMN_ICON_1};
    

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (selectionArgs.length == 0 || TextUtils.isEmpty(selectionArgs[0])) return null;
        
        MatrixCursor c = new MatrixCursor(COLUMNS);
        Cursor fileCursor = getContext().getContentResolver().query(ProviderTableMeta.CONTENT_URI,
                                                                    null,
                                                                    ProviderTableMeta.FILE_NAME + " like ?",
                                                                    new String[]{"%"+selectionArgs[0]+"%"},
                                                                    ProviderTableMeta.DEFAULT_SORT_ORDER);
        int row = 0;
        if (fileCursor.moveToFirst()) {
            do {
                c.addRow(createRow(new Integer(row),
                                   fileCursor.getString(fileCursor.getColumnIndex(ProviderTableMeta.FILE_PATH)),
                                   fileCursor.getString(fileCursor.getColumnIndex(ProviderTableMeta.FILE_ACCOUNT_OWNER)),
                                   fileCursor.getString(fileCursor.getColumnIndex(ProviderTableMeta.FILE_CONTENT_TYPE))));
                row++;
            } while(fileCursor.moveToNext());
        }
        fileCursor.close();
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    
    private Object[] createRow(Integer id, String fullpath, String account, String mimetype) {
        File f = new File(fullpath);

        return new Object[] { id, // _id
                f.getName(), // text1
                f.getParent(), // text2
                fullpath, // intent data
                FileDetailActivity.ACTION_DISPLAY_FILE, // action
                SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT,
                DisplayUtils.mimetypeToResourceID(mimetype)};
    }
    

}
