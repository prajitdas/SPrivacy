package com.prajitdas.sprivacy.policymanager.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;

public class PolicyRuleChooserActivity extends Activity {
	private TableLayout mTableOfPolicies;
	private Button mBtnShowAllPolicies;
	private Button mBtnShowAllApplications;
	private Button mBtnShowAllProviders;
	private PolicyDBHelper db;
	private SQLiteDatabase database;
	private ArrayList<ToggleButton> mToggleButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		mToggleButtons = new ArrayList<ToggleButton>();
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
//		Connector.getInstance().registerDatabase(this, SPrivacyApplication.getConstDbkey(), db.getDatabaseName());
		
		instantiateViews();
		addOnClickListener();
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
	protected void onDestroy() {
		super.onDestroy();
//		Connector.getInstance().unregisterDatabase(this, SPrivacyApplication.getConstDbkey(), db.getDatabaseName());
	}

	private void instantiateViews() {
		mBtnShowAllPolicies = (Button) findViewById(R.id.btnShowPols);
		mBtnShowAllApplications = (Button) findViewById(R.id.btnShowApps);
		mBtnShowAllProviders  = (Button) findViewById(R.id.btnShowPros);
		mTableOfPolicies = (TableLayout) findViewById(R.id.tableOfPolicies);
		addTableRow();
		addTableRow(db.findPolicy(database, SPrivacyApplication.getConstAppname(), SPrivacyApplication.getConstImages()));
		addTableRow(db.findPolicy(database, SPrivacyApplication.getConstAppname(), SPrivacyApplication.getConstFiles()));
		addTableRow(db.findPolicy(database, SPrivacyApplication.getConstAppname(), SPrivacyApplication.getConstContacts()));
	}

	private void addTableRow(PolicyRule aPolicyRule) {
		TableRow tblRow = new TableRow(this);
		TextView mTextViewPolicyStmt = new TextView(this);
		ToggleButton tempToggleButton = new ToggleButton(this);
		tempToggleButton.setTextOn(SPrivacyApplication.getConstAccessGranted());
		tempToggleButton.setTextOff(SPrivacyApplication.getConstAccessDenied());
		
		mTextViewPolicyStmt.setText(aPolicyRule.toString());
		
		tempToggleButton.setChecked(aPolicyRule.isPolicyRule());
		tempToggleButton.setId(aPolicyRule.getId());
		
		tblRow.addView(mTextViewPolicyStmt);
		tblRow.addView(tempToggleButton);
		mTableOfPolicies.addView(tblRow);
		
		mToggleButtons.add(tempToggleButton);
	}

	private void addTableRow() {
		TableRow tblRow = new TableRow(this);
		TextView mTextViewPolicyStmt = new TextView(this);
		TextView mTextViewPolicyValu = new TextView(this);
		
		mTextViewPolicyStmt.setText(R.string.text_view_policy_conditions);
		mTextViewPolicyStmt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyStmt.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyStmt.setGravity(Gravity.CENTER);
		
		mTextViewPolicyValu.setText(R.string.text_view_policy_setting);
		mTextViewPolicyValu.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyValu.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyValu.setGravity(Gravity.CENTER);
		
		tblRow.addView(mTextViewPolicyStmt);
		tblRow.addView(mTextViewPolicyValu);
		mTableOfPolicies.addView(tblRow);
		
	}

	private void addOnClickListener() {
		mBtnShowAllPolicies.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllPoliciesActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnShowAllApplications.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllApplicationsActivity.class);
				startActivity(intent);
			}
		});

		mBtnShowAllProviders.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllProvidersActivity.class);
				startActivity(intent);
			}
		});
		
		for(int i = 0; i < mToggleButtons.size(); i++) {
			mToggleButtons.get(i).setOnClickListener(new OnClickListener() {
				//Toggle Button to identify which policy was modified
				@Override
				public void onClick(View v) {
//					SPrivacyApplication.makeToast(v.getContext(), "clicked on " + listOfPolicyRules.get(index).toString());
					togglePolicy(v.getId());
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
		PolicyRule tempPolicyRule = db.findPolicyByID(database, idOfPolicy);
		tempPolicyRule.togglePolicyRule();
		db.updatePolicyRule(database, tempPolicyRule);
	}
}