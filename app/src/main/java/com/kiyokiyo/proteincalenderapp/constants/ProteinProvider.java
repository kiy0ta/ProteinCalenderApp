package com.kiyokiyo.proteincalenderapp.constants;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.kiyokiyo.proteincalenderapp.util.LogUtil;

import java.util.HashMap;

/**
 * Created by kiyota on 2019/07/14.
 */

public class ProteinProvider extends ContentProvider {

    private static final String TAG = "DbProteinProvider";
    private static final String DATABASE_NAME = "inputs.db";
    private static final String INPUT_TABLE = "input";
    private static final int INPUT_DATABASE_VERSION = 1;
    private static final int MATCH_INPUT = 1;
    private static final int MATCH_INPUT_ID = 2;

    private static HashMap<String, String> sInputProjectionMap;
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ProteinCalendarContract.AUTHORITY, ProteinCalendarContract.PATH_INPUT,
                MATCH_INPUT);
        sUriMatcher.addURI(ProteinCalendarContract.AUTHORITY, ProteinCalendarContract.PATH_INPUT + "/#",
                MATCH_INPUT_ID);

        sInputProjectionMap = new HashMap<String, String>();
        sInputProjectionMap.put(ProteinCalendarContract.Input._ID, ProteinCalendarContract.Input._ID);
        sInputProjectionMap.put(ProteinCalendarContract.Input.RECORD_DATE, ProteinCalendarContract.Input.RECORD_DATE);
        sInputProjectionMap.put(ProteinCalendarContract.Input.RECORD_TYPE, ProteinCalendarContract.Input.RECORD_TYPE);
        sInputProjectionMap.put(ProteinCalendarContract.Input.RECORD_PRICE, ProteinCalendarContract.Input.RECORD_PRICE);
        sInputProjectionMap.put(ProteinCalendarContract.Input.RECORD_BOTTLE, ProteinCalendarContract.Input.RECORD_BOTTLE);
        sInputProjectionMap.put(ProteinCalendarContract.Input.RECORD_PROTEIN, ProteinCalendarContract.Input.RECORD_PROTEIN);

    }

    private SQLiteDatabase mInputDB;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        DbProteinDatabaseHelper dbHelper = new DbProteinDatabaseHelper(mContext);
        mInputDB = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        int matchType = sUriMatcher.match(uri);
        switch (matchType) {
            case MATCH_INPUT:
            case MATCH_INPUT_ID:
                return queryInput(uri, projection, selection, selectionArgs, sortOrder,
                        matchType);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_INPUT:
                return ProteinCalendarContract.Input.CONTENT_TYPE;
            case MATCH_INPUT_ID:
                return ProteinCalendarContract.Input.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_INPUT:
            case MATCH_INPUT_ID:
                return insertInput(uri, values);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int request = sUriMatcher.match(uri);
        switch (request) {
            case MATCH_INPUT:
            case MATCH_INPUT_ID:
                return deleteExternalInput(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = 0;
        int request = sUriMatcher.match(uri);
        switch (request) {
            case MATCH_INPUT:
            case MATCH_INPUT_ID:
                result = updateExternalInput(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (result > 0) {
            notifyChange(uri);
        }
        return result;
    }

    private void notifyChange(Uri uri) {
        mContext.getContentResolver().notifyChange(uri, null);
    }


    private Cursor queryInput(Uri uri, String[] projection, String selection,
                              String[] selectionArgs, String sortOrder, int matchType) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String orderBy;
        switch (matchType) {
            case MATCH_INPUT:
                queryBuilder.setTables(INPUT_TABLE);
                queryBuilder.setProjectionMap(sInputProjectionMap);
                orderBy = ProteinCalendarContract.Input.DEFAULT_SORT_ORDER;
                break;
            case MATCH_INPUT_ID:
                queryBuilder.setTables(INPUT_TABLE);
                queryBuilder.setProjectionMap(sInputProjectionMap);
                queryBuilder.appendWhere(ProteinCalendarContract.Input._ID + "=" + uri.getPathSegments().get(1));
                orderBy = ProteinCalendarContract.Input.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // Use the default sort order only If no sort order is specified.
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }
        Cursor cur = queryBuilder
                .query(mInputDB, projection, selection, selectionArgs, null, null, orderBy);
        // Tell the cursor what URI to watch, so it knows when its source data changes.
        cur.setNotificationUri(mContext.getContentResolver(), uri);
        return cur;
    }

    private Uri insertInput(Uri uri, ContentValues values) {
        long rowId = mInputDB.insert(INPUT_TABLE, null, values);
        if (rowId <= 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }
        return ContentUris.withAppendedId(ProteinCalendarContract.Input.CONTENT_URI, rowId);
    }

    private int deleteExternalInput(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String rowId;
        int request = sUriMatcher.match(uri);
        switch (request) {
            case MATCH_INPUT:
                count = mInputDB.delete(INPUT_TABLE, selection, selectionArgs);
                break;
            case MATCH_INPUT_ID:
                rowId = uri.getPathSegments().get(1);
                selection = ProteinCalendarContract.Input._ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : "");
                count = mInputDB.delete(INPUT_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }

    private int updateExternalInput(Uri uri, ContentValues values, String selection,
                                    String[] selectionArgs) {
        String rowId;
        int count = 0;
        int request = sUriMatcher.match(uri);
        switch (request) {
            case MATCH_INPUT:
                count = mInputDB
                        .update(INPUT_TABLE, values, selection, selectionArgs);
                break;
            case MATCH_INPUT_ID:
                rowId = uri.getPathSegments().get(1);
                selection = ProteinCalendarContract.Input._ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : "");
                count = mInputDB
                        .update(INPUT_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }


    /**
     * helperクラス
     */
    private static class DbProteinDatabaseHelper extends SQLiteOpenHelper {

        private DbProteinDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, INPUT_DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + INPUT_TABLE + " (" + ProteinCalendarContract.Input._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + ProteinCalendarContract.Input.RECORD_DATE
                    + " TEXT," + ProteinCalendarContract.Input.RECORD_TYPE
                    + " TEXT," + ProteinCalendarContract.Input.RECORD_PRICE + " TEXT,"
                    + ProteinCalendarContract.Input.RECORD_BOTTLE + " TEXT,"
                    + ProteinCalendarContract.Input.RECORD_PROTEIN + " TEXT " + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtil.d(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        }

    }
}
