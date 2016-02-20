package io.github.maciejbiela.fiszki.fragments;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
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
import io.github.maciejbiela.fiszki.R;
import io.github.maciejbiela.fiszki.database.CardsTable;
import io.github.maciejbiela.fiszki.utils.AlertHelper;
import io.github.maciejbiela.fiszki.utils.CardHelper;
import io.github.maciejbiela.fiszki.utils.StatisticsHelper;

public class EditCardFragment extends Fragment implements LoaderCallbacks<Cursor> {

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

    private long cardId;

    public EditCardFragment() {

        // Required empty public constructor
    }

    public void setCardId(long cardId) {

        this.cardId = cardId;
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
        setOnClickListeners();
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return CardHelper.getCardWithID(getContext(), this.cardId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) {

            UpdatedCard updatedCard = readFieldsFromCursor(data);
            fillFieldsAccordingTo(updatedCard);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        clearFields();
    }

    private void setOnClickListeners() {
        btSaveCard.setOnClickListener(updateCardListener);
    }

    private OnClickListener updateCardListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            UpdatedCard updatedCard = readInputFields();
            if (fieldsNotEmpty(updatedCard)) {

                update(updatedCard);
            } else {

                informThatAllFieldsNeedToBeFilled();
            }
        }
    };

    private void update(UpdatedCard updatedCard) {

        ContentValues values = putValues(updatedCard);
        if (CardHelper.update(getContext().getContentResolver(), cardId, values)) {

            switchBackToPreviousFragment();
        } else {

            displayUpdatingAlert();
        }
    }

    private boolean fieldsNotEmpty(UpdatedCard updatedCard) {

        return !(TextUtils.isEmpty(updatedCard.motherLanguage) ||
                TextUtils.isEmpty(updatedCard.foreignLanguage) ||
                TextUtils.isEmpty(updatedCard.category));
    }

    private UpdatedCard readInputFields() {

        String motherLanguage = etMotherLanguage.getText().toString();
        String foreignLanguage = etForeignLanguage.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        return new UpdatedCard(motherLanguage, foreignLanguage, category, 0, 0);
    }

    private ContentValues putValues(UpdatedCard updatedCard) {

        ContentValues values = new ContentValues();
        values.put(CardsTable.COLUMN_MOTHER_LANGUAGE, updatedCard.motherLanguage);
        values.put(CardsTable.COLUMN_FOREIGN_LANGUAGE, updatedCard.foreignLanguage);
        values.put(CardsTable.COLUMN_CATEGORY, updatedCard.category);
        values.put(CardsTable.COLUMN_GOOD_ANSWERS, updatedCard.goodAnswers);
        values.put(CardsTable.COLUMN_TOTAL_ANSWERS, updatedCard.totalAnswers);
        return values;
    }

    private class UpdatedCard {

        String motherLanguage;
        String foreignLanguage;
        String category;
        int goodAnswers;
        int totalAnswers;

        public UpdatedCard(String motherLanguage, String foreignLanguage, String category, int goodAnswers, int totalAnswers) {

            this.motherLanguage = motherLanguage;
            this.foreignLanguage = foreignLanguage;
            this.category = category;
            this.goodAnswers = goodAnswers;
            this.totalAnswers = totalAnswers;
        }
    }

    private UpdatedCard readFieldsFromCursor(Cursor data) {

        String motherLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_MOTHER_LANGUAGE));
        String foreignLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
        String category = data.getString(data.getColumnIndex(CardsTable.COLUMN_CATEGORY));
        int goodAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_GOOD_ANSWERS));
        int totalAnswers = data.getInt(data.getColumnIndex(CardsTable.COLUMN_TOTAL_ANSWERS));
        return new UpdatedCard(motherLanguage, foreignLanguage, category, goodAnswers, totalAnswers);
    }

    private void fillFieldsAccordingTo(UpdatedCard updatedCard) {

        String statistics = StatisticsHelper.getText(updatedCard.goodAnswers, updatedCard.totalAnswers);
        tvStatistics.setText(statistics);
        etMotherLanguage.setText(updatedCard.motherLanguage);
        etForeignLanguage.setText(updatedCard.foreignLanguage);
        spCategory.setSelection(getSpinnerIndex(updatedCard.category));
    }

    private void clearFields() {
        tvStatistics.setText(EMPTY_STRING);
        etMotherLanguage.setText(EMPTY_STRING);
        etForeignLanguage.setText(EMPTY_STRING);
    }

    private void switchBackToPreviousFragment() {
        BrowseCardsFragment fragment = new BrowseCardsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void displayUpdatingAlert() {

        String alertTitle = "Card updating error";
        String alertMessage = "There was an error while updating your card. " +
                "Please note that you can NOT have multiple cards for the same word in your mother language";
        AlertHelper.displayAlert(getContext(), alertTitle, alertMessage);
    }

    private void informThatAllFieldsNeedToBeFilled() {

        String alertTitle = "Missing value(s)";
        String alertMessage = "Word and its translation must be non-empty. Category needs to be selected";
        displayAlert(alertTitle, alertMessage);
    }

    private void displayAlert(String alertTitle, String alertMessage) {

        AlertHelper.displayAlert(getContext(), alertTitle, alertMessage);
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
