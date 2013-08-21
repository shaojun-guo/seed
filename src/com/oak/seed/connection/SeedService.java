package com.oak.seed.connection;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Message;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.oak.seed.ChatActivity;
import com.oak.seed.R;
import com.oak.seed.data.MessageItem;
import com.oak.seed.utils.MyLog;
import com.oak.seed.utils.Utils;

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

	public void removeListener(String jid) {
		mChatObervers.remove(jid);
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

	public void quit() {
		mCm.disconnect();
		stopSelf();
	}

	public void onReceiveMessage(Chat chat, Message message) {
		String from = message.getFrom();
		int indexOfSlash = from.indexOf("/");
		if (indexOfSlash != -1) {
			from = from.substring(0, indexOfSlash);
		}
		MyLog.d(TAG, "from: " + from);
		ChatListener l = mChatObervers.get(from);
		if (l != null) {
			l.onMessageReceived(message);
		} else {
			showNotification(from, message);
		}
	}

	private void showNotification(String from, Message message) {
		RosterEntry contact = mCm.getRoster().getEntry(from);
		String title = from;
		if (contact != null) {
			title = contact.getName();
		}
		MessageItem item = Utils.makeReceivedMessage(title, message);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.stat_notify_chat)
			.setContentTitle(title)
			.setContentText(item.getBody());

		Intent resultIntent = new Intent(this, ChatActivity.class);
		resultIntent.putExtra("jid", from);
		resultIntent.putExtra("name", title);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager nm = (NotificationManager) getSystemService(
				Context.NOTIFICATION_SERVICE);
		nm.notify(R.drawable.stat_notify_chat, builder.build());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCm.disconnect();
		MyLog.d(TAG, "Service destroyed");
	}

}
