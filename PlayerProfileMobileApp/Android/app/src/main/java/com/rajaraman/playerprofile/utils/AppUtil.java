package com.rajaraman.playerprofile.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.rajaraman.playerprofile.BuildConfig;
import com.rajaraman.playerprofile.R;

public class AppUtil {

    private static ProgressDialog pd = null;

    public final static void logDebugMessage(String tag, String message) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, message);
		}
	}

	public final static void logErrorMessage(String tag, String message) {
		// if (BuildConfig.DEBUG) {
		Log.e(tag, message);
		// }
	}

	public final static void showToastMessage(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

    public final static void showProgressDialog(Context context) {

        pd = new ProgressDialog(context);

        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    public final static void dismissProgressDialog() {
        pd.dismiss();
    }

    public final static void showDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}