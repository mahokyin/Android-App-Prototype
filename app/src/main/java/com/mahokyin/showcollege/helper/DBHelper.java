package com.mahokyin.showcollege.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mahokyin.showcollege.College;

public class DBHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "collegeDB";

	// Contacts table name
	private static final String TABLE_COLLEGE = "COLLEGE";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DOMAIN = "domain";
	private static final String KEY_WEB_PAGE = "webpage";
	private static final String KEY_COUNTRY = "country";
	private static final String KEY_COUNTRY_CODE = "country_code";



	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COLLEGE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_DOMAIN + " TEXT," +
				 KEY_WEB_PAGE + " TEXT," +
				 KEY_COUNTRY + " TEXT," +
				KEY_COUNTRY_CODE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLEGE);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addCollege(College college) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, college.getName());
		values.put(KEY_DOMAIN, college.getDomain());
		values.put(KEY_WEB_PAGE, college.getWebPage());
		values.put(KEY_COUNTRY, college.getCountry());
		values.put(KEY_COUNTRY_CODE, college.getCountryCode());

		// Inserting Row
		db.insert(TABLE_COLLEGE, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public College getCollege(String name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_COLLEGE, new String[] {
				KEY_NAME, KEY_DOMAIN, KEY_WEB_PAGE, KEY_COUNTRY, KEY_COUNTRY_CODE}, KEY_NAME + "=?",
				new String[] { name }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		College contact = new College(cursor.getString(0),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		db.close(); // Closing database connection
		// return contact
		return contact;
	}

	public College getCollege(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_COLLEGE + " WHERE " +
				KEY_ID + "=" + id + "", null );

		if (cursor != null) {
			cursor.moveToFirst();
			College college = new College();
			college.setName(cursor.getString(1));
			college.setDomain(cursor.getString(2));
			college.setWebPage(cursor.getString(3));
			college.setCountry(cursor.getString(4));
			college.setCountryCode(cursor.getString(5));
			return college;
		}
		// return contact
		return null;
	}
	
	// Getting All Contacts
	public List<College> getAllColleges() {
		List<College> collegeList = new ArrayList<College>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_COLLEGE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				College college = new College();
				college.setName(cursor.getString(1));
				college.setDomain(cursor.getString(2));
				college.setWebPage(cursor.getString(3));
				college.setCountry(cursor.getString(4));
				college.setCountryCode(cursor.getString(5));
				// Adding contact to list
				collegeList.add(college);
			} while (cursor.moveToNext());
		}
		db.close(); // Closing database connection
		// return contact list
		return collegeList;
	}

	// Updating single contact
//	public int updateContact(Contact contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_NAME, contact.getName());
//		values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//		// updating row
//		return db.update(TABLE_COLLEGE, values, KEY_ID + " = ?",
//				new String[] { String.valueOf(contact.getID()) });
//	}

	// Deleting single contact
	public void deleteContact(College contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COLLEGE, KEY_NAME + " = ?",
				new String[] { String.valueOf(contact.getName()) });
		db.close();
	}

	public void clearTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COLLEGE, null, null);
		db.close();
	}

	// Getting contacts Count
	public int getCollegeCounts() {
		String countQuery = "SELECT  * FROM " + TABLE_COLLEGE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

}
