package com.hst.spv.dialogfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hst.spv.R;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.utils.SPVConstants;

/**
 * Created by Admin on 15-09-2020.
 */

public class SimpleAlertDialogFragment extends DialogFragment {

    private int tag;
    DialogClickListener dialogActions;

    public static SimpleAlertDialogFragment newInstance(int title, String message) {
        SimpleAlertDialogFragment frag = new SimpleAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(SPVConstants.ALERT_DIALOG_TITLE, title);
        args.putString(SPVConstants.ALERT_DIALOG_MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    public static SimpleAlertDialogFragment newInstance(int title, String message, int tag) {
        SimpleAlertDialogFragment frag = new SimpleAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(SPVConstants.ALERT_DIALOG_TITLE, title);
        args.putString(SPVConstants.ALERT_DIALOG_MESSAGE, message);
        args.putInt(SPVConstants.ALERT_DIALOG_TAG, tag);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogActions = (DialogClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement DialogClickListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String message = args.getString(SPVConstants.ALERT_DIALOG_MESSAGE, "");
        int title = args.getInt(SPVConstants.ALERT_DIALOG_TITLE);
        tag = args.getInt(SPVConstants.ALERT_DIALOG_TAG, 0);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_button_ok, mListener).create();
    }

    DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                dialog.cancel();
                if (SimpleAlertDialogFragment.this.dialogActions != null)
                    SimpleAlertDialogFragment.this.dialogActions
                            .onAlertPositiveClicked(tag);

            } else {
                dialog.cancel();
                if (SimpleAlertDialogFragment.this.dialogActions != null)
                    SimpleAlertDialogFragment.this.dialogActions
                            .onAlertNegativeClicked(tag);
            }
        }
    };
}
