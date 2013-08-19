package com.oak.seed.utils;

import android.content.Context;

import com.oak.seed.R;

public class Utils {
	public static String getErrorMsg(Context context, int code) {
		String msg = "N/A";
		int resId = -1;
		switch (code) {
		case 0:
			break;
		case 401:
			resId = R.string.not_authorized;
			break;
		}
		if (resId != -1) {
			msg = context.getString(resId);
		}
		return msg;
	}

}
