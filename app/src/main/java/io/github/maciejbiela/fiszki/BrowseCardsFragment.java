package io.github.maciejbiela.fiszki;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowseCardsFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.lv_cards)
    ListView lvCards;

    SimpleCursorAdapter adapter;

    public BrowseCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_cards, container, false);
        ButterKnife.bind(this, view);
        populateCategorySpinner();
        spCategory.setOnItemSelectedListener(this);
        adapter = new CardSimpleCursorAdapter(getContext());
        lvCards.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
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
        return Card.getAllForCategory(getContext(), category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void populateCategorySpinner() {
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] categoriesWithAll = new String[categories.length + 1];
        categoriesWithAll[0] = "all";
        System.arraycopy(categories, 0, categoriesWithAll, 1, categories.length);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoriesWithAll);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }
}
