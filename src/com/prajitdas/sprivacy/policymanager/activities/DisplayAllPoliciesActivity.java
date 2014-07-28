package com.prajitdas.sprivacy.policymanager.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;
import com.prajitdas.sprivacy.policymanager.util.PolicyInfo;

public class DisplayAllPoliciesActivity extends Activity {
	private ArrayList<HashMap<String, String>> listOfPoliciesInStringForm;
	private ListView mListView;
	private SimpleAdapter mAdapter;
	private PolicyDBHelper db;
	private SQLiteDatabase database; 
	private String[] mapFrom;
	private int[] mapTo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all);
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		listOfPoliciesInStringForm = new ArrayList<HashMap<String, String>>();

		mapFrom = new String[] {"labelData", "detailData"};
		mapTo = new int[] {R.id.labelData, R.id.detailData};

		for(PolicyInfo aPolicyRule : db.findAllPolicies(database)) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("labelData", aPolicyRule.getLabel());
			tempMap.put("detailData", aPolicyRule.getDetailData());
			listOfPoliciesInStringForm.add(tempMap);
		}
		loadView(listOfPoliciesInStringForm);
	}

	private void loadView(ArrayList<HashMap<String, String>> list) {
		mListView = (ListView) findViewById(R.id.listViewAllEntities);
		mAdapter = new SimpleAdapter(this, list, R.layout.list_item, mapFrom, mapTo);
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
		getMenuInflater().inflate(R.menu.display_all_data, menu);
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