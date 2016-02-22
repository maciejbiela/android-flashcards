package io.github.maciejbiela.fiszki.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fiszki.db";

    private static final int DATABASE_VERSION = 1;

    public CardsDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        CardsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        CardsTable.onUpgrade(db);
    }
}
