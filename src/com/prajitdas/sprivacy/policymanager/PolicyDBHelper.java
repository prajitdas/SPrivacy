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
import com.prajitdas.sprivacy.policymanager.util.PolicyInfo;
import com.prajitdas.sprivacy.policymanager.util.ProvInfo;
import com.prajitdas.sprivacy.policymanager.util.UserContext;

public class PolicyDBHelper extends SQLiteOpenHelper {
	// fields for the database tables
	private final static String APPID = "id";
	private final static String APPLABEL = "label";
	private final static String APPPACK = "package";
	private final static String APPPERM = "permissions";
	
	private final static String PROVID = "id";
	private final static String PROVLABEL = "label";
	private final static String PROVPRO = "provider";
	private final static String PROVAUTH = "authority";
	private final static String PROVREADPERM = "readperm";
	private final static String PROVWRITEPERM = "writeperm";

	private final static String POLID = "id";
	private final static String POLAPPID = "appid";
	private final static String POLPROVID = "provid";
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
	private final static String PROVIDER_TABLE_NAME = "providers";
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
	 * The providers that are accessible on the phone
	 * Table has the following columns:-
	 * PROVID
	 * PROVLABEL
	 * PROVPRO
	 * PROVAUTH
	 * PROVREADPERM
	 * PROVWRITEPERM 
	 */
	private final static String CREATE_PROVIDER_TABLE =  " CREATE TABLE " + PROVIDER_TABLE_NAME + " (" + 
			PROVID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			PROVLABEL + " TEXT NOT NULL, " + //Should be "unique" has been set not unique because it seems some of the providers are repeating TODO figure this out
			PROVPRO + " TEXT NOT NULL, " + //Should be "unique" has been set not unique because it seems some of the providers are repeating TODO figure this out
			PROVAUTH + " TEXT, " + //Should be "not null" has been set null because it seems some of the providers do not have an authority
			PROVREADPERM + " TEXT, " +
			PROVWRITEPERM+ " TEXT, " +
			"UNIQUE("+PROVPRO+", "+PROVAUTH+"));";

	/**
	 *  The policies that are installed by default on the phone.
	 *  The table has the following columns:-
	 *  POLID
	 *  POLAPPID
	 *  POLPROVID
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
			POLPROVID + " INTEGER NOT NULL REFERENCES providers(id) ON DELETE CASCADE, " +
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
		//loads the providers
		for(ProvInfo aProvider : defaultDataLoader.getProviders())
			addProvider(db, aProvider);
		//loads the policies, this is the interesting part and can be used to load
		//a default set of policies from an xml resource or a web service
		for(PolicyInfo aPolicyRule : defaultDataLoader.getPolicies())
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
	public int addPolicy(SQLiteDatabase db, PolicyInfo aPolicyRule) {
		ContentValues values = new ContentValues();
		values.put(POLAPPID, aPolicyRule.getAppId());
		values.put(POLPROVID, aPolicyRule.getProvId());
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
	 * method to insert into provider table the provider
	 * @param db
	 * @param aProvider
	 * @return
	 */
	public int addProvider(SQLiteDatabase db, ProvInfo aProvider) {
		ContentValues values = new ContentValues();
		values.put(PROVLABEL, aProvider.getLabel());
		values.put(PROVPRO, aProvider.getProviderName());
		values.put(PROVAUTH, aProvider.getAuthority());
		values.put(PROVREADPERM, aProvider.getReadPermission());
		values.put(PROVWRITEPERM, aProvider.getWritePermission());
		try {
			db.insert(PROVIDER_TABLE_NAME, null, values);
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
	public void deletePolicy(SQLiteDatabase db, PolicyInfo aPolicyRule) {
		db.delete(POLICY_TABLE_NAME, POLID + " = ?",
				new String[] { String.valueOf(aPolicyRule.getId()) });
	}

	/**
	 * method to delete a row from a table based on the identifier
	 * @param db
	 * @param aProvider
	 */
	public void deleteProvider(SQLiteDatabase db, ProvInfo aProvider) {
		db.delete(PROVIDER_TABLE_NAME, PROVID + " = ?",
				new String[] { String.valueOf(aProvider.getId()) });
	}

	/**
	 * Finds a policy based on the application and the provider being accessed
	 * @param db
	 * @param appPack
	 * @param provAuth
	 * @return
	 */
	public PolicyInfo findPolicyByApp(SQLiteDatabase db, String appPack, String provAuth) {
		// Select Policy Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					PROVIDER_TABLE_NAME + "." + PROVID + "," +
					PROVIDER_TABLE_NAME + "." + PROVLABEL + "," +
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
					" LEFT JOIN " + PROVIDER_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLPROVID + 
					" = " +  PROVIDER_TABLE_NAME + "." + PROVID + 
					" WHERE "  +  
					APPLICATION_TABLE_NAME + "." + APPPACK + " = '" + appPack + "' AND " +
					PROVIDER_TABLE_NAME + "." + PROVAUTH + " = '" + provAuth + 
					"';";

		PolicyInfo policyInfo = new PolicyInfo();
		
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				policyInfo.setId(Integer.parseInt(cursor.getString(0)));
				policyInfo.setAppId(Integer.parseInt(cursor.getString(1)));
				policyInfo.setAppLabel(cursor.getString(2));
				policyInfo.setProvId(Integer.parseInt(cursor.getString(3)));
				policyInfo.setProvLabel(cursor.getString(4));
				
				UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
				policyInfo.setUserContext(contextCondition);
				
				if(Integer.parseInt(cursor.getString(9)) == 1)
					policyInfo.setRule(true);
				else
					policyInfo.setRule(false);
				policyInfo.setAccessLevel(Integer.parseInt(cursor.getString(10)));
			}
		} catch(SQLException e) {
            throw new SQLException("Could not find " + e);
		}
		return policyInfo;
	}
	
	/**
	 * Finds a policy based on the policy id
	 * @param db
	 * @param id
	 * @return
	 */
	public PolicyInfo findPolicyByID(SQLiteDatabase db, int id) {
		// Select Policy Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					PROVIDER_TABLE_NAME + "." + PROVID + "," +
					PROVIDER_TABLE_NAME + "." + PROVLABEL + "," +
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
					" LEFT JOIN " + PROVIDER_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLPROVID + 
					" = " +  PROVIDER_TABLE_NAME + "." + PROVID + 
					" WHERE "  +  
					POLICY_TABLE_NAME + "." + POLID + " = " + id + ";";

		PolicyInfo policyInfo = new PolicyInfo();
		
		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				policyInfo.setId(Integer.parseInt(cursor.getString(0)));
				policyInfo.setAppId(Integer.parseInt(cursor.getString(1)));
				policyInfo.setAppLabel(cursor.getString(2));
				policyInfo.setProvId(Integer.parseInt(cursor.getString(3)));
				policyInfo.setProvLabel(cursor.getString(4));
				
				UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
				policyInfo.setUserContext(contextCondition);
				
				if(Integer.parseInt(cursor.getString(9)) == 1)
					policyInfo.setRule(true);
				else
					policyInfo.setRule(false);
				policyInfo.setAccessLevel(Integer.parseInt(cursor.getString(10)));
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		return policyInfo;
	}

	/**
	 * Getting all policies
	 * @param db
	 * @return
	 */
	public ArrayList<PolicyInfo> findAllPolicies(SQLiteDatabase db) {
		ArrayList<PolicyInfo> policyInfos = new ArrayList<PolicyInfo>();
		// Select All Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPLABEL + "," +
					PROVIDER_TABLE_NAME + "." + PROVID + "," +
					PROVIDER_TABLE_NAME + "." + PROVLABEL + "," +
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
					" LEFT JOIN " + PROVIDER_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLPROVID + 
					" = " +  PROVIDER_TABLE_NAME + "." + PROVID + ";";

		try{
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PolicyInfo policyInfo = new PolicyInfo();
					policyInfo.setId(Integer.parseInt(cursor.getString(0)));
					policyInfo.setAppId(Integer.parseInt(cursor.getString(1)));
					policyInfo.setAppLabel(cursor.getString(2));
					policyInfo.setProvId(Integer.parseInt(cursor.getString(3)));
					policyInfo.setProvLabel(cursor.getString(4));
					
					UserContext contextCondition = new UserContext(cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
					policyInfo.setUserContext(contextCondition);
					
					if(Integer.parseInt(cursor.getString(9)) == 1)
						policyInfo.setRule(true);
					else
						policyInfo.setRule(false);
					policyInfo.setAccessLevel(Integer.parseInt(cursor.getString(10)));

					// Adding policies to list
					policyInfos.add(policyInfo);
				} while (cursor.moveToNext());
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		// return policy rules list
		return policyInfos;
	}
	
	/**
	 * Getting all providers
	 * @param db
	 * @return
	 */
	public ArrayList<ProvInfo> findAllProviders(SQLiteDatabase db) {
		ArrayList<ProvInfo> provInfos = new ArrayList<ProvInfo>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + PROVIDER_TABLE_NAME + ";";

		try{
			Cursor cursor = db.rawQuery(selectQuery, null);
	
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProvInfo provInfo = new ProvInfo(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5));
					Log.v(SPrivacyApplication.getDebugTag(), provInfo.toString());
					// Adding providers to list
					provInfos.add(provInfo);
				} while (cursor.moveToNext());
			}
		} catch(SQLException e) {
	        throw new SQLException("Could not find " + e);
		}
		// return policy rules list
		return provInfos;
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
		db.execSQL(CREATE_PROVIDER_TABLE);
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
		db.execSQL("DROP TABLE IF EXISTS " +  PROVIDER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  POLICY_TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(PolicyDBHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ". Old data will be destroyed");
		db.execSQL("DROP TABLE IF EXISTS " +  APPLICATION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " +  PROVIDER_TABLE_NAME);
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
	public int updatePolicyRule(SQLiteDatabase db, PolicyInfo aPolicyRule) {
		ContentValues values = new ContentValues();
		values.put(POLAPPID, aPolicyRule.getAppId());
		values.put(POLPROVID, aPolicyRule.getProvId());
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
	 * method to update single provider
	 * @param db
	 * @param aProvider
	 * @return
	 */
	public int updateProvider(SQLiteDatabase db, ProvInfo aProvider) {
		ContentValues values = new ContentValues();
		values.put(PROVLABEL, aProvider.getLabel());
		values.put(PROVPRO, aProvider.getProviderName());
		values.put(PROVAUTH, aProvider.getAuthority());
		values.put(PROVREADPERM, aProvider.getReadPermission());
		values.put(PROVWRITEPERM, aProvider.getWritePermission());
		return db.update(PROVIDER_TABLE_NAME, values, PROVID + " = ?", 
				new String[] { String.valueOf(aProvider.getId()) });
	}
}