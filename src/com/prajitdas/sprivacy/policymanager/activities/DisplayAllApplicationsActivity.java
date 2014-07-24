package com.prajitdas.sprivacy.policymanager.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;
import com.prajitdas.sprivacy.policymanager.util.AppInfo;

public class DisplayAllApplicationsActivity extends Activity {
	private ArrayList<String> listOfApplications;
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private PolicyDBHelper db;
	private SQLiteDatabase database; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_policies);
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		listOfApplications = new ArrayList<String>();
		for(AppInfo anApp : db.findAllApplications(database))
			listOfApplications.add(anApp.toString());
		loadView(listOfApplications);
	}

	private void loadView(ArrayList<String> list) {
		mListView = (ListView) findViewById(R.id.listViewPolicies);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		database = db.getWritableDatabase();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_all_policies, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}