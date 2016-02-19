package io.github.maciejbiela.fiszki;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuessFragment extends Fragment {

    @Bind(R.id.sp_category)
    Spinner spCategory;

    public GuessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess, container, false);
        ButterKnife.bind(this, view);
        Category.populateSpinner(getContext(), spCategory);
        return view;
    }
}
