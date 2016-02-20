package io.github.maciejbiela.fiszki;

import android.app.Fragment;
import android.content.ContentResolver;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewCardFragment extends Fragment {

    @Bind(R.id.et_mother_language)
    EditText etMotherLanguage;

    @Bind(R.id.et_foreign_language)
    EditText etForeignLanguage;

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.bt_save_card)
    Button btSaveCard;

    public AddNewCardFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_card, container, false);
        ButterKnife.bind(this, view);
        setOnClickListeners();
        return view;
    }

    private void setOnClickListeners() {
        btSaveCard.setOnClickListener(saveCardHandler);
    }

    private OnClickListener saveCardHandler = new OnClickListener() {

        @Override
        public void onClick(View v) {
            NewCard newCard = readInputFields();
            if (fieldsNotEmpty(newCard)) {
                add(newCard);
            } else {
                informThatAllFieldsNeedToBeFilled();
            }
        }
    };

    private class NewCard {

        private String motherLanguage;
        private String foreignLanguage;
        private String category;

        public NewCard(String motherLanguage, String foreignLanguage, String category) {

            this.motherLanguage = motherLanguage;
            this.foreignLanguage = foreignLanguage;
            this.category = category;
        }
    }

    private NewCard readInputFields() {

        String motherLanguage = etMotherLanguage.getText().toString();
        String foreignLanguage = etForeignLanguage.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        return new NewCard(motherLanguage, foreignLanguage, category);
    }

    private boolean fieldsNotEmpty(NewCard newCard) {

        return !(TextUtils.isEmpty(newCard.motherLanguage) ||
                TextUtils.isEmpty(newCard.foreignLanguage) ||
                TextUtils.isEmpty(newCard.category));
    }

    private void add(NewCard newCard) {

        ContentResolver contentResolver = getContext().getContentResolver();
        if (Card.add(contentResolver, newCard.motherLanguage, newCard.foreignLanguage, newCard.category)) {

            Log.d("INSERT", "New card created: " + newCard.motherLanguage + " -> " + newCard.foreignLanguage + " : " + newCard.category);
        } else {

            Log.d("INSERT", "Card with this word in mother language already exists!");
            String alertTitle = "Card adding error";
            String alertMessage = "There was an error adding your new card. " +
                    "Please note that you can NOT have multiple cards for the same word in your mother language";
            displayAlert(alertTitle, alertMessage);
        }
    }

    private void informThatAllFieldsNeedToBeFilled() {

        String alertTitle = "Missing value(s)";
        String alertMessage = "Word and its translation must be non-empty. Category needs to be selected";
        displayAlert(alertTitle, alertMessage);
    }

    private void displayAlert(String alertTitle, String alertMessage) {

        AlertHelper.displayAlert(getContext(), alertTitle, alertMessage);
    }
}
