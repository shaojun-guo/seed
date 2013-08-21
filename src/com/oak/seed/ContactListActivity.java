package com.oak.seed;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.oak.seed.data.ContactItem;
import com.oak.seed.utils.MyLog;

public class ContactListActivity extends BinderActivity {
	ExpandableListView mContactListView;
	Roster mRoster;
	ContactAdapter mContactAdapter;
	View mEmptyView;

	Spinner mStatusSelector;

	TextView mMyName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		mMyName = (TextView) findViewById(R.id.my_name);
		mStatusSelector = (Spinner) findViewById(R.id.my_status);
		mStatusSelector.setAdapter(new StatusAdapter(this));
		mStatusSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Mode mode = (Mode) parent.getAdapter().getItem(position);
				new ChangeStatusTask().execute(mode);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		mEmptyView = findViewById(R.id.no_contact);
		mContactListView = (ExpandableListView) findViewById(R.id.contact_list);
		mContactAdapter = new ContactAdapter(this);
		mContactListView.setAdapter(mContactAdapter);
		mContactListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				ContactItem contact = (ContactItem)mContactAdapter.getChild(groupPosition, childPosition);
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
		mMyName.setText(mCm.getSelfName());
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
		mContactAdapter.setContacts(mRoster);
	}

	Handler mStatusHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mContactAdapter.setContacts(mRoster);
		}
	};

	class ChangeStatusTask extends AsyncTask<Mode, Void, Void> {

		@Override
		protected Void doInBackground(Mode... params) {
			Mode mode = params[0];
			Presence p = new Presence(Type.available);
			p.setMode(mode);
			mCm.getConnection().sendPacket(p);
			return null;
		}
	}

}
