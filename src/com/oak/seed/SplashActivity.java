package com.oak.seed;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.oak.seed.connection.SeedService;

public class SplashActivity extends BinderActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(android.R.layout.activity_list_item);
		startService(new Intent(this, SeedService.class));
	}

	@Override
	public void onBound() {
		super.onBound();
		if (!mCm.getConnection().isAuthenticated()) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			startActivity(new Intent(this, ContactListActivity.class));
		}
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
