package com.cursoandroid.mushrooms_net.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mushrooms_net.R;

public class LoginActivity extends Activity {

	private final String API_URL = "http://mushrooms-android.esy.es/api";
	private final String LOGIN_ROUTE = "/login";
	private final String REGISTER_ROUTE = "/register";

	private ProgressBar pbProgress;
	private Button btnLogin;
	private Button btnRegister;
	private EditText etUserName;
	private EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		pbProgress = (ProgressBar) findViewById(R.id.login_pb_progress);
		btnLogin = (Button) findViewById(R.id.login_bt_login);
		btnRegister = (Button) findViewById(R.id.login_bt_register);
		etUserName = (EditText) findViewById(R.id.login_et_username);
		etPassword = (EditText) findViewById(R.id.login_et_password);

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginTask loginTask = new LoginTask(v.getContext());
				loginTask.execute();
			}
		});

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterTask registerTask = new RegisterTask();
				registerTask.execute();
			}
		});

	}

	private class LoginTask extends AsyncTask<Void, Integer, String> {
		private Context context;
		
		public LoginTask(Context context) {
			this.context = context;
		}


		protected String doInBackground(Void... params) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.VISIBLE);
					btnLogin.setEnabled(false);
					btnRegister.setEnabled(false);
				}
			});
			String result = "";
			try {
				URL url = new URL(API_URL + LOGIN_ROUTE);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setAllowUserInteraction(true);
				conn.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				JSONObject json = new JSONObject();
				JSONObject user = new JSONObject();
				user.put("user_name", etUserName.getText().toString());
				user.put("password", etPassword.getText().toString());
				json.put("user", user);

				wr.write(json.toString());
				wr.flush();
				wr.close();
				InputStream in = new BufferedInputStream(conn.getInputStream());

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				result = builder.toString();

			} catch (IOException e) {
				Log.e("LoginService", e.getMessage());
			} catch (JSONException e) {
				Log.e("LoginService", e.getMessage());
			}
			return result;
		}

		protected void onPostExecute(String result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.GONE);
					btnLogin.setEnabled(true);
					btnRegister.setEnabled(true);
				}
			});

			try {
				JSONObject json = new JSONObject(result);
				JSONObject login = (JSONObject) json.get("login");
				String status = login.getString("status");
				String errorMessage = login.getString("message");
				String toastMessage = "";
				if (status.equals("success")) {
					toastMessage = "Logged in";
					SharedPreferences prefs = getSharedPreferences(
							"MisPreferencias", Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("currentUser", etUserName.getText().toString());
					editor.commit();
					Intent intent = new Intent(context, MushroomListActivity.class);
					startActivity(intent);
				} else if (status.equals("error")
						&& errorMessage.equals("no_user"))
					toastMessage = "Invalid user/password";
				else
					toastMessage = "Error";
				Toast.makeText(getApplicationContext(), toastMessage,
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class RegisterTask extends AsyncTask<Void, Integer, String> {

		protected String doInBackground(Void... params) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.VISIBLE);
					btnLogin.setEnabled(false);
					btnRegister.setEnabled(false);
				}
			});
			String result = "";
			try {
				URL url = new URL(API_URL + REGISTER_ROUTE);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setAllowUserInteraction(true);
				conn.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				JSONObject json = new JSONObject();
				JSONObject user = new JSONObject();
				user.put("user_name", etUserName.getText().toString());
				user.put("password", etPassword.getText().toString());
				json.put("user", user);

				wr.write(json.toString());
				wr.flush();
				wr.close();
				InputStream in = new BufferedInputStream(conn.getInputStream());

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				result = builder.toString();

			} catch (IOException e) {
				Log.e("LoginService", e.getMessage());
			} catch (JSONException e) {
				Log.e("LoginService", e.getMessage());
			}
			return result;
		}

		protected void onPostExecute(String result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.GONE);
					btnLogin.setEnabled(true);
					btnRegister.setEnabled(true);
				}
			});
			try {
				JSONObject json = new JSONObject(result);
				JSONObject registration = (JSONObject) json.get("registration");
				String status = registration.getString("status");
				String errorMessage = registration.getString("message");
				String toastMessage = "";
				if (status.equals("success"))
					toastMessage = "Signed Up";
				else if (status.equals("error")
						&& errorMessage.equals("user_exists"))
					toastMessage = "User name already taken";
				else
					toastMessage = "Error";
				Toast.makeText(getApplicationContext(), toastMessage,
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
