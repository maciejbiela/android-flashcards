package io.github.maciejbiela.fiszki.adapter;

import android.content.Context;
import android.widget.SimpleCursorAdapter;

import io.github.maciejbiela.fiszki.R;
import io.github.maciejbiela.fiszki.database.CardsTable;

public class CardSimpleCursorAdapter extends SimpleCursorAdapter {

    public CardSimpleCursorAdapter(Context context) {

        super(context,
                R.layout.item_card,
                null,
                FROM,
                TO,
                0);
    }

    private static final String[] FROM = new String[]{

            CardsTable.COLUMN_MOTHER_LANGUAGE,
            CardsTable.COLUMN_FOREIGN_LANGUAGE,
            CardsTable.COLUMN_CATEGORY
    };

    private static final int[] TO = new int[]{

            R.id.tv_mother_language,
            R.id.tv_foreign_language,
            R.id.tv_category
    };
}
