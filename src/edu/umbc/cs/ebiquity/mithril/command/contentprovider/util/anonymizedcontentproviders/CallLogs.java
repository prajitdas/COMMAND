package edu.umbc.cs.ebiquity.mithril.command.contentprovider.util.anonymizedcontentproviders;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import edu.umbc.cs.ebiquity.mithril.command.COMMANDApplication;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.PolicyDBHelper;
/**
 * @purpose: Is internally called by SPrivacy to get anonymous data
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class CallLogs extends ContentProvider {
	static final String PROVIDER_NAME = COMMANDApplication.getConstAnonymizedAuthorityPrefix()
			+COMMANDApplication.getConstAnnonymous()
			+COMMANDApplication.getConstCallLogs();
	static final String URL = "content://" + PROVIDER_NAME;
	 static final Uri CONTENT_URI = Uri.parse(URL);

	static final String _ID = "_id";
	static final String CALL_LOG = "callLog";

	static final int CALL_LOGS = 1;
	
	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, COMMANDApplication.getConstCallLogs(), CALL_LOGS);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	/**
	* Database specific constant declarations
	*/
	private SQLiteDatabase db;
	static final String TABLE_NAME = "anonymousCallLog";
	static final String CREATE_DB_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			CALL_LOG + " BLOB NOT NULL);";

	@Override
	public boolean onCreate() {
		Context context = getContext();
		PolicyDBHelper dbHelper = new PolicyDBHelper(context);
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
			case CALL_LOGS:
				return "vnd.android.cursor.dir/contact";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// the TABLE_NAME to query on
		queryBuilder.setTables(TABLE_NAME);

		switch (uriMatcher.match(uri)) {
			// maps all database column names
			case CALL_LOGS:
				queryBuilder.setProjectionMap(PROJECTION_MAP);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == ""){
			// No sorting-> sort on names by default
			sortOrder = _ID;
		}
		Cursor cursor = queryBuilder.query(db, projection, selection, 
				selectionArgs, null, null, sortOrder);
		/** 
		* register to watch a content URI for changes
		*/
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = db.insert(TABLE_NAME, "", values);
		// If record is added successfully
		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)){
			case CALL_LOGS:
				// delete all the records of the table
				count = db.delete(TABLE_NAME, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)){
			case CALL_LOGS:
				count = db.update(TABLE_NAME, values, selection, selectionArgs);
				break;
			default: 
				throw new IllegalArgumentException("Unsupported URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}