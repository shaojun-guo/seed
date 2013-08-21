package com.oak.seed.data;


public class MessageItem {

	private String mBody;
	private String mName;
	private long mTime;
	private boolean mIsSelf;

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		mBody = body;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public boolean isIsSelf() {
		return mIsSelf;
	}

	public void setIsSelf(boolean isSelf) {
		mIsSelf = isSelf;
	}
}
