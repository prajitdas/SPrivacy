package com.prajitdas.sprivacy.policymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;

public class PolicyRuleChooserActivity extends Activity {
	private TableLayout mTableOfPolicies;
	private Button mBtnShowAllPolicies;
	private PolicyDBHelper db;
	private ArrayList<PolicyRule> listOfPolicyRules;
	private ArrayList<String> listOfPoliciesInStringForm;
	private Map<Integer, ToggleButton> mToggleButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		listOfPoliciesInStringForm = new ArrayList<String>();
		mToggleButtons = new HashMap<Integer, ToggleButton>();
		db = new PolicyDBHelper(this);
		db.getWritableDatabase();
		listOfPolicyRules = db.getAllPolicies();
		for(PolicyRule aPolicyRule : listOfPolicyRules)
			listOfPoliciesInStringForm.add(aPolicyRule.toString());
		
		instantiateViews();
		addOnClickListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		db.getWritableDatabase();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}

	private void instantiateViews() {
		mBtnShowAllPolicies = (Button) findViewById(R.id.btnShow);
		mTableOfPolicies = (TableLayout) findViewById(R.id.tableOfPolicies);
		for(PolicyRule aPolicyRule : listOfPolicyRules)
			addTableRow(aPolicyRule.toString(), aPolicyRule.isPolicyRule());
	}

	private void addTableRow(String policyText, boolean policyValue) {
		TableRow tblRow = new TableRow(this);
		TextView mTextViewPolicyStmt = new TextView(this);
		mToggleButtons.put(mToggleButtons.size(), new ToggleButton(this));
		
		mTextViewPolicyStmt.setText(policyText);
		mToggleButtons.get(mToggleButtons.size()-1).setChecked(policyValue);
		
		tblRow.addView(mTextViewPolicyStmt);
		tblRow.addView(mToggleButtons.get(mToggleButtons.size()-1));
		mTableOfPolicies.addView(tblRow);
	}

	private void addOnClickListener() {
		mBtnShowAllPolicies.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllPoliciesActivity.class);
				intent.putStringArrayListExtra("PolicyRuleChooserActivity", listOfPoliciesInStringForm);
				startActivity(intent);
			}
		});
		
		for(int i = 0; i < mToggleButtons.size(); i++) {
			mToggleButtons.get(i).setOnClickListener(new OnClickListener() {
				//Toggle Button to identify which policy was modified
				@Override
				public void onClick(View v) {
					SPrivacyApplication.makeToast(v.getContext(), "clicked on" + mToggleButtons);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_rule_chooser, menu);
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
	
	private void togglePolicy(int idOfPolicy) {
//		//Get a single policy to modify		
//		Cursor c = getContentResolver().query(PolicyQuery.baseUri, 
//				PolicyQuery.projection, 
//				Integer.toString(idOfPolicy), 
//				PolicyQuery.selectionArgs, 
//				PolicyQuery.sort);
//		if (!c.moveToFirst()) {
//			SPrivacyApplication.makeToast(this, "Well, could not find the particular id!");
//		}
//		else {				
//			ContentValues values = new ContentValues();
//	
//			values.put(PolicyDBHelper.getAppnameColumnName(), c.getString(c.getColumnIndex(PolicyDBHelper.getAppnameColumnName())));
//		    values.put(PolicyDBHelper.getResourceColumnName(), c.getString(c.getColumnIndex(PolicyDBHelper.getResourceColumnName())));
//		    if(c.getString(c.getColumnIndex(PolicyDBHelper.getPolicyColumnName())).equals("1"))
//		    	values.put(PolicyDBHelper.getPolicyColumnName(), 0);
//		    else
//		    	values.put(PolicyDBHelper.getPolicyColumnName(), 1);
//		    getContentResolver().update(PolicyQuery.baseUri, values, Integer.toString(idOfPolicy), null);	
//			SPrivacyApplication.makeToast(this, "Updated: "+values.toString());
//		}
	}
	
}