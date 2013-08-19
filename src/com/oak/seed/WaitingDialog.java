package com.oak.seed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

public class WaitingDialog {
	private ProgressDialog mProgressDialog;
	public WaitingDialog(Context context) {
		if (Build.VERSION.SDK_INT >= 11) {
			mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		} else {
			mProgressDialog = new ProgressDialog(context);
		}
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(context.getText(R.string.waiting));
		mProgressDialog.setCanceledOnTouchOutside(false);
	}

	public void show() {
		mProgressDialog.show();
	}

	public void dismiss() {
		mProgressDialog.dismiss();
	}

}
