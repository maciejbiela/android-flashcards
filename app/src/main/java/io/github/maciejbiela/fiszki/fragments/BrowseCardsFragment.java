package io.github.maciejbiela.fiszki.fragments;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.R;
import io.github.maciejbiela.fiszki.adapter.CardSimpleCursorAdapter;
import io.github.maciejbiela.fiszki.utils.AlertHelper;
import io.github.maciejbiela.fiszki.utils.CardHelper;
import io.github.maciejbiela.fiszki.utils.CategoryHelper;

public class BrowseCardsFragment extends Fragment
        implements OnItemSelectedListener, LoaderCallbacks<Cursor>, OnClickListener {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.lv_cards)
    ListView lvCards;

    @Bind(R.id.bt_clear_cards)
    Button btClearCards;

    SimpleCursorAdapter adapter;

    public BrowseCardsFragment() {

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

        View view = inflater.inflate(R.layout.fragment_browse_cards, container, false);
        ButterKnife.bind(this, view);
        setUpOnClickListeners();
        setUpCategorySpinner();
        setUpCardsListView();
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
        return CardHelper.getAllForCategory(getContext(), category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {

        displayDeleteWarning();
    }

    private void setUpOnClickListeners() {

        btClearCards.setOnClickListener(this);
    }

    private void setUpCategorySpinner() {

        CategoryHelper.populateSpinner(getContext(), spCategory);
        spCategory.setOnItemSelectedListener(this);
    }

    private void setUpCardsListView() {

        adapter = new CardSimpleCursorAdapter(getContext());
        lvCards.setAdapter(adapter);
        lvCards.setOnItemClickListener(cardSelectedHandler);
    }

    private OnItemClickListener cardSelectedHandler = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            EditCardFragment fragment = new EditCardFragment();
            fragment.setCardId(id);
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    };

    private void displayDeleteWarning() {

        final BrowseCardsFragment currentFragment = this;
        String title = "Careful!";
        String message = "You are about to delete all your cards.\n" +
                "Please note that this operation is irreversible.\n" +
                "Press Cancel to keep your cards.\n" +
                "Pressing OK will proceed with deleting all cards.";
        DialogInterface.OnClickListener deleteHandler = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                CardHelper.deleteAll(getContext().getContentResolver());
                getLoaderManager().restartLoader(0, null, currentFragment);
            }
        };
        AlertHelper.displayAlertOKCancel(getContext(), title, message, deleteHandler);
    }
}
