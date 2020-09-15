package com.hst.spv.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Admin on 15-09-2020.
 */

public class ProgressDialogHelper {
    private Context context;
    private ProgressDialog pDialog;

    public ProgressDialogHelper(Context context) {
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    public void showProgressDialog(String message) {
        pDialog.setMessage(message);
        if (!((Activity) context).isFinishing() && !pDialog.isShowing()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void cancelProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
