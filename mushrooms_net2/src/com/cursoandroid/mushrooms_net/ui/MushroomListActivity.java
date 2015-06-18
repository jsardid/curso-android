package com.cursoandroid.mushrooms_net.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cursoandroid.mushrooms_net.util.Mushroom;
import com.cursoandroid.mushrooms_net.util.MushroomSQLiteHelper;
import com.example.mushrooms_net.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MushroomListActivity extends AppCompatActivity implements
		OnRefreshListener {

	private final String API_URL = "http://mushrooms-android.esy.es/api";

	private ListView lvMushrooms;
	private SwipeRefreshLayout slMushrooms;
	private ArrayList<Mushroom> mushrooms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mushroom_list);

		lvMushrooms = (ListView) findViewById(R.id.lv_mushrooms_list_activity);
		slMushrooms = (SwipeRefreshLayout) findViewById(R.id.sl_list_activity_swipe_layout);

		lvMushrooms.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (lvMushrooms == null || lvMushrooms
						.getChildCount() == 0) ? 0 : lvMushrooms.getChildAt(0)
						.getTop();
				slMushrooms.setEnabled(firstVisibleItem == 0
						&& topRowVerticalPosition >= 0);
			}
		});

		slMushrooms.setOnRefreshListener(this);
		slMushrooms.setColorSchemeResources(R.color.color_brown_100,
				R.color.color_brown_300, R.color.color_brown_500,
				R.color.color_brown_700);
		onRefresh();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_mushroom_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_add_mushroom) {
			Intent intent = new Intent(this, CreateMushroomActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_log_out) {
			SharedPreferences prefs = getSharedPreferences("prefs",
					Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = prefs.edit();
			editor.remove("currentUser");
			editor.commit();
			Intent intent = new Intent(this, LoginSignupActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	class MushroomsAdapter extends ArrayAdapter<Mushroom> {

		MushroomsAdapter(Context context, ArrayList<Mushroom> mushrooms) {
			super(context, R.layout.list_item_mushroom, mushrooms);
		}

		@SuppressWarnings("deprecation")
		public View getView(int position, View convertView, ViewGroup parent) {

			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				item = inflater.inflate(R.layout.list_item_mushroom, parent,
						false);

				holder = new ViewHolder();
				holder.commonName = (TextView) item
						.findViewById(R.id.tv_list_item_mushroom_common_name);
				holder.binomialName = (TextView) item
						.findViewById(R.id.tv_list_item_mushroom_binomial_name);
				holder.image = (ImageView) item
						.findViewById(R.id.tv_list_item_mushroom_image);
				CardView card = (CardView) item
						.findViewById(R.id.cv_list_item_mushroom_card);

				card.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TextView tvCommonName = (TextView) v
								.findViewById(R.id.tv_list_item_mushroom_common_name);
						Intent intent = new Intent(v.getContext(),
								MushroomDetailActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putString("commonName",
								(String) tvCommonName.getText());
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				});
				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}
			holder.image.setImageDrawable(getResources().getDrawable(
					R.drawable.noimage));

			holder.commonName.setText(mushrooms.get(position).getCommonName());
			holder.binomialName.setText(mushrooms.get(position)
					.getBinomialName());
			ImageLoader imageLoader = ImageLoader.getInstance();

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisk(true).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getApplicationContext()).defaultDisplayImageOptions(
					defaultOptions).build();

			imageLoader.init(config);
			imageLoader.displayImage("http://mushrooms-android.esy.es/img/"
					+ mushrooms.get(position).getId() + ".jpg", holder.image);

			return (item);
		}
	}

	static class ViewHolder {
		TextView commonName;
		TextView binomialName;
		ImageView image;
	}

	@Override
	public void onRefresh() {
		RefreshListTask refreshListTask = new RefreshListTask(this);
		refreshListTask.execute();

	}

	private class RefreshListTask extends AsyncTask<Void, Integer, String> {
		private Context context;

		public RefreshListTask(Context context) {
			this.context = context;
		}

		protected String doInBackground(Void... params) {

			String result = "";
			try {
				SharedPreferences prefs = getSharedPreferences("prefs",
						Context.MODE_PRIVATE);
				String currentUser = prefs.getString("currentUser", "");

				URL url = new URL(API_URL + "/user/" + currentUser
						+ "/mushrooms");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setAllowUserInteraction(true);
				conn.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");

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
			}
			return result;
		}

		protected void onPostExecute(String result) {
			try {

				JSONObject json;
				json = new JSONObject(result);
				String status = json.getString("status");
				if (status.equals("success")) {

					// Delete all mushrooms from database
					MushroomSQLiteHelper mushroomsDBH = MushroomSQLiteHelper
							.getInstance(getApplicationContext());

					SQLiteDatabase db = mushroomsDBH.getWritableDatabase();

					String sqlDelete = "DELETE FROM Mushrooms";
					SQLiteStatement deleteStmt = db.compileStatement(sqlDelete);
					deleteStmt.executeInsert();

					// Insert mushrooms from response
					JSONArray jsonMushroomsArray = json
							.getJSONArray("mushrooms");
					for (int i = 0; i < jsonMushroomsArray.length(); i++) {
						JSONObject mushroom = jsonMushroomsArray
								.getJSONObject(i);
						String sqlInsert = "INSERT INTO Mushrooms (id, commonName, binomialName, description) VALUES (?,?,?,?)";
						SQLiteStatement insertStmt = db
								.compileStatement(sqlInsert);
						insertStmt.clearBindings();
						insertStmt.bindString(1, mushroom.getString("id"));
						insertStmt.bindString(2,
								mushroom.getString("common_name"));
						insertStmt.bindString(3,
								mushroom.getString("binomial_name"));
						insertStmt.bindString(4,
								mushroom.getString("description"));
						insertStmt.executeInsert();
					}

					// Select and set adapter
					Cursor c = db
							.rawQuery(
									"SELECT id, commonName, binomialName, description FROM Mushrooms ORDER BY id DESC",
									null);

					mushrooms = new ArrayList<Mushroom>();
					if (c.moveToFirst()) {
						do {
							String id = c.getString(0);
							String commonName = c.getString(1);
							String binomialName = c.getString(2);
							String description = c.getString(3);
							mushrooms.add(new Mushroom(id, commonName,
									binomialName, description));
						} while (c.moveToNext());
					}

					lvMushrooms = (ListView) findViewById(R.id.lv_mushrooms_list_activity);

					MushroomsAdapter adapter = new MushroomsAdapter(context,
							mushrooms);

					lvMushrooms.setAdapter(adapter);
					slMushrooms.setRefreshing(false);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
