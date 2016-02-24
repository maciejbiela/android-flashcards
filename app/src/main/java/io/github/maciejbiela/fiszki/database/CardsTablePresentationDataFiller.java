package io.github.maciejbiela.fiszki.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class CardsTablePresentationDataFiller {

    public static void fillWithData(SQLiteDatabase db) {

        insert(db, generate("CAT", "KOT", "ANIMALS", 12, 14));
        insert(db, generate("DOG", "PIES", "ANIMALS", 7, 10));
        insert(db, generate("MOUSE", "MYSZ", "ANIMALS", 3, 15));
        insert(db, generate("FOOT", "NOGA", "BODY PARTS", 2, 3));
        insert(db, generate("FINGER", "PALEC", "BODY PARTS", 1, 2));
        insert(db, generate("EYE", "OKO", "BODY PARTS", 3, 3));
        insert(db, generate("MEETING", "SPOTKANIE", "BUSINESS", 0, 2));
        insert(db, generate("SALARY", "PENSJA", "BUSINESS", 3, 6));
        insert(db, generate("OFFICE", "BIURO", "BUSINESS", 2, 5));
        insert(db, generate("RED", "CZERWONY", "COLORS", 14, 20));
        insert(db, generate("GREEN", "NIEBIESKI", "COLORS", 4, 8));
        insert(db, generate("BLUE", "ZIELONY", "COLORS", 11, 23));
        insert(db, generate("COUGH", "KASZEL", "HEALTH", 0, 0));
        insert(db, generate("SICK LEAVE", "ZWOLNIENIE LEKARSKIE", "HEALTH", 1, 6));
        insert(db, generate("DOCTOR", "LEKARZ", "HEALTH", 2, 5));
        insert(db, generate("ART", "SZTUKA", "SCHOOL", 3, 5));
        insert(db, generate("CLASS", "LEKCJA", "SCHOOL", 2, 10));
        insert(db, generate("TEACHER", "NAUCZYCIEL", "SCHOOL", 1, 9));
        insert(db, generate("RUNNING", "BIEGANIE", "SPORTS", 11, 15));
        insert(db, generate("SKI", "NARTY", "SPORTS", 7, 7));
        insert(db, generate("SWIMMING POOL", "BASEN", "SPORTS", 21, 50));
    }

    private static ContentValues generate(String motherLanguage,
                                          String foreignLanguage,
                                          String category,
                                          int goodAnswers,
                                          int totalAnswers) {

        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, motherLanguage);
        values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, foreignLanguage);
        values.put(CardsTable.COLUMN_CATEGORY, category);
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, goodAnswers);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, totalAnswers);
        return values;
    }

    private static void insert(SQLiteDatabase db, ContentValues values) {

        db.insert(CardsTable.TABLE_CARDS, null, values);
    }
}
