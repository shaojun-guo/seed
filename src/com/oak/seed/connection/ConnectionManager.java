package com.oak.seed.connection;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
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
	private ChatManager mChatMgr;
	private HashMap<String, Chat> mChatPool;
	private Roster mRoster;
	private MessageListener mMessageListener = new MessageListener() {
        public void processMessage(Chat chat, Message message) {
            mService.onReceiveMessage(chat, message);
        }
    };
	
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
		mChatMgr = mConnection.getChatManager();
		mChatMgr.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (!createdLocally) {
					chat.addMessageListener(mMessageListener);
				}
			}

		});
	}

	public Roster getRoster() {
		return mRoster;
	}

	public String getSelfName() {
		return mConnection.getAccountManager().getAccountAttribute("username");
	}

	public void sendMessage(String jid, String msg) {
		try {
			if (mChatMgr == null) {
				return;
			}
	        Chat chat = mChatPool.get(jid);
			if (chat == null) {
				chat = mChatMgr.createChat(jid, mMessageListener);
		        mChatPool.put(jid, chat);
			}
			chat.sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
