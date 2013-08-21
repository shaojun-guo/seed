package com.oak.seed.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.content.Context;

import com.oak.seed.R;
import com.oak.seed.data.MessageItem;

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

	public static int getPresenceIconId(Presence p) {
		Mode mode = p.getMode();
		Type type = p.getType();

		if (type == Type.unavailable) {
			return R.drawable.presence_offline;
		}
		return getModeIconId(mode);
	}

	public static int getModeIconId(Mode mode) {
		int resId = R.drawable.presence_online;
		if (mode == Mode.available) {
			resId = R.drawable.presence_online;
		} else if (mode == Mode.away) {
			resId = R.drawable.presence_away;
		} else if (mode == Mode.chat) {
			resId = R.drawable.presence_online;
		} else if (mode == Mode.dnd) {
			resId = R.drawable.presence_busy;
		} else if (mode == Mode.xa) {
			resId = R.drawable.presence_audio_away;
		}
		return resId;
	}

	public static MessageItem makeReceivedMessage(String name, Message message) {
		String body = message.getBody();
		Date date = new Date();
		DelayInformation delayInfo = (DelayInformation) message.getExtension("x", "jabber:x:delay");
		if (delayInfo != null) {
			date = delayInfo.getStamp();
		}
		MessageItem item = new MessageItem();
		item.setName(name);
		item.setTime(date.getTime());
		item.setBody(body);
		item.setIsSelf(false);
		return item;
	}

	public static MessageItem makeMessageToSend(String name, String body) {
		MessageItem item = new MessageItem();
		item.setName(name);
		item.setBody(body);
		Date date = new Date();
		item.setTime(date.getTime());
		item.setIsSelf(true);
		return item;
	}
}
