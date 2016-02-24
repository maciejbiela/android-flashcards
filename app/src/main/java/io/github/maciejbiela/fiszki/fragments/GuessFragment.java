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

    @Bind(R.id.bt_next_card_from_category)
    Button btNextCardFromCategory;

    private static final String EMPTY_STRING = "";
    private static final String POSITION = "POSITION";

    private boolean hasData;
    private int previousPosition = 0;
    private State state = State.UNDEFINED;

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
        initLoader();
        if (savedInstanceState != null) {

            read(savedInstanceState);
        } else {

            hasData = false;
        }
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
    public void onSaveInstanceState(Bundle outState) {

        save(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position != previousPosition) {

            restartLoader();
        }
        previousPosition = position;
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

        if (!hasData) {

            presentCurrentData(data);
        } else {

            presentPreviousData();
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
        btNextCardFromCategory.setOnClickListener(nextCardHandler);
    }

    private OnClickListener findOutHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {

            presentAnswer();
            state = State.AFTER_FIND_OUT;
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

    private OnClickListener nextCardHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {

            restartLoader();
        }
    };

    private void clearFields() {

        tvMotherLanguage.setText(EMPTY_STRING);
        tvForeignLanguage.setText(EMPTY_STRING);
        setAnswerButtonsVisibility(INVISIBLE);
        btNextCardFromCategory.setVisibility(INVISIBLE);
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
        btNextCardFromCategory.setVisibility(INVISIBLE);
    }

    private void answerWasSuccessful(boolean successful) {

        updateCardStatistics(successful);
        displayStatistics();
        disableNextAnswerForTheSameCard();
        state = State.AFTER_ANSWER;
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
        btNextCardFromCategory.setVisibility(VISIBLE);
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

    private void presentCurrentData(Cursor data) {

        if (data.moveToNext()) {

            extractCard(data);
            presentWord();
            state = State.AFTER_LOAD;
        } else {

            btFindOut.setEnabled(false);
            clearFields();
        }
    }

    private void presentPreviousData() {

        hasData = false;
        switch (state) {

            case AFTER_LOAD:
                presentWord();
                break;
            case AFTER_FIND_OUT:
                presentWord();
                presentAnswer();
                break;
            case AFTER_ANSWER:
                presentWord();
                presentAnswer();
                displayStatistics();
                disableNextAnswerForTheSameCard();
                break;
        }
    }

    private void extractCard(Cursor data) {

        id = data.getLong(data.getColumnIndex(CardsTable.COLUMN_ID));
        motherLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_MOTHER_LANGUAGE));
        foreignLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
        goodAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_GOOD_ANSWERS));
        totalAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_TOTAL_ANSWERS));
    }

    private enum State {

        AFTER_LOAD(0), AFTER_FIND_OUT(1), AFTER_ANSWER(2), UNDEFINED(-1);

        private int code;

        State(int code) {

            this.code = code;
        }

        static State forCode(int code) {

            switch (code) {

                case 0:
                    return AFTER_LOAD;
                case 1:
                    return AFTER_FIND_OUT;
                case 2:
                    return AFTER_ANSWER;
                default:
                    return UNDEFINED;
            }
        }

        static String key() {
            return "STATE";
        }
    }

    private Loader<Cursor> initLoader() {

        return getLoaderManager().initLoader(0, null, this);
    }

    private void restartLoader() {

        getLoaderManager().restartLoader(0, null, this);
    }


    private void read(Bundle savedInstanceState) {

        hasData = true;
        int code = savedInstanceState.getInt(State.key());
        state = State.forCode(code);
        previousPosition = savedInstanceState.getInt(POSITION);
        motherLanguage = savedInstanceState.getString(CardsTable.COLUMN_MOTHER_LANGUAGE);
        foreignLanguage = savedInstanceState.getString(CardsTable.COLUMN_FOREIGN_LANGUAGE);
        goodAnswers = savedInstanceState.getInt(CardsTable.COLUMN_GOOD_ANSWERS);
        totalAnswers = savedInstanceState.getInt(CardsTable.COLUMN_TOTAL_ANSWERS);
        id = savedInstanceState.getLong(CardsTable.COLUMN_ID);
    }

    private void save(Bundle outState) {

        outState.putInt(State.key(), state.code);
        outState.putInt(POSITION, previousPosition);
        outState.putString(CardsTable.COLUMN_MOTHER_LANGUAGE, motherLanguage);
        outState.putString(CardsTable.COLUMN_FOREIGN_LANGUAGE, foreignLanguage);
        outState.putInt(CardsTable.COLUMN_GOOD_ANSWERS, goodAnswers);
        outState.putInt(CardsTable.COLUMN_TOTAL_ANSWERS, totalAnswers);
        outState.putLong(CardsTable.COLUMN_ID, id);
    }
}
