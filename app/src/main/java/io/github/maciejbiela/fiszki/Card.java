package io.github.maciejbiela.fiszki;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;

import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

public class Card {

    public static Loader<Cursor> getAllForCategory(Context context, String category) {
        String selection = null;
        String[] selectionArgs = null;
        if (!"all".equals(category)) {
            selection = CardsTable.COLUMN_CATEGORY + " = ?";
            selectionArgs = new String[]{category.toUpperCase()};
        }
        return new CursorLoader(context, CardsProvider.CONTENT_URI, null, selection, selectionArgs, null);
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
