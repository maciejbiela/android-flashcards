package io.github.maciejbiela.fiszki;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;

import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

public class Card {

    public static boolean add(ContentResolver contentResolver,
                              String inMotherLanguage,
                              String inForeignLanguage,
                              String category) {
        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_FROM_LANGUAGE, inMotherLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_TO_LANGUAGE, inForeignLanguage.toUpperCase());
        values.put(CardsTable.COLUMN_CATEGORY, category.toUpperCase());
        try {
            contentResolver.insert(CardsProvider.CONTENT_URI, values);
        } catch (SQLiteConstraintException e) {
            return false;
        }
        return true;
    }
}
