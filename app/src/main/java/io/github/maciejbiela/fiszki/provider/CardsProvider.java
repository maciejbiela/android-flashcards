package io.github.maciejbiela.fiszki.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import io.github.maciejbiela.fiszki.database.CardsDatabaseHelper;
import io.github.maciejbiela.fiszki.database.CardsTable;

import static io.github.maciejbiela.fiszki.database.CardsTable.COLUMN_ID;
import static io.github.maciejbiela.fiszki.database.CardsTable.TABLE_CARDS;

public class CardsProvider extends ContentProvider {

    private CardsDatabaseHelper databaseHelper;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "io.github.maciejbiela.fiszki.provider";
    private static final String PATH = "cards";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    public static final String LIMIT_QUERY_PARAMETER = "LIMIT";

    private static final int CARDS = 10;
    private static final int CARD_ID = 20;

    static {

        MATCHER.addURI(AUTHORITY, PATH, CARDS);
        MATCHER.addURI(AUTHORITY, PATH + "/#", CARD_ID);
    }

    @Override
    public boolean onCreate() {

        databaseHelper = new CardsDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (MATCHER.match(uri)) {

            case CARDS:
                if (TextUtils.isEmpty(sortOrder)) {

                    sortOrder = "_ID ASC";
                }
                String limit = uri.getQueryParameter(LIMIT_QUERY_PARAMETER);
                return db.query(TABLE_CARDS, projection, selection, selectionArgs, null, null, sortOrder, limit);

            case CARD_ID:
                String id = uri.getLastPathSegment();
                selection = CardsTable.COLUMN_ID + " = ?";
                selectionArgs = new String[]{id};
                return db.query(TABLE_CARDS, projection, selection, selectionArgs, null, null, sortOrder);

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (!isCardsURI(uri)) {

            return null;
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insertOrThrow(TABLE_CARDS, null, values);
        return Uri.parse(PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        if (isUnknownURI(uri)) {

            return 0;
        }
        if (isCardIdURI(uri)) {
            String cardId = uri.getLastPathSegment();
            selection = COLUMN_ID + " = ?";
            selectionArgs = new String[]{cardId};
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.delete(TABLE_CARDS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (!isCardIdURI(uri)) {

            return 0;
        }
        String id = uri.getLastPathSegment();
        selection = CardsTable.COLUMN_ID + " = ?";
        selectionArgs = new String[]{id};
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.update(TABLE_CARDS, values, selection, selectionArgs);
    }

    private boolean isCardsURI(Uri uri) {

        return MATCHER.match(uri) == CARDS;
    }

    private boolean isCardIdURI(Uri uri) {

        return MATCHER.match(uri) == CARD_ID;
    }

    private boolean isUnknownURI(Uri uri) {

        return !isCardsURI(uri) && !isCardIdURI(uri);
    }
}
