package main.client.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

public final class UserErrorHandler {

    public UserErrorHandler(final Activity context, final String string) {
        this(context, string,null);
    }

    public UserErrorHandler(final Activity context, final String string, final String arg) {
        if(arg == null || arg.isEmpty()) {
            buildDisplay(context, string);
        } else {
            buildDisplay(context, string + " : " + arg);
        }
    }

    private void buildDisplay(final Activity context, final String errorMessage) {
        context.runOnUiThread(new RunnableDisplay(context, errorMessage));
    }

    private final class RunnableDisplay implements Runnable {

        private final String errorMessage;
        private final Context context;

        RunnableDisplay(Context context, String errorMessage) {
            this.context = context;
            this.errorMessage = errorMessage;
        }

        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Erreur");
            builder.setMessage(errorMessage);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}