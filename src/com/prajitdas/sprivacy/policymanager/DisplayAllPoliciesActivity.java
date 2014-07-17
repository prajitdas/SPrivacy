package com.prajitdas.sprivacy.policymanager;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.prajitdas.sprivacy.R;

public class DisplayAllPoliciesActivity extends Activity {
	private ArrayList<String> listOfPoliciesInStringForm;
	private ListView mListViewPolicies;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_policies);
		listOfPoliciesInStringForm = new ArrayList<String>();
		mListViewPolicies = (ListView) findViewById(R.id.listViewPolicies);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfPoliciesInStringForm);
		mListViewPolicies.setAdapter(mAdapter);
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
