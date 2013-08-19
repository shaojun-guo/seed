package com.oak.seed;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.oak.seed.connection.SeedService;

public class MainActivity extends BinderActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(android.R.layout.activity_list_item);
		startService(new Intent(this, SeedService.class));
		startActivity(new Intent(this, LoginActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
