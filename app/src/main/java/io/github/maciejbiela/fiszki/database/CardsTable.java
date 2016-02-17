package io.github.maciejbiela.fiszki.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CardsTable {

    public static final String TABLE_CARDS = "cards";
    public static final String COLUMN_FROM_LANGUAGE = "FROM_LANGUAGE";
    public static final String COLUMN_TO_LANGUAGE = "TO_LANGUAGE";

    private static final String DATABASE_CREATE = "CREATE TABLE " +
            TABLE_CARDS +
            "(" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FROM_LANGUAGE + " TEXT NOT NULL, " +
            COLUMN_TO_LANGUAGE + " TEXT NOT NULL" +
            ");";

    private static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_CARDS + ";";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }
}
