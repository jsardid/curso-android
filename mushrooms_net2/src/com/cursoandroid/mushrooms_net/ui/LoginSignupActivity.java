package com.cursoandroid.mushrooms_net.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mushrooms_net.R;

public class LoginSignupActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = getSharedPreferences("prefs",
				Context.MODE_PRIVATE);
		if (prefs.contains("currentUser")) {
			Intent intent = new Intent(this, MushroomListActivity.class);
			startActivity(intent);
		} else {
			setContentView(R.layout.activity_login_signup);
			LoginFragment loginFragment = new LoginFragment();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction tr = fm.beginTransaction();
			tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			tr.add(R.id.login_signup_fragment_container, loginFragment);
			tr.commit();
		}
	}

}
