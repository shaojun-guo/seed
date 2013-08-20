package com.oak.seed.data;

import org.jivesoftware.smack.RosterGroup;

public class GroupItem {
	private RosterGroup mGroup;
	private int mOnlineNum;

	public RosterGroup getGroup() {
		return mGroup;
	}

	public void setGroup(RosterGroup group) {
		mGroup = group;
	}

	public String getName() {
		return mGroup.getName();
	}

	public int getTotal() {
		return mGroup.getEntryCount();
	}

	public int getOnlineNum() {
		return mOnlineNum;
	}

	public void setOnlineNum(int onlineNum) {
		mOnlineNum = onlineNum;
	}

}
