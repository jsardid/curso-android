package com.cursoandroid.mushrooms_net.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
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
	
	final static int GET_IMAGE_FROM_GALLERY = 0;
	
	ImageView ivMushroomImage;
	FrameLayout flMushroomImageWrapper;
	byte[] image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_mushroom);

		ivMushroomImage = (ImageView) findViewById(R.id.iv_create_mushroom_image);
		flMushroomImageWrapper = (FrameLayout) findViewById(R.id.fl_create_mushroom_image);

		flMushroomImageWrapper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"), GET_IMAGE_FROM_GALLERY);
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
				image = Utils.getByteArrayFromBitmap(scaledBitmap);

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_mushroom_creation) {
			MushroomSQLiteHelper mushroomsDBH = MushroomSQLiteHelper
					.getInstance(this);
			EditText et_commonName = (EditText) findViewById(R.id.et_create_mushroom_common_name);
			EditText et_binomialName = (EditText) findViewById(R.id.et_create_mushroom_binomial_name);
			EditText et_description = (EditText) findViewById(R.id.et_create_mushroom_description);

			if (et_commonName.getText().toString().equals("")) {
				Toast toast = Toast.makeText(this,
						"A common name must be given", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				try {
					SQLiteDatabase db = mushroomsDBH.getWritableDatabase();

					String sql = "INSERT INTO Mushrooms (commonName, binomialName, description, image) VALUES (?,?,?,?)";
					SQLiteStatement insertStmt = db.compileStatement(sql);
					insertStmt.clearBindings();
					insertStmt
							.bindString(1, et_commonName.getText().toString());
					insertStmt.bindString(2, et_binomialName.getText()
							.toString());
					insertStmt.bindString(3, et_description.getText()
							.toString());
					insertStmt.bindBlob(4, image == null ? new byte[0] : image);
					insertStmt.executeInsert();
					db.close();
					NavUtils.navigateUpFromSameTask(this);
				} catch (RuntimeException e) {
					Toast toast = Toast.makeText(this,
							"Error creating mushroom", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
