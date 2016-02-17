package io.github.maciejbiela.fiszki;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewCardFragment extends Fragment
        implements OnClickListener {

    @Bind(R.id.from_language)
    EditText fromLanguage;

    @Bind(R.id.to_language)
    EditText toLanguage;

    @Bind(R.id.save_card)
    Button saveCard;

    public AddNewCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_card, container, false);
        ButterKnife.bind(this, view);
        saveCard.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("TEST", "I'm beeing clicked");
    }
}
