package com.cursoandroid.mushrooms_net.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cursoandroid.mushrooms_net.util.Mushroom;
import com.cursoandroid.mushrooms_net.util.MushroomSQLiteHelper;
import com.example.mushrooms_net.R;

public class MushroomListActivity extends AppCompatActivity {

	private ListView lvMushrooms;
	private ArrayList<Mushroom> mushrooms;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mushroom_list);

		MushroomSQLiteHelper mushroomsDBH = MushroomSQLiteHelper
				.getInstance(this);

		db = mushroomsDBH.getWritableDatabase();

		Cursor c = db
				.rawQuery(
						"SELECT commonName, binomialName, description, image FROM Mushrooms ORDER BY ROWID DESC",
						null);

		mushrooms = new ArrayList<Mushroom>();
		if (c.moveToFirst()) {
			do {
				String commonName = c.getString(0);
				String binomialName = c.getString(1);
				String description = c.getString(2);
				byte[] image = c.getBlob(3);
				mushrooms.add(new Mushroom(commonName, binomialName, description, image));
			} while (c.moveToNext());
		}

		lvMushrooms = (ListView) findViewById(R.id.lv_mushrooms_list_activity);

		MushroomsAdapter adapter = new MushroomsAdapter(this, mushrooms);

		lvMushrooms.setAdapter(adapter);
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
				item = inflater.inflate(R.layout.list_item_mushroom, parent, false);

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
						mBundle.putString("commonName", (String) tvCommonName.getText());
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				});
				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}
			holder.commonName.setText(mushrooms.get(position).getCommonName());
			holder.binomialName.setText(mushrooms.get(position)
					.getBinomialName());
			if (mushrooms.get(position).getImageBitMap() != null)
				holder.image.setImageBitmap(mushrooms.get(position)
						.getImageBitMap());
			else
				holder.image.setImageDrawable(getResources().getDrawable(R.drawable.noimage));

			return (item);
		}
	}

	static class ViewHolder {
		TextView commonName;
		TextView binomialName;
		ImageView image;
	}
}
