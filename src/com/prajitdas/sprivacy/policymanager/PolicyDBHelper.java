package com.prajitdas.sprivacy.policymanager;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.util.AppInfo;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;
import com.prajitdas.sprivacy.policymanager.util.Resource;

public class PolicyDBHelper extends SQLiteOpenHelper {
	// fields for the database
	private final static String APPID = "id";
	private final static String APPNAME = "name";
	
	private final static String RESID = "id";
	private final static String RESNAME = "name";

	private final static String POLID = "id";
	private final static String POLAPPID = "appid";
	private final static String POLRESID = "resid";
	private final static String POLICY = "policy";
	
	// database declarations
	private final static String DATABASE_NAME = "PrivacyPolicies";
	private final static int DATABASE_VERSION = 1;
	
	private final static String APPLICATION_TABLE_NAME = "applications";
	private final static String RESOURCE_TABLE_NAME = "resources";
	private final static String POLICY_TABLE_NAME = "policies";

	private final static String CREATE_APPLICATION_TABLE = " CREATE TABLE " + APPLICATION_TABLE_NAME + " (" + 
			APPID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			APPNAME + " TEXT NOT NULL UNIQUE);";
	
	private final static String CREATE_RESOURCE_TABLE =  " CREATE TABLE " + RESOURCE_TABLE_NAME + " (" + 
			RESID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RESNAME + " TEXT NOT NULL UNIQUE);";

	private final static String CREATE_POLICY_TABLE =  " CREATE TABLE " + POLICY_TABLE_NAME + " (" +
			POLID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			POLAPPID + " INTEGER NOT NULL REFERENCES applications(id) ON DELETE CASCADE, " +
			POLRESID + " INTEGER NOT NULL REFERENCES resources(id) ON DELETE CASCADE, " +
			POLICY + " INTEGER DEFAULT 0);";
	
	private static DefaultDataLoader defaultDataLoader;
	
	/**
	 * Database creation constructor
	 * @param context
	 */
	public PolicyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		defaultDataLoader = new DefaultDataLoader();
	}

	/**
	 * method to insert into application table the application
	 * @param value the name of the application
	 */
	public void addApplication(SQLiteDatabase db, AppInfo anAppInfo) {
		ContentValues values = new ContentValues();
		values.put(APPNAME, anAppInfo.getName());
		db.insert(APPLICATION_TABLE_NAME, null, values);
	}
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	
	/**
	 * method to insert into policy table the policy
	 * @param appId the applcation id
	 * @param resId the resource id
	 * @param policy the policy value
	 */
	public void addPolicy(SQLiteDatabase db, PolicyRule aPolicyRule) {
		ContentValues values = new ContentValues();
		values.put(POLAPPID, aPolicyRule.getAppId());
		values.put(POLRESID, aPolicyRule.getResId());
		if(aPolicyRule.isPolicyRule())
			values.put(POLICY, 1);
		else
			values.put(POLICY, 0);
		db.insert(POLICY_TABLE_NAME, null, values);
	}

	/**
	 * method to insert into resource table the resource
	 * @param value the name of the resource
	 */
	public void addResource(SQLiteDatabase db, Resource aResource) {
		ContentValues values = new ContentValues();
		values.put(RESNAME, aResource.getName());
		db.insert(RESOURCE_TABLE_NAME, null, values);
	}
	
	/**
	 * method to delete a row from a table based on the identifier 
	 * @param db reference to the db instance
	 * @param id identifier of the row to delete
	 * @param tableName table from which to delete data
	 */
	public void deleteApplication(SQLiteDatabase db, AppInfo anAppInfo) {
		db.delete(APPLICATION_TABLE_NAME, APPID + " = ?",
				new String[] { String.valueOf(anAppInfo.getId()) });
	}
	
	/**
	 * method to delete a row from a table based on the identifier 
	 * @param db reference to the db instance
	 * @param id identifier of the row to delete
	 * @param tableName table from which to delete data
	 */
	public void deletePolicy(SQLiteDatabase db, PolicyRule aPolicyRule) {
		db.delete(POLICY_TABLE_NAME, POLID + " = ?",
				new String[] { String.valueOf(aPolicyRule.getId()) });
	}
	
	/**
	 * method to delete a row from a table based on the identifier 
	 * @param db reference to the db instance
	 * @param id identifier of the row to delete
	 * @param tableName table from which to delete data
	 */
	public void deleteResource(SQLiteDatabase db, Resource aResource) {
		db.delete(RESOURCE_TABLE_NAME, RESID + " = ?",
				new String[] { String.valueOf(aResource.getId()) });
	}
	
	/**
	 * method to load the default set of policies into the database
	 * @param db reference to the db instance
	 */
	private void loadDefaultPoliciesIntoDB(SQLiteDatabase db) {
		for(AppInfo anAppInfo : defaultDataLoader.getApplications())
			addApplication(db, anAppInfo);
		for(Resource aResource : defaultDataLoader.getResources())
			addResource(db, aResource);
		for(PolicyRule aPolicyRule : defaultDataLoader.getPolicies())
			addPolicy(db, aPolicyRule);
	}
	
	/**
	 * table creation happens in onCreate this method also loads the default policies
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_APPLICATION_TABLE);
		db.execSQL(CREATE_RESOURCE_TABLE);
		db.execSQL(CREATE_POLICY_TABLE);
		loadDefaultPoliciesIntoDB(db);
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
		values.put(APPNAME, anAppInfo.getName());
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
		if(aPolicyRule.isPolicyRule())
			values.put(POLICY, 1);
		else
			values.put(POLICY, 0);
		return db.update(POLICY_TABLE_NAME, values, POLID + " = ?", 
				new String[] { String.valueOf(aPolicyRule.getId()) });
	}
	
	/**
	 * method to update single resource
	 * @param resName
	 */
	public int updateResource(SQLiteDatabase db, Resource aResource) {
		ContentValues values = new ContentValues();
		values.put(RESNAME, aResource.getName());
		return db.update(RESOURCE_TABLE_NAME, values, RESID + " = ?", 
				new String[] { String.valueOf(aResource.getId()) });
	}

	/**
	 * Getting all policies
	 * @return returns a list of policies
	 */
	public ArrayList<PolicyRule> getAllPolicies(SQLiteDatabase db) {
		ArrayList<PolicyRule> policyRules = new ArrayList<PolicyRule>();
		// Select All Query
		String selectQuery = "SELECT "+
					POLICY_TABLE_NAME + "." + POLID + "," +
					APPLICATION_TABLE_NAME + "." + APPID + "," +
					APPLICATION_TABLE_NAME + "." + APPNAME + "," +
					RESOURCE_TABLE_NAME + "." + RESID + "," +
					RESOURCE_TABLE_NAME + "." + RESNAME + "," +
					POLICY_TABLE_NAME + "." + POLICY +
					" FROM " + 
					POLICY_TABLE_NAME +
					" LEFT JOIN " + APPLICATION_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLAPPID + 
					" = " +  APPLICATION_TABLE_NAME + "." + APPID +
					" LEFT JOIN " + RESOURCE_TABLE_NAME + 
					" ON " + POLICY_TABLE_NAME + "." + POLRESID + 
					" = " +  RESOURCE_TABLE_NAME + "." + RESID + ";";

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PolicyRule policyRule = new PolicyRule();
				policyRule.setId(Integer.parseInt(cursor.getString(0)));
				policyRule.setAppId(Integer.parseInt(cursor.getString(1)));
				policyRule.setAppName(cursor.getString(2));
				policyRule.setResId(Integer.parseInt(cursor.getString(3)));
				policyRule.setResName(cursor.getString(4));
				if(Integer.parseInt(cursor.getString(5)) == 1)
					policyRule.setPolicyRule(true);
				else
					policyRule.setPolicyRule(false);
				// Adding policies to list
				policyRules.add(policyRule);
			} while (cursor.moveToNext());
		}

		// return policy rules list
		return policyRules;
	}
	
	/**
	 * Class that loads the default policies from network or other sources into an ArrayList of PolicyRule
	 * @TODO This class needs to be modified in order to fix how the default policies are loaded.
	 */
	class DefaultDataLoader {			
		private final String APPNAME = "com.prajitdas.parserapp";
		
		private ArrayList<AppInfo> applications;

		private ArrayList<Resource> resources;

		private ArrayList<PolicyRule> policies;

		public DefaultDataLoader() {
			applications = new ArrayList<AppInfo>();
			resources = new ArrayList<Resource>();
			policies = new ArrayList<PolicyRule>();
			naiveWayToLoadData();
		}

		public ArrayList<AppInfo> getApplications() {
			return applications;
		}

		public ArrayList<PolicyRule> getPolicies() {
			return policies;
		}

		public ArrayList<Resource> getResources() {
			return resources;
		}
		/**
		 * At the moment the default policy is being loaded in a naive way have to work on this to improve it
		 */
		private void naiveWayToLoadData() {
			AppInfo tempAppInfo = new AppInfo();

			tempAppInfo.setId(1);
			tempAppInfo.setName(APPNAME);
			applications.add(tempAppInfo);
			
			int count = 1;
			setValues(count, SPrivacyApplication.getConstImages(), tempAppInfo);
			count++;
			setValues(count, SPrivacyApplication.getConstFiles(), tempAppInfo);
			count++;
			setValues(count, SPrivacyApplication.getConstVideos(), tempAppInfo);
			count++;
			setValues(count, SPrivacyApplication.getConstAudios(), tempAppInfo);
			count++;
			setValues(count, SPrivacyApplication.getConstContacts(), tempAppInfo);
			count++;
		}
		public void setApplications(ArrayList<AppInfo> applications) {
			this.applications = applications;
		}
		
		public void setPolicies(ArrayList<PolicyRule> policies) {
			this.policies = policies;
		}

		public void setResources(ArrayList<Resource> resources) {
			this.resources = resources;
		}
		private void setValues(int id, String resource, AppInfo anAppInfo) {
			Resource tempRes = new Resource();
			PolicyRule tempPolicy = new PolicyRule();
			tempRes.setId(id);
			tempRes.setName(resource);
			resources.add(tempRes);
			
			tempPolicy.setId(id);
		    tempPolicy.setPolicyRule(true);
			tempPolicy.setAppId(anAppInfo.getId());
			tempPolicy.setAppName(anAppInfo.getName());
			tempPolicy.setResId(tempRes.getId());
		    tempPolicy.setResName(tempRes.getName());
			policies.add(tempPolicy);
		}
	}
}