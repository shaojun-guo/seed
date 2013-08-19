package com.oak.seed;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.oak.seed.connection.ChatListener;

public class ChatActivity extends BinderActivity implements ChatListener {
	String mJid;
	ListView mMessageList;
	EditText mMessageInput;
	Button mSendBtn;
	ChatAdapter mAdapter;

	String mNewMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent i = getIntent();
		mJid = i.getStringExtra("jid");
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
						mAdapter.addMessage("> " +msg);
						mCm.sendMessage(mJid, msg);
					}
				}
			}
		});
	}

	public void onBound() {
		mService.addListener(mJid, this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mAdapter.addMessage("< " + mNewMessage);
				break;
			}
		}
	};

	@Override
	public void onMessageReceived(String msg) {
		mNewMessage = msg;
		mHandler.sendEmptyMessage(0);
	}

	class ChatAdapter extends BaseAdapter {
		ArrayList<String> mMessageList;
		Context mContext;

		public ChatAdapter(Context context) {
			mContext = context;
			mMessageList = new ArrayList<String>();
		}

		public void addMessage(String msg) {
			Log.d("", "Add msg: " + msg);
			mMessageList.add(msg);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String msg = mMessageList.get(position);
			TextView text;
			if (convertView == null) {
				text = (TextView) LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
			} else {
				text = (TextView) convertView;
			}
			text.setText(msg);
			return text;
		}
		
	}
}
