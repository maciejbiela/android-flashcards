package io.github.maciejbiela.fiszki;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.provider.CardsProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseCardsFragment extends Fragment {

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
        Cursor cursor = getContext().getContentResolver().query(CardsProvider.CONTENT_URI, null, null, null, null);
        CardCursorAdapter adapter = new CardCursorAdapter(getContext(), cursor);
        cards.setAdapter(adapter);
        return view;
    }

}
