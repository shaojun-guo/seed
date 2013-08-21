package com.oak.seed;

import org.jivesoftware.smack.packet.Message;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.oak.seed.connection.ChatListener;
import com.oak.seed.utils.Utils;

public class ChatActivity extends BinderActivity implements ChatListener {
	String mJid;
	String mReceiverName;
	ListView mMessageList;
	EditText mMessageInput;
	Button mSendBtn;
	ChatAdapter mAdapter;

	Message mNewMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent i = getIntent();
		mJid = i.getStringExtra("jid");
		mReceiverName = i.getStringExtra("name");
		if (TextUtils.isEmpty(mJid)) {
			finish();
			return;
		}
		mMessageList = (ListView) findViewById(R.id.message_list);
		mAdapter = new ChatAdapter(this);
		mMessageList.setAdapter(mAdapter);
		mMessageInput = (EditText) findViewById(R.id.message_input);
		mSendBtn = (Button) findViewById(R.id.send_btn);
		mSendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mService != null) {
					String msg = mMessageInput.getText().toString();
					if (!TextUtils.isEmpty(msg)) {
						mCm.sendMessage(mJid, msg);
						mAdapter.addMessage(Utils.makeMessageToSend(mCm.getSelfName(), msg));
						mMessageInput.setText("");
					}
				}
			}
		});
	}

	public void onBound() {
		mService.addListener(mJid, this);
	}

	public void onDestroy() {
		mService.removeListener(mJid);
		super.onDestroy();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Message message = (Message) msg.obj;
			mAdapter.addMessage(Utils.makeReceivedMessage(mReceiverName, message));
		}
	};

	@Override
	public void onMessageReceived(Message msg) {
		mHandler.obtainMessage(0, msg).sendToTarget();
	}

}
