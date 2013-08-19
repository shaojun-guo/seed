package com.oak.seed.connection;

import java.util.HashMap;

import org.jivesoftware.smack.packet.Message;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.oak.seed.utils.MyLog;

public class SeedService extends Service {
	private static final String TAG = "SeedService";

	public static final int MSG_LOGIN_SUCCESS = 0;
	public static final int MSG_LOGIN_FAIL = 1;

	LocalBinder mBinder = new LocalBinder();

	ConnectionManager mCm;
	HashMap<String, ChatListener> mChatObervers;

	@Override
	public void onCreate() {
		super.onCreate();
		mCm = new ConnectionManager(this);
		mChatObervers = new HashMap<String, ChatListener>();
		MyLog.d(TAG, "Service created");
	}

	public void addListener(String jid, ChatListener l) {
		MyLog.d(TAG, "add " + jid);
		mChatObervers.put(jid, l);
	}

	public class LocalBinder extends Binder {
		public SeedService getService() {
			return SeedService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public ConnectionManager getConnectionManager() {
		return mCm;
	}

	public void onReceiveMessage(Message message) {
		String from = message.getFrom();
		int indexOfSlash = from.indexOf("/");
		if (indexOfSlash != -1) {
			from = from.substring(0, indexOfSlash);
		}
		MyLog.d(TAG, "from: " + from);
		ChatListener l = mChatObervers.get(from);
		if (l != null)
			l.onMessageReceived(message.getBody());
		else
			MyLog.d(TAG, "Can not find listener");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCm.disconnect();
		MyLog.d(TAG, "Service destroyed");
	}

}
