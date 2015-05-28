package com.cursoandroid.ejemplobasico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	EditText etNombre;
	Button btnHello;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etNombre = (EditText) findViewById(R.id.activity_main_edit_text);
		btnHello = (Button) findViewById(R.id.activity_main_button);

		btnHello.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("nombre", etNombre.getText().toString());

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_menu_copy) {
			Toast toast = Toast.makeText(getApplicationContext(), "Copy", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}else if (id == R.id.action_menu_cut) {
			Toast toast = Toast.makeText(getApplicationContext(), "Cut", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}if (id == R.id.action_menu_paste) {
			Toast toast = Toast.makeText(getApplicationContext(), "Paste", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}if (id == R.id.action_menu_share) {
			Toast toast = Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
