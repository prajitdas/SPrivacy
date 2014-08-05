package com.prajitdas.sprivacy.policymanager.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;
import com.prajitdas.sprivacy.policymanager.util.PolicyInfo;

public class PolicyRuleChooserActivity extends Activity {
	private Button mBtnDBOps;
	private TableLayout mTableOfPolicies;
	private PolicyDBHelper db;
	private SQLiteDatabase database;
	private ArrayList<ToggleButton> mToggleButtons;
	private ArrayList<RadioGroup> mRadioGroups;

	private void addDataRows() {
		try {
			addTableRow(db.findPolicyByAppProv(database, 
					SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
					SPrivacyApplication.getConstImages()));
			addTableRow(db.findPolicyByAppProv(database, 
					SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
					SPrivacyApplication.getConstFiles()));
			addTableRow(db.findPolicyByAppProv(database, 
					SPrivacyApplication.getConstAppForWhichWeAreSettingPolicies(), 
					SPrivacyApplication.getConstContacts()));
		} catch(SQLException e) {
			SPrivacyApplication.makeToast(this, "Seems like there is no data in the database");
		}
	}
	
	private void addOnClickListener() {
		mBtnDBOps.setOnClickListener(new OnClickListener() {

			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DBOpsActivity.class);
				startActivity(intent);
			}
		});
		
		for(int i = 0; i < mToggleButtons.size(); i++) {
			mToggleButtons.get(i).setOnClickListener(new OnClickListener() {
				//Toggle Button to identify which policy was modified
				@Override
				public void onClick(View v) {
					togglePolicy(v.getId());
				}
			});
		}
		for(int i = 0; i < mRadioGroups.size(); i++) {
			mRadioGroups.get(i).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					changeAccessLevel(mRadioGroups.indexOf(group), checkedId);
				}
			});
		}
	}
	
	private void addTableRow() {
		TableRow tblRow = new TableRow(this);
		TextView mTextViewPolicyStmt = new TextView(this);
		TextView mTextViewPolicyValu = new TextView(this);
		TextView mTextViewPolicyLevl = new TextView(this);
		
		mTextViewPolicyStmt.setText(R.string.text_view_policy_conditions);
		mTextViewPolicyStmt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyStmt.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyStmt.setGravity(Gravity.CENTER);
		
		mTextViewPolicyValu.setText(R.string.text_view_policy_setting);
		mTextViewPolicyValu.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyValu.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyValu.setGravity(Gravity.CENTER);
		
		mTextViewPolicyLevl.setText(R.string.text_view_policy_level);
		mTextViewPolicyLevl.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyLevl.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyLevl.setGravity(Gravity.CENTER);
		
		tblRow.addView(mTextViewPolicyStmt);
		tblRow.addView(mTextViewPolicyValu);
		tblRow.addView(mTextViewPolicyLevl);
		mTableOfPolicies.addView(tblRow);
	}
	
	private void addTableRow(PolicyInfo aPolicyRule) {
		if(aPolicyRule!=null){
			TableRow tblRow = new TableRow(this);
			
			TextView tempViewPolcStmt = new TextView(this);
			ToggleButton tempToggleButton = new ToggleButton(this);
			
			RadioButton tempRadioButtonNoData = new RadioButton(this);
			RadioButton tempRadioButtonFakeData = new RadioButton(this);
			RadioButton tempRadioButtonAnonymizedData = new RadioButton(this);
			RadioGroup tempViewPolGroup = new RadioGroup(this);

			tempViewPolcStmt.setText(aPolicyRule.toString());
			
			tempToggleButton.setTextOn(SPrivacyApplication.getConstAccessGranted());
			tempToggleButton.setTextOff(SPrivacyApplication.getConstAccessDenied());
			tempToggleButton.setChecked(aPolicyRule.isRule());
			tempToggleButton.setId(aPolicyRule.getId());
			
			tempRadioButtonNoData.setText(R.string.radio_button_text_no_data);
			tempRadioButtonFakeData.setText(R.string.radio_button_text_fake_data);
			tempRadioButtonAnonymizedData.setText(R.string.radio_button_text_anonymous_data);

			tempViewPolGroup.setOrientation(RadioGroup.VERTICAL);
			tempViewPolGroup.addView(tempRadioButtonNoData);
			tempViewPolGroup.addView(tempRadioButtonFakeData);
			tempViewPolGroup.addView(tempRadioButtonAnonymizedData);
			tempViewPolGroup.setEnabled(false);
			
			tblRow.addView(tempViewPolcStmt);
			tblRow.addView(tempToggleButton);
			tblRow.addView(tempViewPolGroup);
			mTableOfPolicies.addView(tblRow);
			
			mRadioGroups.add(tempViewPolGroup);
			mToggleButtons.add(tempToggleButton);
		}
	}

	private void instantiateViews() {
		mBtnDBOps = (Button) findViewById(R.id.btnDBOps);
		mTableOfPolicies = (TableLayout) findViewById(R.id.tableOfPolicies);
		addTableRow();
		addDataRows();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		mToggleButtons = new ArrayList<ToggleButton>();
		mRadioGroups = new ArrayList<RadioGroup>();
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		instantiateViews();
		addOnClickListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_rule_chooser, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		database = db.getWritableDatabase();
	}
	
	private void togglePolicy(int idOfPolicy) {
		SPrivacyApplication.makeToast(this, "The id of policy is "+Integer.toString(idOfPolicy));
		PolicyInfo tempPolicyRule = db.findPolicyByID(database, idOfPolicy);
		tempPolicyRule.togglePolicy();
		db.updatePolicyRule(database, tempPolicyRule);
		if(!tempPolicyRule.isRule())
			mRadioGroups.get(idOfPolicy).setEnabled(true);
		else
			mRadioGroups.get(idOfPolicy).setEnabled(false);
	}
	private void changeAccessLevel(int idOfPolicy, int accessLevel) {
		SPrivacyApplication.makeToast(this, "checked radio button is "+Integer.toString(accessLevel)
				+" and the id of policy is "+Integer.toString(idOfPolicy));
		PolicyInfo tempPolicyRule = db.findPolicyByID(database, idOfPolicy);
		tempPolicyRule.changeAccessLevel(accessLevel);
		db.updatePolicyRule(database, tempPolicyRule);
		if(!tempPolicyRule.isRule())
			mRadioGroups.get(idOfPolicy).setEnabled(true);
		else
			mRadioGroups.get(idOfPolicy).setEnabled(false);
	}
}