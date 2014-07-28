package com.prajitdas.sprivacy.policymanager;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.util.AppInfo;
import com.prajitdas.sprivacy.policymanager.util.DefaultDataLoader;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;
import com.prajitdas.sprivacy.policymanager.util.Provider;
import com.prajitdas.sprivacy.policymanager.util.UserContext;

public class PolicyDBHelper extends SQLiteOpenHelper {
	// fields for the database
	private final static String APPID = "id";
	private final static String APPLABEL = "label";
	private final static String APPPACK = "package";
	private final static String APPPERM = "permissions";
	
	private final static String RESID = "id";
	private final static String RESLABEL = "label";
	private final static String RESPRO = "provider";
	private final static String RESAUTH = "authority";
	private final static String RESREADPERM = "readperm";
	private final static String RESWRITEPERM = "writeperm";

	private final static String POLID = "id";
	private final static String POLAPPID = "appid";
	private final static String POLRESID = "resid";
	private final static String CONTEXTLOC = "location";
	private final static String CONTEXTACT = "activity";
	private final static String CONTEXTTIME = "time";
	private final static String CONTEXTID = "identity";
	private final static String POLICY = "policy";
	private final static String POLACCLVL = "accesslvl";
	
	// database declarations
	private final static String DATABASE_NAME = "PrivacyPolicies";
	private final static int DATABASE_VERSION = 1;

	private final static String APPLICATION_TABLE_NAME = "applications";
	private final static String RESOURCE_TABLE_NAME = "resources";
	private final static String POLICY_TABLE_NAME = "policies";
	
	private Context context;

	/**
	 * The applications installed on the phone
	 * Table has the following columns:-
	 * APPID
	 * APPLABEL
	 * APPPACK
	 * APPPERM
	 */
	private final static String CREATE_APPLICATION_TABLE = " CREATE TABLE " + APPLICATION_TABLE_NAME + " (" + 
			APPID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			APPLABEL + " TEXT NOT NULL UNIQUE, " +
			APPPACK + " TEXT NOT NULL UNIQUE, " +
			APPPERM + " TEXT);";
	
	/**
	 * The resources that are accessible on the phone
	 * Table has the following columns:-
	 * RESID
	 * RESLABEL
	 * RESPRO
	 * RESAUTH
	 * RESREADPERM
	 * RESWRITEPERM 
	 */
	private final static String CREATE_RESOURCE_TABLE =  " CREATE TABLE " + RESOURCE_TABLE_NAME + " (" + 
			RESID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RESLABEL + " TEXT NOT NULL, " + //Should be "unique" has been set not unique because it seems some of the providers are repeating TODO figure this out
			RESPRO + " TEXT NOT NULL, " + //Should be "unique" has been set not unique because it seems some of the providers are repeating TODO figure this out
			RESAUTH + " TEXT, " + //Should be "not null" has been set null because it seems some of the providers do not have an authority
			RESREADPERM + " TEXT, " +
			RESWRITEPERM+ " TEXT);";

	/**
	 *  The policies that are installed by default on the phone.
	 *  The table has the following columns:-
	 *  POLID
	 *  POLAPPID
	 *  POLRESID
	 *  CONTEXTLOC
	 *  CONTEXTACT
	 *  CONTEXTTIME
	 *  CONTEXTID
	 *  POLICY
	 *  POLACCLVL
	 *  
	 *  A value of 1 in the policy column refers to a policy of access granted
	 *  A value of 0 in the policy column refers to a policy of access denied
	 *  
	 *  A value of 0 in the accesslvl column refers to access level of "give all data"
	 *  A value of 1 in the accesslvl column refers to access level of "give fake data"
	 *  A value of 2 in the accesslvl column refers to access level of "give no data"
	 *  
	 *  This last option will be more complicated to implement and will have to incorporate 
	 *  how we limit the data. One possible idea is to remove any data which can identify the
	 *  device or the user in any way.
	 *  A value of 3 in the accesslvl column refers to access level of "give limited data"
	 */
	private final static String CREATE_POLICY_TABLE =  " CREATE TABLE " + POLICY_TABLE_NAME + " (" +
			POLID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			POLAPPID + " INTEGER NOT NULL REFERENCES applications(id) ON DELETE CASCADE, " +
			POLRESID + " INTEGER NOT NULL REFERENCES resources(id) ON DELETE CASCADE, " +
			CONTEXTLOC + " TEXT NOT NULL DEFAULT '*', "+
			CONTEXTACT + " TEXT NOT NULL DEFAULT '*', "+
			CONTEXTTIME + " TEXT NOT NULL DEFAULT '*', "+
			CONTEXTID + " TEXT NOT NULL DEFAULT '*', "+
			POLICY + " INTEGER NOT NULL DEFAULT 0, " +
			POLACCLVL + " INTEGER NOT NULL DEFAULT 0);";
	
	private static DefaultDataLoader defaultDataLoader;
	
	/**
	 * method to load the default set of policies into the database
	 * @param db reference to the db instance
	 */
	private void loadDefaultPoliciesIntoDB(SQLiteDatabase db) {
		defaultDataLoader = new DefaultDataLoader(getContext());
		//loads the applications
		for(AppInfo anAppInfo : defaultDataLoader.getApplications())
			addApplication(db, anAppInfo);
		//loads the resources or providers
		for(Provider aResource : defaultDataLoader.getResources())
			addResource(db, aResource);
		//loads the policies, this is the interesting part and can be used to load
		//a default set of policies from an xml resource or a web service
		for(PolicyRule aPolicyRule : defaultDataLoader.getPolicies())
			addPolicy(db, aPolicyRule);
	}

	/**
	 * Database creation constructor
	 * @param context
	 */
	public PolicyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.setContext(context); 
	}

	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public static int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	
	/**
	 * method to insert into application table the application
	 * @param db
	 * @param anAppInfo
	 * @return
	 */
	public int addApplication(SQLiteDatabase db, AppInfo anAppInfo) {
		ContentValues values = new ContentValues();
		values.put(APPLABEL, anAppInfo.getLabel());
		values.put(APPPACK, anAppInfo.getPackageName());
		values.put(APPPERM, anAppInfo.getPermissions());
		try{
			db.insert(APPLICATION_TABLE_NAME, null, values);
		} catch (SQLException e) {
            Log.e(SPrivacyApplication.getDebugTag(), "Error inserting " + values, e);
            return -1;
		}
		return 1;
	}
	
	/**
	 * method to insert into policy table the policy
	 * @param db
	 * @param aPolicyRule
	 * @return
	 */
	public int addPolicy(SQLiteDatabase db, PolicyRule aPolicyRule) {
		ContentValues values = new ContentValues();
		values.put(POLAPPID, aPolicyRule.getAppId());
		values.put(POLRESID, aPolicyRule.getResId());
		values.put(CONTEXTLOC, aPolicyRule.getUserContext().getLocation());
		values.put(CONTEXTACT, aPolicyRule.getUserContext().getActivity());
		values.put(CONTEXTTIME, aPolicyRule.getUserContext().getTime());
		values.put(CONTEXTID, aPolicyRule.getUserContext().getIdentity());
		if(aPolicyRule.isRule())
			values.put(POLICY, 1);
		else
			values.put(POLICY, 0);
		values.put(POLACCLVL, aPolicyRule.getAccessLevel());
		try {
			db.insert(POLICY_TABLE_NAME, null, values);
		} catch (SQLException e) {
	        Log.e(SPrivacyApplication.getDebugTag(), "Error inserting " + values, e);
	        return -1;
		}
		return 1;
	}

	/**
	 * method to insert into resource table the resource
	 * @param db
	 * @param aResource
	 * @return
	 */
	public int addResource(SQLiteDatabase db, Provider aResource) {
		ContentValues values = new ContentValues();
		values.put(RESLABEL, aResource.getLabel());
		values.put(RESPRO, aResource.getProviderName());
		values.put(RESAUTH, aResource.getAuthority());
		values.put(RESREADPERM, aResource.getReadPermission());
		values.put(RESWRITEPERM, aResource.getWritePermission());
		try {
			db.insert(RESOURCE_TABLE_NAME, null, values);
		} catch (SQLException e) {
	        Log.e(SPrivacyApplication.getDebugTag(), "Error inserting " + values, e);
	        return -1;
		}
		return 1;
	}

	/**
	 * method to delete a row from a table based on the identifier 
	 * @param db
	 * @param anAppInfo
	 */
	public void deleteApplication(SQLiteDatabase db, AppInfo anAppInfo) {
		db.delete(APPLICATION_TABLE_NAME, APPID + " = ?",
				new String[] { String.valueOf(anAppInfo.getId()) });
	}

	/**
	 * method to delete a row from a table based on the identifier
	 * @param db
	 * @param aPolicyRule
	 */
	public void deletePolicy(SQLiteDatabase db, PolicyRule aPolicyRule) {
		db.delete(POLICY_TABLE_NAME, POLID + " = ?",
				new String[] { String.valueOf(aPolicyRule.getId()) });
	}

	/**
	 * method to delete a row from a table based on the identifier
	 * @param db
	 * @param aResource
	 */
	public void deleteResource(SQLiteDatabase db, Provider aResource) {
		db.delete(RESOURCE_TABLE_NAME, RESID + " = ?",
				new String[] { String.valueOf(aResource.getId()) });
	}

	/**
	 * Finds a policy based on the application and the resource being accessed
	 * @param db
	 * @param appPack
	 * @param resProvider
	 * @return
	 */
	public PolicyRule findPolicyByApp(SQLiteDatabase db, String appPack, String resAuth) {
		// Select Policy Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					RESOURCE_TABLE_NAME + "." + RESID + "," +
					RESOURCE_TABLE_NAME + "." + RESLABEL + "," +
					POLICY_TABLE_NAME + "." + CONTEXTLOC + "," +
					POLICY_TABLE_NAME + "." + CONTEXTACT + "," +
					POLICY_TABLE_NAME + "." + CONTEXTTIME + "," +
					POLICY_TABLE_NAME + "." + CONTEXTID + "," +
					POLICY_TABLE_NAME + "." + POLICY + "," +
					POLICY_TABLE_NAME + "." + POLACCLVL + 
					" FROM " + 
					POLICY_TABLE_NAME +
					" LEFT JOIN " + APPLICATION_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLAPPID + 
					" = " +  APPLICATION_TABLE_NAME + "." + APPID +
					" LEFT JOIN " + RESOURCE_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLRESID + 
					" = " +  RESOURCE_TABLE_NAME + "." + RESID + 
					" WHERE "  +  
					APPLICATION_TABLE_NAME + "." + APPPACK + " = '" + appPack + "' AND " +
					RESOURCE_TABLE_NAME + "." + RESAUTH + " = '" + resAuth + 
					"';";

		PolicyRule policyRule = new PolicyRule();
		
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				policyRule.setId(Integer.parseInt(cursor.getString(0)));
				policyRule.setAppId(Integer.parseInt(cursor.getString(1)));
				policyRule.setAppLabel(cursor.getString(2));
				policyRule.setResId(Integer.parseInt(cursor.getString(3)));
				policyRule.setResLabel(cursor.getString(4));
				
				UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
				policyRule.setUserContext(contextCondition);
				
				if(Integer.parseInt(cursor.getString(9)) == 1)
					policyRule.setRule(true);
				else
					policyRule.setRule(false);
				policyRule.setAccessLevel(Integer.parseInt(cursor.getString(10)));
			}
		} catch(SQLException e) {
            throw new SQLException("Could not find " + e);
		}
		return policyRule;
	}
	
	/**
	 * Finds a policy based on the policy id
	 * @param db
	 * @param id
	 * @return
	 */
	public PolicyRule findPolicyByID(SQLiteDatabase db, int id) {
		// Select Policy Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					RESOURCE_TABLE_NAME + "." + RESID + "," +
					RESOURCE_TABLE_NAME + "." + RESLABEL + "," +
					POLICY_TABLE_NAME + "." + CONTEXTLOC + "," +
					POLICY_TABLE_NAME + "." + CONTEXTACT + "," +
					POLICY_TABLE_NAME + "." + CONTEXTTIME + "," +
					POLICY_TABLE_NAME + "." + CONTEXTID + "," +
					POLICY_TABLE_NAME + "." + POLICY + "," +
					POLICY_TABLE_NAME + "." + POLACCLVL + 
					" FROM " + 
					POLICY_TABLE_NAME +
					" LEFT JOIN " + APPLICATION_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLAPPID + 
					" = " +  APPLICATION_TABLE_NAME + "." + APPID +
					" LEFT JOIN " + RESOURCE_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLRESID + 
					" = " +  RESOURCE_TABLE_NAME + "." + RESID + 
					" WHERE "  +  
					POLICY_TABLE_NAME + "." + POLID + " = " + id + ";";

		PolicyRule policyRule = new PolicyRule();
		
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				policyRule.setId(Integer.parseInt(cursor.getString(0)));
				policyRule.setAppId(Integer.parseInt(cursor.getString(1)));
				policyRule.setAppLabel(cursor.getString(2));
				policyRule.setResId(Integer.parseInt(cursor.getString(3)));
				policyRule.setResLabel(cursor.getString(4));
				
				UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
				policyRule.setUserContext(contextCondition);
				
				if(Integer.parseInt(cursor.getString(9)) == 1)
					policyRule.setRule(true);
				else
					policyRule.setRule(false);
				policyRule.setAccessLevel(Integer.parseInt(cursor.getString(10)));
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		return policyRule;
	}

	/**
	 * Getting all policies
	 * @param db
	 * @return
	 */
	public ArrayList<PolicyRule> findAllPolicies(SQLiteDatabase db) {
		ArrayList<PolicyRule> policyRules = new ArrayList<PolicyRule>();
		// Select All Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					RESOURCE_TABLE_NAME + "." + RESID + "," +
					RESOURCE_TABLE_NAME + "." + RESLABEL + "," +
					POLICY_TABLE_NAME + "." + CONTEXTLOC + "," +
					POLICY_TABLE_NAME + "." + CONTEXTACT + "," +
					POLICY_TABLE_NAME + "." + CONTEXTTIME + "," +
					POLICY_TABLE_NAME + "." + CONTEXTID + "," +
					POLICY_TABLE_NAME + "." + POLICY + "," +
					POLICY_TABLE_NAME + "." + POLACCLVL + 
					" FROM " + 
					POLICY_TABLE_NAME +
					" LEFT JOIN " + APPLICATION_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLAPPID + 
					" = " +  APPLICATION_TABLE_NAME + "." + APPID +
					" LEFT JOIN " + RESOURCE_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLRESID + 
					" = " +  RESOURCE_TABLE_NAME + "." + RESID + ";";

		try{
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PolicyRule policyRule = new PolicyRule();
					policyRule.setId(Integer.parseInt(cursor.getString(0)));
					policyRule.setAppId(Integer.parseInt(cursor.getString(1)));
					policyRule.setAppLabel(cursor.getString(2));
					policyRule.setResId(Integer.parseInt(cursor.getString(3)));
					policyRule.setResLabel(cursor.getString(4));
					
					UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
					policyRule.setUserContext(contextCondition);
					
					if(Integer.parseInt(cursor.getString(9)) == 1)
						policyRule.setRule(true);
					else
						policyRule.setRule(false);
					policyRule.setAccessLevel(Integer.parseInt(cursor.getString(10)));

					// Adding policies to list
					policyRules.add(policyRule);
				} while (cursor.moveToNext());
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		// return policy rules list
		return policyRules;
	}
	
	/**
	 * Getting all providers
	 * @param db
	 * @return
	 */
	public ArrayList<Provider> findAllProviders(SQLiteDatabase db) {
		ArrayList<Provider> providers = new ArrayList<Provider>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + RESOURCE_TABLE_NAME + ";";

		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
	
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Provider provider = new Provider(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5));
					Log.v(SPrivacyApplication.getDebugTag(), provider.toString());
					// Adding providers to list
					providers.add(provider);
				} while (cursor.moveToNext());
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		// return policy rules list
		return providers;
	}

	/**
	 * Getting all applications
	 * @param db
	 * @return
	 */
	public ArrayList<AppInfo> findAllApplications(SQLiteDatabase db) {
		ArrayList<AppInfo> apps = new ArrayList<AppInfo>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + APPLICATION_TABLE_NAME + ";";

		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
	
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					AppInfo app = new AppInfo(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3));
					// Adding applications to list
					apps.add(app);
				} while (cursor.moveToNext());
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		// return applications list
		return apps;
	}

	/**
	 * table creation happens in onCreate this method also loads the default policies
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_APPLICATION_TABLE);
		db.execSQL(CREATE_RESOURCE_TABLE);
		db.execSQL(CREATE_POLICY_TABLE);
		//The following method loads the database with the default data on creation of the database
		loadDefaultPoliciesIntoDB(db);
	}

	/**
	 * Method to delete all data from the database; Very dangerous
	 * @param db
	 */
	public void deleteAllData(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " +  APPLICATION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  RESOURCE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  POLICY_TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(PolicyDBHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ". Old data will be destroyed");
		db.execSQL("DROP TABLE IF EXISTS " +  APPLICATION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  RESOURCE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  POLICY_TABLE_NAME);
		onCreate(db);
	}
	
	/**
	 * method to update single application
	 * @param appName
	 */
	public int updateApplication(SQLiteDatabase db, AppInfo anAppInfo) {
		ContentValues values = new ContentValues();
		values.put(APPLABEL, anAppInfo.getLabel());
		values.put(APPPACK, anAppInfo.getPackageName());
		values.put(APPPERM, anAppInfo.getPermissions());
		return db.update(APPLICATION_TABLE_NAME, values, APPID + " = ?", 
				new String[] { String.valueOf(anAppInfo.getId()) });
	}
	
	/**
	 * method to update single policy
	 * @param aPolicyRule update the policy value
	 */
	public int updatePolicyRule(SQLiteDatabase db, PolicyRule aPolicyRule) {
		ContentValues values = new ContentValues();
		values.put(POLAPPID, aPolicyRule.getAppId());
		values.put(POLRESID, aPolicyRule.getResId());
		values.put(CONTEXTLOC, aPolicyRule.getUserContext().getLocation());
		values.put(CONTEXTACT, aPolicyRule.getUserContext().getActivity());
		values.put(CONTEXTTIME, aPolicyRule.getUserContext().getTime());
		values.put(CONTEXTID, aPolicyRule.getUserContext().getIdentity());
		if(aPolicyRule.isRule())
			values.put(POLICY, 1);
		else
			values.put(POLICY, 0);
		values.put(POLACCLVL, aPolicyRule.getAccessLevel());
		return db.update(POLICY_TABLE_NAME, values, POLID + " = ?", 
				new String[] { String.valueOf(aPolicyRule.getId()) });
	}
	
	/**
	 * method to update single resource
	 * @param resName
	 */
	public int updateResource(SQLiteDatabase db, Provider aResource) {
		ContentValues values = new ContentValues();
		values.put(RESLABEL, aResource.getLabel());
		values.put(RESPRO, aResource.getProviderName());
		values.put(RESAUTH, aResource.getAuthority());
		values.put(RESREADPERM, aResource.getReadPermission());
		values.put(RESWRITEPERM, aResource.getWritePermission());
		return db.update(RESOURCE_TABLE_NAME, values, RESID + " = ?", 
				new String[] { String.valueOf(aResource.getId()) });
	}
}