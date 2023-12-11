package com.example.capstone;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class TransactionLoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    TransactionLoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    void loadingAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading_transaction, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
