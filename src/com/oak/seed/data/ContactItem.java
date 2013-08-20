package com.oak.seed.data;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class ContactItem {
	RosterEntry mEntry;
	Presence mPresence;

	public RosterEntry getEntry() {
		return mEntry;
	}

	public void setEntry(RosterEntry entry) {
		mEntry = entry;
	}

	public Presence getPresence() {
		return mPresence;
	}

	public void setPresence(Presence presence) {
		mPresence = presence;
	}

}
