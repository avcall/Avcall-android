/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avcall.app.ui;

import com.avcall.app.Constant;
import com.avcall.app.utils.StreamTools;
import com.avcall.app.utils.SystemStatusManager;
import com.avcall.app.R;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * register screen
 * 
 */
public class RegisterActivity extends BaseActivity {
	protected static final int SUCCESS = 1;
	protected static final int ERROR = 2;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private ProgressDialog pd;

	private Handler handler = new Handler(){
		public void handleMessage(Message message){
			RegisterActivity.this.pd.dismiss();
			switch (message.what){
				default:
					return;
				case ERROR:
					Toast.makeText(RegisterActivity.this.getApplicationContext(), RegisterActivity.this.getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
					return;
				case SUCCESS:
					try{
						String str = new JSONObject(message.obj.toString()).getString("error");
						if(str != null){
							Toast.makeText(RegisterActivity.this.getApplicationContext(), RegisterActivity.this.getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
							return;
						}else{
							Toast.makeText(RegisterActivity.this.getApplicationContext(), RegisterActivity.this.getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
							return;
						}
					}catch (JSONException e){
						e.printStackTrace();
					}
			}
		}
	};

	private void setTranslucentStatus(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemStatusManager localSystemStatusManager = new SystemStatusManager(this);
			localSystemStatusManager.setStatusBarTintEnabled(true);
			localSystemStatusManager.setStatusBarTintResource(R.color.top_bar_normal_bg);
			getWindow().getDecorView().setFitsSystemWindows(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.em_activity_register);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
		pd = new ProgressDialog(this);
	}

	public void register(View view) {
		final String username = userNameEditText.getText().toString().trim();
		final String password = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!password.equals(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			pd.setMessage(getResources().getString(R.string.Is_the_registered));
			pd.show();

			new Thread(new Runnable() {
				public void run() {
					try {
						HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(Constant.APPSERVER + "?username=" + username + "&password="+password).openConnection();
						httpURLConnection.setRequestMethod("GET");
						httpURLConnection.setConnectTimeout(15000);
						httpURLConnection.setReadTimeout(15000);
						if(httpURLConnection.getResponseCode() == 200){
							String str = StreamTools.readInputStream(httpURLConnection.getInputStream());
							Message message = Message.obtain();
							message.obj = str;
							message.what = SUCCESS;
							RegisterActivity.this.handler.sendMessage(message);
						}
						return;
					} catch (Exception e) {
						Message message = Message.obtain();
						message.what = ERROR;
						RegisterActivity.this.handler.sendMessage(message);
						e.printStackTrace();
					}
				}
			}).start();

		}
	}

	public void back(View view) {
		finish();
	}

}
