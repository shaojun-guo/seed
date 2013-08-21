package com.oak.seed;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.oak.seed.utils.Utils;

public class LoginActivity extends BinderActivity {
	EditText mUserNameInput;
	EditText mPasswordInput;
	Spinner mStatusSelector;
	Button mLoginBtn;
	Presence mSelectedPresence = new Presence(Type.available);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUserNameInput = (EditText)findViewById(R.id.username_input);
		mPasswordInput = (EditText)findViewById(R.id.password_input);
		mStatusSelector = (Spinner)findViewById(R.id.status_selector);
		mStatusSelector.setAdapter(new StatusAdapter(this));
		mStatusSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Mode mode = (Mode) parent.getAdapter().getItem(position);
				mSelectedPresence.setMode(mode);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mUserNameInput.setText("test1");
		mPasswordInput.setText("111");
		mLoginBtn = (Button)findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performLogin();
			}
		});
	}

	private void performLogin() {
		final String userName = mUserNameInput.getText().toString();
		final String password = mPasswordInput.getText().toString();
		if (TextUtils.isEmpty(userName)) {
			mUserNameInput.setError(getString(R.string.user_empty));
			return;
		}
		if (TextUtils.isEmpty(password)) {
			mPasswordInput.setError(getString(R.string.password_empty));
			return;
		}
		mWaitingDialog.show();
		new LoginTask(mSelectedPresence).execute(userName, password);
	}

	class LoginTask extends AsyncTask<String, Void, Boolean> {
		String mErrorMsg;
		Presence mPresence;
		public LoginTask(Presence p) {
			mPresence = p;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mWaitingDialog.dismiss();
			if (result) {
				Intent i = new Intent(LoginActivity.this, ContactListActivity.class);
				i.putExtra("status", StatusAdapter.getModeIndex(mSelectedPresence.getMode()));
				startActivity(i);
				finish();
			} else {
				showErrorDialog(mErrorMsg);
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String userName = params[0];
			String password = params[1];
			try {
				mCm.login(userName, password, mPresence);
				return true;
			} catch(XMPPException e) {
				mErrorMsg = Utils.getErrorMsg(LoginActivity.this, e.getXMPPError());
			} catch (Exception e) {
				e.printStackTrace();
				mErrorMsg = getString(R.string.unknown_error);
			}
			return false;
		}

	}

	private void showErrorDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setPositiveButton(R.string.ok, null)
			.create().show();
	}

}
