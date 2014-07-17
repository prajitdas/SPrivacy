package com.prajitdas.sprivacy.policymanager;

import java.util.ArrayList;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayAllPoliciesActivity extends Activity {
	private ArrayList<PolicyRule> listOfPolicyRules;
	private ListView mListViewPolicies;
	private ArrayAdapter<PolicyRule> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all_policies);
		PolicyDBHelper db = new PolicyDBHelper(this);
		listOfPolicyRules = new ArrayList<PolicyRule>();
		listOfPolicyRules = db.getAllPolicies();
		mListViewPolicies = (ListView) findViewById(R.id.listViewPolicies);
		mAdapter = new ArrayAdapter<PolicyRule>(this, android.R.layout.simple_list_item_1, listOfPolicyRules);;
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
