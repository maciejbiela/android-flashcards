package io.github.maciejbiela.fiszki.fragments;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
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
import io.github.maciejbiela.fiszki.R;
import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.utils.CardHelper;
import io.github.maciejbiela.fiszki.utils.CategoryHelper;
import io.github.maciejbiela.fiszki.utils.StatisticsHelper;

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
        setUpCategorySpinner();
        setOnClickListeners();
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
        return CardHelper.getRandomForCategory(getContext(), category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToNext()) {

            extractCard(data);
            presentWord();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        clearFields();
    }

    private void setUpCategorySpinner() {

        CategoryHelper.populateSpinner(getContext(), spCategory);
        spCategory.setOnItemSelectedListener(this);
    }

    private void setOnClickListeners() {

        btFindOut.setOnClickListener(findOutHandler);
        btKnew.setOnClickListener(knewHandler);
        btDidNotKnow.setOnClickListener(didNotKnowHandler);
    }

    private OnClickListener findOutHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {

            presentAnswer();
        }
    };

    private OnClickListener knewHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {

            answerWasSuccessful(true);
        }
    };

    private OnClickListener didNotKnowHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {

            answerWasSuccessful(false);
        }
    };

    private void clearFields() {

        tvMotherLanguage.setText(EMPTY_STRING);
        tvForeignLanguage.setText(EMPTY_STRING);
        setAnswerButtonsVisibility(INVISIBLE);
    }

    private void presentAnswer() {

        tvForeignLanguage.setText(foreignLanguage);
        setAnswerButtonsVisibility(VISIBLE);
        setAnswerButtonsEnabled(true);
    }

    private void presentWord() {

        tvMotherLanguage.setText(motherLanguage);
        tvForeignLanguage.setText(EMPTY_STRING);
        btFindOut.setEnabled(true);
        setAnswerButtonsVisibility(INVISIBLE);
        hideStatistics();
    }

    private void answerWasSuccessful(boolean successful) {

        updateCardStatistics(successful);
        displayStatistics();
        disableNextAnswerForTheSameCard();
    }

    private void updateCardStatistics(boolean goodAnswer) {

        totalAnswers++;
        if (goodAnswer) {

            goodAnswers++;
        }
        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, goodAnswers);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, totalAnswers);
        CardHelper.update(getContext().getContentResolver(), id, values);
    }

    private void displayStatistics() {

        String statistics = StatisticsHelper.getText(goodAnswers, totalAnswers);
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