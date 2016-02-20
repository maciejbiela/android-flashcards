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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.provider.CardsProvider;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class GuessFragment extends Fragment
        implements OnItemSelectedListener, LoaderCallbacks<Cursor> {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.tv_foreign_language)
    TextView tvForeignLanguage;

    @Bind(R.id.bt_find_out)
    Button btFindOut;

    @Bind(R.id.tv_mother_language)
    TextView tvMotherLanguage;

    @Bind(R.id.bt_knew)
    Button btKnew;

    @Bind(R.id.bt_did_not_know)
    Button btDidNotKnow;

    @Bind(R.id.tv_statistics)
    TextView tvStatistics;

    private static final String EMPTY_STRING = "";
    private long id;
    private int goodAnswers;
    private int totalAnswers;
    private String motherLanguage;
    private String foreignLanguage;

    public GuessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess, container, false);
        ButterKnife.bind(this, view);
        Category.populateSpinner(getContext(), spCategory);
        spCategory.setOnItemSelectedListener(this);
        btFindOut.setOnClickListener(findOutHandler);
        btKnew.setOnClickListener(knewHandler);
        btDidNotKnow.setOnClickListener(didNotKnowHandler);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String category = spCategory.getSelectedItem().toString();
        return Card.getRandomForCategory(getContext(), category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()) {
            extractCard(data);
            tvMotherLanguage.setText(motherLanguage);
            tvForeignLanguage.setText(EMPTY_STRING);
            btFindOut.setEnabled(true);
            setAnswerButtonsVisibility(INVISIBLE);
            hideStatistics();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tvMotherLanguage.setText(EMPTY_STRING);
        tvForeignLanguage.setText(EMPTY_STRING);
        setAnswerButtonsVisibility(INVISIBLE);
    }

    private OnClickListener findOutHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            tvForeignLanguage.setText(foreignLanguage);
            setAnswerButtonsVisibility(VISIBLE);
            setAnswerButtonsEnabled(true);
        }
    };

    private OnClickListener knewHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCardStatistics(true);
            displayStatistics();
            disableNextAnswerForTheSameCard();
        }
    };

    private OnClickListener didNotKnowHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCardStatistics(false);
            displayStatistics();
            disableNextAnswerForTheSameCard();
        }
    };

    private void updateCardStatistics(boolean goodAnswer) {
        totalAnswers++;
        if (goodAnswer) {
            goodAnswers++;
        }
        Uri uri = Uri.withAppendedPath(CardsProvider.CONTENT_URI, String.valueOf(id));
        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, goodAnswers);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, totalAnswers);
        getContext().getContentResolver().update(uri, values, null, null);
    }

    private void displayStatistics() {
        String statistics = "Your statistics for this card: "
                + goodAnswers + "/" + totalAnswers +
                " (good/total)";
        tvStatistics.setText(statistics);
    }

    private void hideStatistics() {
        tvStatistics.setText(EMPTY_STRING);
    }

    private void disableNextAnswerForTheSameCard() {
        btFindOut.setEnabled(false);
        setAnswerButtonsEnabled(false);
    }

    private void setAnswerButtonsVisibility(int visibility) {
        if (visibility == VISIBLE || visibility == INVISIBLE) {
            btKnew.setVisibility(visibility);
            btDidNotKnow.setVisibility(visibility);
        }
    }

    private void setAnswerButtonsEnabled(boolean enabled) {
        btKnew.setEnabled(enabled);
        btDidNotKnow.setEnabled(enabled);
    }

    private void extractCard(Cursor data) {
        id = data.getLong(data.getColumnIndex(CardsTable.COLUMN_ID));
        motherLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_MOTHER_LANGUAGE));
        foreignLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
        goodAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_GOOD_ANSWERS));
        totalAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_TOTAL_ANSWERS));
    }
}
