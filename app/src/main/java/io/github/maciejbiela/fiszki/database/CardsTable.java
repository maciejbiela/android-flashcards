package io.github.maciejbiela.fiszki.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CardsTable {

    public static final String TABLE_CARDS = "cards";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_MOTHER_LANGUAGE = "MOTHER_LANGUAGE";
    public static final String COLUMN_FOREIGN_LANGUAGE = "FOREIGN_LANGUAGE";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_GOOD_ANSWERS = "GOOD_ANSWERS";
    public static final String COLUMN_TOTAL_ANSWERS = "TOTAL_ANSWERS";

    private static final String DATABASE_CREATE = "CREATE TABLE " +
            TABLE_CARDS +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MOTHER_LANGUAGE + " TEXT NOT NULL UNIQUE, " +
            COLUMN_FOREIGN_LANGUAGE + " TEXT NOT NULL, " +
            COLUMN_CATEGORY + " TEXT NOT NULL, " +
            COLUMN_GOOD_ANSWERS + " INTEGER DEFAULT 0, " +
            COLUMN_TOTAL_ANSWERS + " INTEGER DEFAULT 0" +
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
