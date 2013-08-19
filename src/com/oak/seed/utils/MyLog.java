package com.oak.seed.utils;

import android.util.Log;

public class MyLog {
	private static final boolean DEBUG = true;

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

}
