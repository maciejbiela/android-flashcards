package io.github.maciejbiela.fiszki.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertHelper {

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";

    private static final DialogInterface.OnClickListener DISMISSER = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
        }
    };

    public static void displayAlertOK(Context context, String alertTitle, String alertMessage) {

        displayAlertOK(context, alertTitle, alertMessage, DISMISSER);
    }

    public static void displayAlertOK(Context context, String alertTitle, String alertMessage, DialogInterface.OnClickListener okListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, OK, okListener);
        alertDialog.show();
    }

    public static void displayAlertOKCancel(Context context, String alertTitle, String alertMessage, DialogInterface.OnClickListener okListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, OK, okListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, CANCEL, DISMISSER);
        alertDialog.show();
    }
}
