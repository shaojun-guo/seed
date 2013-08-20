package com.oak.seed.utils;

import org.jivesoftware.smack.packet.XMPPError;

import com.oak.seed.R;

import android.content.Context;

public class Utils {

	public static String getErrorMsg(Context context, XMPPError error) {
		int resId = R.string.unknown_error;
		int code = error.getCode();
		String condition = error.getCondition();
		MyLog.d("Error", "Code:" + code + " Condition:" + condition);
		switch (code) {
		case 302:
			if (condition.equals("gone")) {
				resId = R.string.e302_gone;
			} else {
				resId = R.string.e302_redirect;
			}
			break;
		case 400:
			if (condition.equals("bad-request")) {
				resId = R.string.e400_bad_request;
			} else if (condition.equals("jid-malformed")) {
				resId = R.string.e400_jid_malformed;
			} else {
				resId = R.string.e400_unexpected_condition;
			}
			break;
		case 401:
			resId = R.string.e401_not_authorized;
			break;
		case 402:
			resId = R.string.e402_payment_required;
			break;
		case 403:
			resId = R.string.e403_forbidden;
			break;
		case 404:
			if (condition.equals("item-not-found")) {
				resId = R.string.e404_item_not_found;
			} else if (condition.equals("recipient-unavailable")) {
				resId = R.string.e404_recipient_unavailable;
			} else {
				resId = R.string.e404_remote_server_not_found;
			}
			break;
		case 405:
			resId = R.string.e405_not_allowed;
			break;
		case 406:
			resId = R.string.e406_no_acceptable;
			break;
		case 407:
			if (condition.equals("registration-required")) {
				resId = R.string.e407_registration_required;
			} else {
				resId = R.string.e407_subscription_required;
			}
			break;
		case 408:
			resId = R.string.e408_request_timeout;
			break;
		case 409:
			resId = R.string.e409_conflict;
			break;
		case 500:
			if (condition.equals("undefined-condition")) {
				resId = R.string.e500_undefined_condition;
			} else if (condition.equals("resource-constraint")) {
				resId = R.string.e500_resource_constraint;
			} else {
				resId = R.string.e500_internal_server_error;
			}
			break;
		case 501:
			resId = R.string.e501_feature_not_implemented;
			break;
		case 502:
			resId = R.string.e502_remote_server_error;
			break;
		case 503:
			resId = R.string.e503_service_unavailable;
			break;
		case 504:
			resId = R.string.e504_remote_server_timeout;
			break;
		}
		return context.getString(resId);
	}

}
