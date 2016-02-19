package io.github.maciejbiela.fiszki;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Category {

    public static void populateSpinner(Context context, Spinner spinner) {
        ArrayAdapter<String> adapter = getAdapterForSpinner(context);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static ArrayAdapter<String> getAdapterForSpinner(Context context) {
        String[] categories = context.getResources().getStringArray(R.array.categories);
        String[] categoriesWithAll = new String[categories.length + 1];
        categoriesWithAll[0] = "all";
        System.arraycopy(categories, 0, categoriesWithAll, 1, categories.length);
        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoriesWithAll);
    }
}
