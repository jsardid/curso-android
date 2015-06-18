package com.cursoandroid.mushrooms_net.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MushroomSQLiteHelper extends SQLiteOpenHelper {

	private static MushroomSQLiteHelper sInstance;
	private static final String DATABASE_NAME = "DBMushrooms";
	private static final String TABLE_NAME = "Mushrooms";
	private static final int DATABASE_VERSION = 1;

	String sqlCreate = "CREATE TABLE "
			+ TABLE_NAME
			+ " (id TEXT, commonName TEXT, binomialName TEXT, description TEXT)";

	public static synchronized MushroomSQLiteHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MushroomSQLiteHelper(
					context.getApplicationContext(), DATABASE_NAME, null,
					DATABASE_VERSION);
		}
		return sInstance;
	}

	private MushroomSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(sqlCreate);
	}

}