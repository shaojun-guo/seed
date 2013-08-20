package com.oak.seed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.oak.seed.data.ContactItem;
import com.oak.seed.utils.MyLog;

public class ContactListActivity extends BinderActivity {
	ExpandableListView mContactListView;
	Roster mRoster;
	HashMap<String, List<ContactItem>> mContactList;
	ArrayList<RosterGroup> mGroupList;
	ContactAdapter mAdapter;
	View mEmptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		mEmptyView = findViewById(R.id.no_contact);
		mContactListView = (ExpandableListView) findViewById(R.id.contact_list);
		mContactList = new HashMap<String, List<ContactItem>>();
		mGroupList = new ArrayList<RosterGroup>();
		mAdapter = new ContactAdapter(this);
		mContactListView.setAdapter(mAdapter);
		mContactListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				ContactItem contact = (ContactItem)mAdapter.getChild(groupPosition, childPosition);
				String jid = contact.getEntry().getUser();
				mCm.createChatWith(jid);
				Intent i = new Intent(ContactListActivity.this, ChatActivity.class);
				i.putExtra("jid", jid);
				startActivity(i);
				return true;
			}

		});
		mContactListView.setEmptyView(mEmptyView);
	}

	public void onBound() {
		if (mGroupList.isEmpty() || mContactList.isEmpty()) {
			new GetRosterTask().execute();
		}
	}

	class GetRosterTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			mWaitingDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			mRoster = mCm.getRoster();
			mRoster.addRosterListener(new RosterListener() {
				@Override
				public void entriesAdded(Collection<String> addresses) {
				}
				@Override
				public void entriesDeleted(Collection<String> addresses) {
				}
				@Override
				public void entriesUpdated(Collection<String> addresses) {
				}
				@Override
				public void presenceChanged(Presence presence) {
					MyLog.d("", "from:" + presence.getFrom() + " " + presence);
					mStatusHander.obtainMessage().sendToTarget();
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter.setContacts(mRoster);
			mWaitingDialog.dismiss();
		}

	}

	Handler mStatusHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mAdapter.setContacts(mRoster);
		}
	};

}
