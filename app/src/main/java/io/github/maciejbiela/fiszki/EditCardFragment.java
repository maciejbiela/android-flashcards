package io.github.maciejbiela.fiszki;


import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;
import io.github.maciejbiela.fiszki.utils.CardHelper;
import io.github.maciejbiela.fiszki.utils.StatisticsHelper;

public class EditCardFragment extends Fragment implements LoaderCallbacks<Cursor>, OnClickListener {

    @Bind(R.id.tv_statistics)
    TextView tvStatistics;
    @Bind(R.id.et_mother_language)
    EditText etMotherLanguage;

    @Bind(R.id.et_foreign_language)
    EditText etForeignLanguage;

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.bt_save_card)
    Button btSaveCard;

    private static final String EMPTY_STRING = "";

    private long id;

    public EditCardFragment() {
        // Required empty public constructor
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);
        ButterKnife.bind(this, view);
        btSaveCard.setOnClickListener(this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CardHelper.getCardWithID(getContext(), this.id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String motherLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_MOTHER_LANGUAGE));
            String foreignLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
            String category = data.getString(data.getColumnIndex(CardsTable.COLUMN_CATEGORY));
            int goodAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_GOOD_ANSWERS));
            int totalAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_TOTAL_ANSWERS));
            String statistics = StatisticsHelper.getText(goodAnswers, totalAnswers);
            tvStatistics.setText(statistics);
            etMotherLanguage.setText(motherLanguage);
            etForeignLanguage.setText(foreignLanguage);
            spCategory.setSelection(getSpinnerIndex(category));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tvStatistics.setText(EMPTY_STRING);
        etMotherLanguage.setText(EMPTY_STRING);
        etForeignLanguage.setText(EMPTY_STRING);
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.withAppendedPath(CardsProvider.CONTENT_URI, String.valueOf(id));
        ContentValues values = new ContentValues();

        String motherLanguage = etMotherLanguage.getText().toString();
        String foreignLanguage = etForeignLanguage.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, motherLanguage);
        values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, foreignLanguage);
        values.put(CardsTable.COLUMN_CATEGORY, category);
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, 0);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, 0);

        if (CardHelper.update(getContext().getContentResolver(), id, values)) {
            BrowseCardsFragment fragment = new BrowseCardsFragment();

            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }

    private int getSpinnerIndex(String category) {
        String[] categories = getResources().getStringArray(R.array.categories);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return i;
            }
        }
        return -1;
    }
}
