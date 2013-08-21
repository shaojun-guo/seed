package com.oak.seed.connection;

import org.jivesoftware.smack.packet.Message;

public interface ChatListener {
	void onMessageReceived(Message message);
}
