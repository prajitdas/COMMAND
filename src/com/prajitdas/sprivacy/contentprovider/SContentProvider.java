package com.prajitdas.sprivacy.contentprovider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policyprovider.PolicyProvider;
import com.prajitdas.sprivacy.policyprovider.util.PolicyQuery;

public class SContentProvider extends ContentProvider {
	static final String PROVIDER_NAME = "com.prajitdas.sprivacy.contentprovider.Content";
	static final String URL = "content://" + PROVIDER_NAME;

	static final Uri IMAGES_CONTENT_URI = Uri.parse(URL + "/images");
	static final Uri FILES_CONTENT_URI = Uri.parse(URL + "/files");
	static final Uri VIDEOS_CONTENT_URI = Uri.parse(URL + "/videos");
	static final Uri AUDIOS_CONTENT_URI = Uri.parse(URL + "/audios");
	static final Uri CONTACTS_CONTENT_URI = Uri.parse(URL + "/contacts");
	
	static final String _ID = "_id";
	static final String NAME = "name";
	
	static final int IMAGES = 1;
	static final int FILES = 2;
	static final int VIDEOS = 3;
	static final int AUDIOS = 4;
	static final int CONTACTS = 5;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
		uriMatcher.addURI(PROVIDER_NAME, "files", FILES);
		uriMatcher.addURI(PROVIDER_NAME, "videos", VIDEOS);
		uriMatcher.addURI(PROVIDER_NAME, "audios", AUDIOS);
		uriMatcher.addURI(PROVIDER_NAME, "contacts", CONTACTS);
	}
	
	private static HashMap<String, String> PROJECTION_MAP;
	
	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "Content";
	static final String TABLE_NAME = "content";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			NAME + " TEXT NOT NULL);";
	
	/**
	* Helper class that actually creates and manages 
	* the provider's underlying data repository.
	*/
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DB_TABLE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}
	}
	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		* Create a write able database which will trigger its 
		* creation if it doesn't already exist.
		*/
		db = dbHelper.getWritableDatabase();
		return (db == null)? false:true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			/**
			* Get all student records 
			*/
			case IMAGES:
				return "vnd.android.cursor.dir/image";
			case FILES:
				return "vnd.android.cursor.dir/file";
			case VIDEOS:
				return "vnd.android.cursor.dir/video";
			case AUDIOS:
				return "vnd.android.cursor.dir/audio";
			case CONTACTS:
				return "vnd.android.cursor.dir/contact";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
	
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		if (sortOrder == null || sortOrder == ""){
			/** 
			* By default sort on student names
			*/
			sortOrder = NAME;
		}

		Cursor c;
		switch (uriMatcher.match(uri)) {
			case IMAGES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setImageData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case FILES:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setFileData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case VIDEOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setVideoData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case AUDIOS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setAudioData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			case CONTACTS:
				qb.setProjectionMap(PROJECTION_MAP);
				c = setContactData(uri, projection, selection, selectionArgs, sortOrder);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return c;
	}
	
	private Cursor setImageData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(RealURIsForQuery.imageUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
			Log.v(SPrivacyApplication.getDebugTag(), "Image Policy true");
		}
		else {
			Log.v(SPrivacyApplication.getDebugTag(), "Image Policy false");
			c = null;
		}
		return c;
	}

	private Cursor setFileData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(RealURIsForQuery.fileUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
			int idx = c.getColumnIndexOrThrow(FileColumns.DATA);
		    c.moveToFirst();
			try {
				Log.v(SPrivacyApplication.getDebugTag(), "File Policy true and contents are: " + getStringFromFile(c.getString(idx)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			Log.v(SPrivacyApplication.getDebugTag(), "File Policy false");
			c = null;
		}
		return c;
	}

	private String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}
	
	private String getStringFromFile (String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		//Make sure you close all streams.
		fin.close();        
		return ret;
	}
	
	private Cursor setVideoData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(RealURIsForQuery.videoUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
			Log.v(SPrivacyApplication.getDebugTag(), "Video Policy true");
		}
		else {
			Log.v(SPrivacyApplication.getDebugTag(), "Video Policy false");
			c = null;
		}
		return c;
	}

	
	private Cursor setAudioData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(RealURIsForQuery.audioUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
			Log.v(SPrivacyApplication.getDebugTag(), "Audio Policy true");
		}
		else {
			Log.v(SPrivacyApplication.getDebugTag(), "Audio Policy false");
			c = null;
		}
		return c;
	}

	
	private Cursor setContactData(Uri uri, String[] projection, String selection, 
			String[] selectionArgs, String sortOrder) {
		Cursor c;
		if(isDataAccessAllowed()) {
			c = getContext().getContentResolver()
					.query(RealURIsForQuery.contactUri,
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
			/** 
			* register to watch a content URI for changes
			*/
			c.setNotificationUri(getContext().getContentResolver(), uri);
			Log.v(SPrivacyApplication.getDebugTag(), "Contact Policy true");
		}
		else {
			Log.v(SPrivacyApplication.getDebugTag(), "Contact Policy false");
			c = null;
		}
		return c;
	}

	/**
     * This interface defines constants for the Cursor and CursorLoader
     */
	private interface RealURIsForQuery {
		Uri imageUri = Images.Media.EXTERNAL_CONTENT_URI;
		Uri fileUri = Files.getContentUri("external");
		Uri videoUri = Video.Media.EXTERNAL_CONTENT_URI;
		Uri audioUri = Audio.Media.EXTERNAL_CONTENT_URI;
		Uri contactUri = Images.Media.EXTERNAL_CONTENT_URI;
    }

	/**
	 * This code should be able to find what policies are there to protect what content.
	 * @return
	 */
	private boolean isDataAccessAllowed() {
		String[] projection = { PolicyProvider.getPolicy() };
		// Show all the policies sorted by app name
		Cursor c = getContext().getContentResolver().query(PolicyQuery.baseUri, 
				projection, 
				PolicyProvider.getId() + " = '1' ", 
				PolicyQuery.selectionArgs, 
				PolicyQuery.sort);
		String result = "Results:";

		if (!c.moveToFirst()) {
			SPrivacyApplication.makeToast(getContext(), result+" no content yet!");
		}
		else {
			if(c.getCount() > 1)
				SPrivacyApplication.makeToast(getContext(), "Too many policies");
			else {
				if(c.getString(c.getColumnIndex(PolicyProvider.getPolicy())).equals("1"))
					return true;
			}
		}
		return false;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}
	
    @Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
    	return 0;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}	
}