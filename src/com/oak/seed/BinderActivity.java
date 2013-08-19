package com.oak.seed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.oak.seed.connection.ConnectionManager;
import com.oak.seed.connection.SeedService;
import com.oak.seed.connection.SeedService.LocalBinder;

public class BinderActivity extends Activity {
	boolean mBound;
	SeedService mService;
	WaitingDialog mWaitingDialog;
	ConnectionManager mCm;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWaitingDialog = new WaitingDialog(this);
	}

	@Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SeedService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mCm = mService.getConnectionManager();
			mBound = true;
			onBound();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}

	};

	public void onBound() {
		
	}
}
