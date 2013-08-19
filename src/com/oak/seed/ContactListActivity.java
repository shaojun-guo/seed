package com.oak.seed;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends BinderActivity {
	ListView mContactListView;
	Roster mRoster;
	ArrayList<RosterEntry> mContactList;
	ContactAdapter mAdapter;
	View mEmptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		mEmptyView = findViewById(R.id.no_contact);
		mContactListView = (ListView) findViewById(R.id.contact_list);
		mContactList = new ArrayList<RosterEntry>();
		mAdapter = new ContactAdapter(this);
		mContactListView.setAdapter(mAdapter);
		mContactListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RosterEntry entry = (RosterEntry)mContactListView
							.getAdapter().getItem(position);
				String jid = entry.getUser();
				if (mCm != null) {
					mCm.createChatWith(jid);
					Intent i = new Intent(ContactListActivity.this, ChatActivity.class);
					i.putExtra("jid", jid);
					startActivity(i);
					finish();
				}
			}

		});
		mContactListView.setEmptyView(mEmptyView);
	}

	public void onBound() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				mWaitingDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				mRoster = mCm.getRoster();
				Collection<RosterEntry> collection = mRoster.getEntries();
				for (RosterEntry entry : collection) {
					mContactList.add(entry);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mAdapter.setContacts(mContactList);
				mWaitingDialog.dismiss();
			}

		}.execute();
	}

	class ContactAdapter extends BaseAdapter {
		Context mContext;
		ArrayList<RosterEntry> mContacts;

		public ContactAdapter(Context context) {
			mContext = context;
			mContacts = new ArrayList<RosterEntry>();
		}

		public void setContacts(ArrayList<RosterEntry> contacts) {
			mContacts = contacts;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mContacts.size();
		}

		@Override
		public Object getItem(int position) {
			return mContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			RosterEntry entry = mContacts.get(position);
            
            if (convertView == null) {
                text = (TextView)LayoutInflater.from(mContext)
                		.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                text = (TextView)convertView;
            }
            text.setText(entry.getName());
			return text;
		}

	}

}
