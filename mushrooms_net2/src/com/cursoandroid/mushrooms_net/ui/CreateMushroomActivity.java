package com.cursoandroid.mushrooms_net.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.cursoandroid.mushrooms_net.util.MushroomSQLiteHelper;
import com.cursoandroid.mushrooms_net.util.Utils;
import com.example.mushrooms_net.R;

public class CreateMushroomActivity extends AppCompatActivity {

	private final static int GET_IMAGE_FROM_GALLERY = 0;
	private final static String API_URL = "http://mushrooms-android.esy.es/api";
	private final static String INSERT_MUSHROOM_ROUTE = "/mushroom";

	ImageView ivMushroomImage;
	FrameLayout flMushroomImageWrapper;
	String image_str;
	EditText et_commonName;
	EditText et_binomialName;
	EditText et_description;

	boolean isSaveEnabled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create_mushroom);

		ivMushroomImage = (ImageView) findViewById(R.id.iv_create_mushroom_image);
		flMushroomImageWrapper = (FrameLayout) findViewById(R.id.fl_create_mushroom_image);
		et_commonName = (EditText) findViewById(R.id.et_create_mushroom_common_name);
		et_binomialName = (EditText) findViewById(R.id.et_create_mushroom_binomial_name);
		et_description = (EditText) findViewById(R.id.et_create_mushroom_description);

		flMushroomImageWrapper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						GET_IMAGE_FROM_GALLERY);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == 0 && resultCode == Activity.RESULT_OK
					&& data != null) {

				Uri selectedImageUri = data.getData();
				Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), selectedImageUri);
				Bitmap scaledBitmap = Utils
						.getScaledBitMapFromBitMap(imageBitmap);
				ivMushroomImage.setImageBitmap(scaledBitmap);
				byte[] image = Utils.getByteArrayFromBitmap(scaledBitmap);
				image_str = Base64.encodeToString(image, Base64.DEFAULT);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_mushroom, menu);
		if (isSaveEnabled)
			menu.findItem(R.id.action_mushroom_creation).setEnabled(true);
		else
			menu.findItem(R.id.action_mushroom_creation).setEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_mushroom_creation) {
			if (et_commonName.getText().toString().equals("")) {
				Toast toast = Toast.makeText(this,
						"A common name must be given", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				InsertMushroomTask registerTask = new InsertMushroomTask(
						getApplicationContext());
				registerTask.execute();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class InsertMushroomTask extends AsyncTask<Void, Integer, String> {
		private Context context;

		public InsertMushroomTask(Context context) {
			this.context = context;
		}

		protected String doInBackground(Void... params) {

			String result = "";
			try {
				isSaveEnabled = false;
				invalidateOptionsMenu();

				URL url = new URL(API_URL + INSERT_MUSHROOM_ROUTE);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setAllowUserInteraction(true);
				conn.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				JSONObject json = new JSONObject();
				JSONObject mushroom = new JSONObject();

				SharedPreferences prefs = getSharedPreferences("prefs",
						Context.MODE_PRIVATE);
				String currentUser = prefs.getString("currentUser", "");

				mushroom.put("user_name", currentUser);
				mushroom.put("common_name", et_commonName.getText().toString());
				mushroom.put("binomial_name", et_binomialName.getText()
						.toString());
				mushroom.put("description", et_description.getText().toString());
				mushroom.put("image", image_str);
				json.put("mushroom", mushroom);

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

			Intent intent = new Intent(getApplicationContext(),
					MushroomListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}

	}

}
