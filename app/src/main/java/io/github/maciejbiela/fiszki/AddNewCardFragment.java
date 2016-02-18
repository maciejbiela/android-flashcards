package io.github.maciejbiela.fiszki;


import android.app.Fragment;
import android.content.ContentResolver;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewCardFragment extends Fragment
        implements OnClickListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_card, container, false);
        ButterKnife.bind(this, view);
        btSaveCard.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String motherLanguage = etMotherLanguage.getText().toString();
        String foreignLanguage = etForeignLanguage.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        ContentResolver contentResolver = getContext().getContentResolver();

        if (Card.add(contentResolver, motherLanguage, foreignLanguage, category)) {
            Log.d("INSERT", "New card created: " + motherLanguage + " -> " + foreignLanguage + " : " + category);
        } else {
            Log.d("INSERT", "Card with this word in mother language already exists!");
        }
    }
}
