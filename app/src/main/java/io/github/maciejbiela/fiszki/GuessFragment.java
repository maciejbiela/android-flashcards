package io.github.maciejbiela.fiszki;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.maciejbiela.fiszki.database.CardsTable;

public class GuessFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    @Bind(R.id.tv_foreign_language)
    TextView tvForeignLanguage;

    @Bind(R.id.bt_find_out)
    Button btFindOut;

    Handler handler = new Handler(Looper.getMainLooper());

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
        Category.populateSpinner(getContext(), spCategory);
        spCategory.setOnItemSelectedListener(this);
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
        return Card.getRandomForCategory(getContext(), category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()) {
            final String foreignLanguage = data.getString(data.getColumnIndex(CardsTable.COLUMN_FOREIGN_LANGUAGE));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvForeignLanguage.setText(foreignLanguage);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvForeignLanguage.setText("");
            }
        });
    }
}
