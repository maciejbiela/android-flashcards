package io.github.maciejbiela.fiszki;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;

import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

public class Card {

    public static Cursor getAll(ContentResolver contentResolver) {
        return contentResolver.query(CardsProvider.CONTENT_URI, null, null, null, null);
    }

    public static Cursor getAllForCategory(ContentResolver contentResolver, String category) {
        String selection = CardsTable.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category.toUpperCase()};
        return contentResolver.query(CardsProvider.CONTENT_URI, null, selection, selectionArgs, null);
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
