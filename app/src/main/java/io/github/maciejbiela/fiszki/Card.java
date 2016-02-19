package io.github.maciejbiela.fiszki;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.util.Pair;

import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

public class Card {

    private static Loader<Cursor> getAllForCategory(Context context, String category, String sortOrder, Pair<String, String> queryParameter) {
        Uri uri = CardsProvider.CONTENT_URI;
        String selection = null;
        String[] selectionArgs = null;
        if (!"all".equals(category)) {
            selection = CardsTable.COLUMN_CATEGORY + " = ?";
            selectionArgs = new String[]{category.toUpperCase()};
        }
        if (queryParameter != null) {
            uri = uri.buildUpon()
                    .appendQueryParameter(queryParameter.first, queryParameter.second)
                    .build();
        }
        return new CursorLoader(context, uri, null, selection, selectionArgs, sortOrder);
    }

    public static Loader<Cursor> getAllForCategory(Context context, String category) {
        return getAllForCategory(context, category, null, null);
    }

    public static Loader<Cursor> getRandomForCategory(Context context, String category) {
        String sortOrder = "RANDOM()";
        Pair<String, String> queryParameter = new Pair<>(CardsProvider.LIMIT_QUERY_PARAMETER, "1");
        return getAllForCategory(context, category, sortOrder, queryParameter);
    }

    public static boolean add(ContentResolver contentResolver,
                              String inMotherLanguage,
                              String inForeignLanguage,
                              String category) {
        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, inMotherLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, inForeignLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_CATEGORY, category.toUpperCase());
        try {
            contentResolver.insert(CardsProvider.CONTENT_URI, values);
        } catch (SQLiteConstraintException e) {
            return false;
        }
        return true;
    }
}
