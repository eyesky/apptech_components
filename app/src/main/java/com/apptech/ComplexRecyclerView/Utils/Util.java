package com.apptech.ComplexRecyclerView.Utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.view.Window;

import com.apptech.apptechcomponents.R;

import java.io.File;

/**
 * Created by nirob on 7/2/17.
 */

public class Util {

    public static final String PATH = "/Json/image/";

    // check internet connection available or not
    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public static String generateSDCardLink(String destinationpath, String filename) {
        String filePath = null;
        try {
            String StoragePath = Environment.getExternalStorageDirectory() + destinationpath;
            File sdStorageDir = new File(StoragePath);
            if (!sdStorageDir.exists()) {
                sdStorageDir.mkdirs();
            }
            filePath = sdStorageDir.toString() + "/" + filename;
        } catch (Exception e) {
            return null;
        }
        return filePath;
    }


    public static Dialog downloadDialog(Context context) {
        Dialog dialog;
        dialog = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_lang_popup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//this line transparent layout side bg
        return dialog;
    }

    /*






    public ProgressDialog downloadProgress(String msg, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.alert_status));
        progressDialog.setMessage(msg + 0.0 + "%");
        progressDialog.setMessage(msg + 0.0 + "%");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }*/
}
