package com.cursoandroid.mushrooms_net.ui;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cursoandroid.mushrooms_net.util.Mushroom;
import com.cursoandroid.mushrooms_net.util.MushroomSQLiteHelper;
import com.example.mushrooms_net.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MushroomDetailActivity extends AppCompatActivity {

	private SQLiteDatabase db;
	private Mushroom mushroom;
	private TextView tvCommonName;
	private TextView tvBinomialName;
	private TextView tvDescription;
	private ImageView ivMushroomImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mushroom_detail);

		Bundle bundle = this.getIntent().getExtras();
		String mushroomName = bundle.getString("commonName");

		tvCommonName = (TextView) findViewById(R.id.tv_mushroom_detail_common_name);
		tvBinomialName = (TextView) findViewById(R.id.tv_mushroom_detail_binomial_name);
		tvDescription = (TextView) findViewById(R.id.tv_mushroom_detail_description);
		ivMushroomImage = (ImageView) findViewById(R.id.iv_mushroom_detail_image);

		MushroomSQLiteHelper mushroomsDBH = MushroomSQLiteHelper
				.getInstance(this);

		db = mushroomsDBH.getWritableDatabase();

		Cursor c = db
				.rawQuery(
						"SELECT id, commonName, binomialName, description FROM Mushrooms WHERE commonName LIKE '"
								+ mushroomName + "'", null);

		if (c.moveToFirst()) {
			mushroom = new Mushroom(c.getString(0), c.getString(1),
					c.getString(2), c.getString(3));
			tvCommonName.setText(mushroom.getCommonName());
			tvBinomialName.setText(mushroom.getBinomialName());
			tvDescription.setText(mushroom.getDescription());
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(getBaseContext()));
			imageLoader.displayImage("http://mushrooms-android.esy.es/img/"
					+ mushroom.getId() + ".jpg", ivMushroomImage);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mushroom_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_mushroom_delete) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Delete Mushroom");
			builder.setMessage("Do you want to delete this mushroom?");
			builder.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
