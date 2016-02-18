package io.github.maciejbiela.fiszki;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.database.CardsTable;

public class CardCursorAdapter extends CursorAdapter {

    @Bind(R.id.tv_mother_language)
    TextView tvMotherLanguage;

    @Bind(R.id.tv_foreign_language)
    TextView tvForeignLanguage;

    @Bind(R.id.tv_category)
    TextView tvCategory;

    public CardCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String key = cursor.getString(cursor.getColumnIndex(CardsTable.COLUMN_MOTHER_LANGUAGE));
        String value = cursor.getString(cursor.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
        String category = cursor.getString(cursor.getColumnIndex(CardsTable.COLUMN_CATEGORY));
        tvMotherLanguage.setText(key);
        tvForeignLanguage.setText(value);
        tvCategory.setText(category);
    }
}
