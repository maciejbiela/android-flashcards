package io.github.maciejbiela.fiszki;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseCardsFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.cards)
    ListView cards;

    public BrowseCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_cards, container, false);
        ButterKnife.bind(this, view);
        spCategory.setOnItemSelectedListener(this);
        Cursor cursor = Card.getAll(getContext().getContentResolver());
        setAdapter(cursor);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = spCategory.getSelectedItem().toString();
        Cursor cursor = Card.getAllForCategory(getContext().getContentResolver(), category);
        setAdapter(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setAdapter(Cursor cursor) {
        CardCursorAdapter adapter = new CardCursorAdapter(getContext(), cursor);
        cards.setAdapter(adapter);
    }
}
