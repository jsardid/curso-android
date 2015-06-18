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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mushrooms_net.R;

public class SignupFragment extends Fragment implements OnClickListener {

	private final static String API_URL = "http://mushrooms-android.esy.es/api";
	private final static String REGISTER_ROUTE = "/register";

	private Button btnRegister;
	private EditText etUserName;
	private EditText etPassword;
	private ProgressBar pbProgress;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

		btnRegister = (Button) v.findViewById(R.id.register_bt_register);
		pbProgress = (ProgressBar) v.findViewById(R.id.register_pb_progress);
		etUserName = (EditText) v.findViewById(R.id.register_et_username);
		etPassword = (EditText) v.findViewById(R.id.register_et_password);

		btnRegister.setOnClickListener(this);


		return v;
	}
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.register_bt_register) {
			RegisterTask loginTask = new RegisterTask();
			loginTask.execute();
		}

	}

	
	private class RegisterTask extends AsyncTask<Void, Integer, String> {

		protected String doInBackground(Void... params) {

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.VISIBLE);
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
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pbProgress.setVisibility(View.INVISIBLE);
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
				{
					toastMessage = "Signed Up";
					SharedPreferences prefs = getActivity()
							.getSharedPreferences("prefs", Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("currentUser", etUserName.getText()
							.toString());
					editor.commit();
					Intent intent = new Intent(getActivity().getApplicationContext(),
							MushroomListActivity.class);
					startActivity(intent);

				}
				else if (status.equals("error")
						&& errorMessage.equals("user_exists"))
					toastMessage = "User name already taken";
				else
					toastMessage = "Error";
				Toast.makeText(getActivity(), toastMessage,
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
