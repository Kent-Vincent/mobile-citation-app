package com.example.capstone;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
public class QRLoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    QRLoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    void loadingAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading_qr, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
