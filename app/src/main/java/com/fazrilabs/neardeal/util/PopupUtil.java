package com.fazrilabs.neardeal.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by blastocode on 4/5/16.
 */
public class PopupUtil {
    private static Dialog mDialog;

    public static void showLoading(Context context, String msg) {
        dismiss();
        mDialog = ProgressDialog.show(context, "",
                msg, true);
    }

    public static void dismiss() {
        if(mDialog != null)
        {
            mDialog.dismiss();
        }
    }

}
