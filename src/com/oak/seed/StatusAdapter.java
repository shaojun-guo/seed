package com.oak.seed;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oak.seed.utils.Utils;

public 	class StatusAdapter extends BaseAdapter {
	Context mContext;
	final Presence.Mode[] mStatusList = {
			Mode.chat,
			Mode.available,
			Mode.away,
			Mode.xa,
			Mode.dnd
	};

	public StatusAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return mStatusList.length;
	}

	@Override
	public Object getItem(int position) {
		return mStatusList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Mode status = mStatusList[position];
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.status_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.text = (TextView) view.findViewById(R.id.text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.icon.setImageResource(Utils.getModeIconId(status));
		holder.text.setText(status.name());
		return view;
	}

	class ViewHolder {
		ImageView icon;
		TextView text;
	}

}
