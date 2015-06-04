package com.csc.jsuarezardid.mushrooms_local;

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

import com.csc.jsuarezardid.util.Mushroom;
import com.csc.jsuarezardid.util.MushroomSQLiteHelper;

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
						"SELECT commonName, binomialName, description, image FROM Mushrooms WHERE commonName LIKE '"
								+ mushroomName + "'", null);

		if (c.moveToFirst()) {
			mushroom = new Mushroom(c.getString(0), c.getString(1),
					c.getString(2), c.getBlob(3));
			tvCommonName.setText(c.getString(0));
			tvBinomialName.setText(c.getString(1));
			tvDescription.setText(c.getString(2));
			if (mushroom.getImageBitMap() != null)
				ivMushroomImage.setImageBitmap(mushroom.getImageBitMap());
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
							MushroomSQLiteHelper mushroomsDBH = MushroomSQLiteHelper
									.getInstance(getApplicationContext());

							SQLiteDatabase db = mushroomsDBH
									.getWritableDatabase();

							String sql = "DELETE FROM Mushrooms WHERE commonName like ?";
							SQLiteStatement insertStmt = db
									.compileStatement(sql);
							insertStmt.clearBindings();
							insertStmt.bindString(1, mushroom.getCommonName());
							insertStmt.executeInsert();
							db.close();
							dialog.dismiss();
							NavUtils.navigateUpFromSameTask(MushroomDetailActivity.this);
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
