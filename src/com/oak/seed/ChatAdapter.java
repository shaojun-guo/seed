package com.oak.seed;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oak.seed.data.MessageItem;

public class ChatAdapter extends BaseAdapter {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd hh:mm");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	ArrayList<MessageItem> mMessageList;
	Context mContext;

	public ChatAdapter(Context context) {
		mContext = context;
		mMessageList = new ArrayList<MessageItem>();
	}

	public void addMessage(MessageItem msg) {
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
	public View getView(int position, View view, ViewGroup parent) {
		MessageItem msg = mMessageList.get(position);
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.name = (TextView)view.findViewById(R.id.name);
			holder.time = (TextView)view.findViewById(R.id.time);
			holder.body = (TextView)view.findViewById(R.id.body);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}

		SimpleDateFormat formater = TIME_FORMAT;
		Date now = new Date(System.currentTimeMillis());
		Date date = new Date(msg.getTime());
		if (now.getDay() != date.getDay()) {
			formater = DATE_FORMAT;
		}

		holder.name.setText(msg.getName());
		holder.time.setText("(" + formater.format(date) + ") :");
		if (msg.isIsSelf()) {
			holder.name.setTextColor(Color.BLUE);
			holder.time.setTextColor(Color.BLUE);
		} else {
			holder.name.setTextColor(Color.RED);
			holder.time.setTextColor(Color.RED);
		}
		holder.body.setText(msg.getBody());
		return view;
	}

	class ViewHolder {
		TextView name;
		TextView time;
		TextView body;
	}

}
