package com.cursoandroid.ejemplobasico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

	TextView tvSaludo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		tvSaludo = (TextView) findViewById(R.id.activity_detail_text_view);
        Bundle bundle = this.getIntent().getExtras();
        tvSaludo.setText("Hello " + bundle.getString("nombre"));

	}

}
