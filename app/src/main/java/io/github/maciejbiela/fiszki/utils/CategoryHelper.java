package io.github.maciejbiela.fiszki.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import io.github.maciejbiela.fiszki.R;

public class CategoryHelper {

    public static final String ALL = "all";

    public static void populateSpinner(Context context, Spinner spinner) {

        ArrayAdapter<String> adapter = getAdapterForSpinner(context);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static ArrayAdapter<String> getAdapterForSpinner(Context context) {

        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoriesWithAll(context));
    }

    private static String[] categoriesWithAll(Context context) {

        String[] categories = context.getResources().getStringArray(R.array.categories);
        String[] categoriesWithAll = new String[categories.length + 1];
        categoriesWithAll[0] = ALL;
        System.arraycopy(categories, 0, categoriesWithAll, 1, categories.length);
        return categoriesWithAll;
    }
}
