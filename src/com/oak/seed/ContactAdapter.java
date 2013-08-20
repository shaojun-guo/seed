package com.oak.seed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oak.seed.data.ContactItem;
import com.oak.seed.data.GroupItem;
import com.oak.seed.utils.MyLog;
import com.oak.seed.utils.Utils;

public class ContactAdapter extends BaseExpandableListAdapter {
	Context mContext;
	ArrayList<GroupItem> mGroups;
	HashMap<String, Presence> mPresences;
	HashMap<String, List<ContactItem>> mContacts;

	public ContactAdapter(Context context) {
		mContext = context;
		mGroups = new ArrayList<GroupItem>();
		mContacts = new HashMap<String, List<ContactItem>>();
	}

	public void setContacts(Roster roster) {
		mGroups.clear();
		mContacts.clear();;
		Collection<RosterGroup> groups = roster.getGroups();
		for (RosterGroup group : groups) {
			int onlineNum = 0;
			GroupItem groupItem = new GroupItem();
			groupItem.setGroup(group);
			Collection<RosterEntry> entries = group.getEntries();
			List<ContactItem> contacts = new ArrayList<ContactItem>();
			for (RosterEntry entry : entries) {
				Presence presence = roster.getPresence(entry.getUser());
				if (presence.isAvailable()) {
					onlineNum++;
				}
				ContactItem contact = new ContactItem();
				contact.setEntry(entry);
				contact.setPresence(presence);
				contacts.add(contact);
			}
			groupItem.setOnlineNum(onlineNum);
			mGroups.add(groupItem);
			mContacts.put(group.getName(), contacts);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mContacts.get(
				mGroups.get(groupPosition).getName()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mContacts.get(
				mGroups.get(groupPosition).getName()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View view, ViewGroup parent) {
		GroupItem group = mGroups.get(groupPosition);
		int count = group.getTotal();
		int online = group.getOnlineNum();
		GroupViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);
			holder = new GroupViewHolder();
			holder.name = (TextView) view.findViewById(R.id.group_name);
			view.setTag(holder);
		} else {
			holder = (GroupViewHolder)view.getTag();
		}
		holder.name.setText(group.getName() + " (" + online + "/" + count + ")");
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		GroupItem group = mGroups.get(groupPosition);
		ContactItem contact = mContacts.get(group.getName()).get(childPosition);
		RosterEntry entry = contact.getEntry();
		Presence presence = contact.getPresence();
        ContactViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
            holder = new ContactViewHolder();
            holder.presenceIcon = (ImageView) view.findViewById(R.id.presence_icon);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.status = (TextView) view.findViewById(R.id.status_text);
            view.setTag(holder);
        } else {
            holder = (ContactViewHolder)view.getTag();
        }
        holder.presenceIcon.setImageResource(Utils.getPresenceIconId(presence));
        holder.name.setText(entry.getName());
        holder.status.setText(presence.getStatus());
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupViewHolder {
		TextView name;
	}

	class ContactViewHolder {
		ImageView presenceIcon;
		TextView name;
		TextView status;
	}

}
