package com.oak.seed.connection;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;


public class ConnectionManager {
	public static final String SERVER_NAME = "10.0.0.195";

	private SeedService mService;

	private ConnectionConfiguration mConConfig;
	private XMPPConnection mConnection;
	private HashMap<String, Chat> mChatPool;
	private Roster mRoster;
	
	public ConnectionManager(SeedService service) {
		mService = service;
		mConConfig = new ConnectionConfiguration(SERVER_NAME, 5222);
		mConConfig.setSASLAuthenticationEnabled(false);
		mConnection = new XMPPConnection(mConConfig);
		mChatPool = new HashMap<String, Chat>();
	}

	public XMPPConnection getConnection() {
		return mConnection;
	}

	public void connect() throws XMPPException {
		mConnection.connect();
	}

	public void disconnect() {
		mConnection.disconnect();
	}

	public void login(String userName, String password, Presence presence) throws XMPPException {
		if (!mConnection.isConnected()) {
			mConnection.connect();
		}
		mConnection.login(userName, password);
		if (presence != null) {
			mConnection.sendPacket(presence);
		}
		mRoster = mConnection.getRoster();
	}

	public Roster getRoster() {
		return mRoster;
	}

	public String getSelfName() {
		return mConnection.getAccountManager().getAccountAttribute("name");
	}

	public void createChatWith(String jid) {
        ChatManager chatmanager = mConnection.getChatManager();
        Chat chat = chatmanager.createChat(jid,
                new MessageListener() {
                    public void processMessage(Chat chat, Message message) {
                        System.out.println("Received message: "
                                + message.toXML());
                        mService.onReceiveMessage(message);
                    }
                });
        mChatPool.put(jid, chat);
	}

	public void sendMessage(String jid, String msg) {
		try {
			mChatPool.get(jid).sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
