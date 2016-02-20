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

import java.util.Set;

import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

public class Card {

    public static Loader<Cursor> getCardWithID(Context context, long id) {
        Uri uri = Uri.withAppendedPath(CardsProvider.CONTENT_URI, String.valueOf(id));
        return new CursorLoader(context, uri, null, null, null, null);
    }

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
                              String motherLanguage,
                              String foreignLanguage,
                              String category) {
        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, motherLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, foreignLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_CATEGORY, category.toUpperCase());
        try {
            contentResolver.insert(CardsProvider.CONTENT_URI, values);
        } catch (SQLiteConstraintException e) {
            return false;
        }
        return true;
    }

    public static boolean update(ContentResolver contentResolver, long id, ContentValues values) {
        Set<String> columns = values.keySet();
        if (columns.contains(CardsTable.COLUMN_MOTHER_LANGUAGE)) {
            String motherLanguage = (String) values.get(CardsTable.COLUMN_MOTHER_LANGUAGE);
            values.remove(CardsTable.COLUMN_MOTHER_LANGUAGE);
            values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, motherLanguage.toUpperCase());
        }
        if (columns.contains(CardsTable.COLUMN_FOREIGN_LANGUAGE)) {
            String foreignLanguage = (String) values.get(CardsTable.COLUMN_FOREIGN_LANGUAGE);
            values.remove(CardsTable.COLUMN_FOREIGN_LANGUAGE);
            values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, foreignLanguage.toUpperCase());
        }
        if (columns.contains(CardsTable.COLUMN_CATEGORY)) {
            String category = (String) values.get(CardsTable.COLUMN_CATEGORY);
            values.remove(CardsTable.COLUMN_CATEGORY);
            values.put(CardsTable.COLUMN_CATEGORY, category.toUpperCase());
        }
        Uri uri = Uri.withAppendedPath(CardsProvider.CONTENT_URI, String.valueOf(id));
        return contentResolver.update(uri, values, null, null) != -1;
    }
}
