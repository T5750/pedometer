package com.evangel.pedometerlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * test
 */
public class TestDBHelper extends SQLiteOpenHelper {
	private static final String TAG = "TestDBHelper";
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "TodayStepDB.db";
	private static final String TABLE_NAME = "TodayStepData";
	private static final String PRIMARY_KEY = "_id";
	public static final String TODAY = "today";
	public static final String DATE = "date";
	public static final String STEP = "step";
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + PRIMARY_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODAY + " TEXT, " + DATE
			+ " long, " + STEP + " long);";

	public static TestDBHelper factory(Context context) {
		return new TestDBHelper(context);
	}

	private TestDBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.e(TAG, SQL_CREATE_TABLE);
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
	}

	public synchronized void updateStepById(String _id, long stepNum) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(STEP, String.valueOf(stepNum));
		getWritableDatabase().update(TABLE_NAME, contentValues,
				PRIMARY_KEY + "=" + _id, null);
	}
}
