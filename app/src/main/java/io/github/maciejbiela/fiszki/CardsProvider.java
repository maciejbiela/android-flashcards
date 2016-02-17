package io.github.maciejbiela.fiszki;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class CardsProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    private static final String DBNAME = "fiszki.db";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String AUTHORITY = "io.github.maciejbiela.fiszki.provider";

    public static final String BASE_PATH = "cards";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    static {
        MATCHER.addURI(AUTHORITY, BASE_PATH, 0);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (MATCHER.match(uri) != 0) {
            return null;
        }
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = "_ID ASC";
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        return db.query(BASE_PATH, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (MATCHER.match(uri) != 0) {
            return null;
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(BASE_PATH, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static final String SQL_CREATE = "CREATE TABLE " +
            "cards " +
            "(" +
            "_ID INTEGER PRIMARY KEY, " +
            "FROM_LANGUAGE TEXT, " +
            "TO_LANGUAGE TEXT )";

    protected static final class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
