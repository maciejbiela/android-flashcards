package io.github.maciejbiela.fiszki.utils;

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

import static io.github.maciejbiela.fiszki.utils.CategoryHelper.ALL;

public class CardHelper {

    public static final String RANDOM_SORT_ORDER = "RANDOM()";
    public static final String SELECTION_PLACEHOLDER = " = ?";
    public static final String ONE_ROW = "1";

    public static Loader<Cursor> getCardWithID(Context context, long id) {

        return new CursorLoader(context, cardUri(id), null, null, null, null);
    }

    public static Loader<Cursor> getAllForCategory(Context context, String category) {

        return getAllForCategory(context, category, null, null);
    }

    public static Loader<Cursor> getRandomForCategory(Context context, String category) {

        String sortOrder = RANDOM_SORT_ORDER;
        Pair<String, String> queryParameter = new Pair<>(CardsProvider.LIMIT_QUERY_PARAMETER, ONE_ROW);
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
        return add(contentResolver, values);
    }

    public static boolean update(ContentResolver contentResolver, long cardId, ContentValues values) {

        Set<String> columns = values.keySet();
        capitalizeValues(values, columns);
        return isUpdateSuccessful(contentResolver, values, cardUri(cardId));
    }

    public static boolean delete(ContentResolver contentResolver, long id) {

        return delete(contentResolver, cardUri(id));
    }

    public static boolean deleteAll(ContentResolver contentResolver) {

        return delete(contentResolver, CardsProvider.CONTENT_URI);
    }

    public static boolean resetStatistics(ContentResolver contentResolver, long cardId) {

        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, 0);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, 0);
        if (contentResolver.update(cardUri(cardId), values, null, null) != 0) {

            return true;
        }
        return false;
    }

    private static Loader<Cursor> getAllForCategory(Context context,
                                                    String category,
                                                    String sortOrder,
                                                    Pair<String, String> queryParameter) {

        Uri uri = CardsProvider.CONTENT_URI;

        boolean all = ALL.equals(category);
        String selection = all ? null : CardsTable.COLUMN_CATEGORY + SELECTION_PLACEHOLDER;
        String[] selectionArgs = all ? null : new String[]{category.toUpperCase()};

        if (queryParameter != null) {

            String key = queryParameter.first;
            String value = queryParameter.second;
            uri = uri.buildUpon()
                    .appendQueryParameter(key, value)
                    .build();
        }
        return new CursorLoader(context, uri, null, selection, selectionArgs, sortOrder);
    }

    private static boolean add(ContentResolver contentResolver, ContentValues values) {
        try {

            contentResolver.insert(CardsProvider.CONTENT_URI, values);
        } catch (SQLiteConstraintException e) {

            return false;
        }
        return true;
    }

    private static boolean delete(ContentResolver contentResolver, Uri uri) {

        if (contentResolver.delete(uri, null, null) != 0) {

            return true;
        }
        return false;
    }

    private static Uri cardUri(long cardId) {

        return Uri.withAppendedPath(CardsProvider.CONTENT_URI, String.valueOf(cardId));
    }

    private static boolean isUpdateSuccessful(ContentResolver contentResolver, ContentValues values, Uri uri) {

        return contentResolver.update(uri, values, null, null) != -1;
    }

    private static void capitalizeValues(ContentValues values, Set<String> columns) {

        capitalizeValue(values, columns, CardsTable.COLUMN_MOTHER_LANGUAGE);
        capitalizeValue(values, columns, CardsTable.COLUMN_FOREIGN_LANGUAGE);
        capitalizeValue(values, columns, CardsTable.COLUMN_CATEGORY);
    }

    private static void capitalizeValue(ContentValues values, Set<String> columns, String columnName) {

        if (columns.contains(columnName)) {

            String value = (String) values.get(columnName);
            values.put(columnName, value.toUpperCase());
        }
    }
}
