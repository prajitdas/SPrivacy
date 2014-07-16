package com.prajitdas.sprivacy.policyprovider;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policyprovider.util.PolicyQuery;
import com.prajitdas.sprivacy.policyprovider.util.PolicyRule;

public class PolicyRuleChooserActivity extends Activity {
	private TableLayout mTableOfPolicies;
	private Button mButtonShow;
	private ArrayList<PolicyRule> listOfPolicyRules;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		getAllPolicies();
		
		instantiateViews();
		addOnClickListener();
	}

	private void instantiateViews() {
		mButtonShow = (Button) findViewById(R.id.btnShow);
		mTableOfPolicies = (TableLayout) findViewById(R.id.tableOfPolicies);

		TableRow tblRowHeader  = new TableRow(this);
		TextView mTextViewPolicyStmtHeader = new TextView(this);
		TextView mTextViewPolicyValueHeader = new TextView(this);
		
		mTextViewPolicyStmtHeader.setText(R.string.text_view_access_policy_text);
		mTextViewPolicyStmtHeader.setTextAppearance(this, android.R.style.TextAppearance_Large);
		mTextViewPolicyStmtHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
		
		mTextViewPolicyValueHeader.setText(R.string.text_view_policy_value_text);
		mTextViewPolicyValueHeader.setTextAppearance(this, android.R.style.TextAppearance_Large);
		mTextViewPolicyValueHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
		
		tblRowHeader.addView(mTextViewPolicyStmtHeader);
		tblRowHeader.addView(mTextViewPolicyValueHeader);
		mTableOfPolicies.addView(tblRowHeader);
		
//		for()
//
//		if(isDataAccessAllowed(1))
//			mToggleBtnAccessPolicy.setChecked(true);
//		else
//			mToggleBtnAccessPolicy.setChecked(false);
//
//		mLargeTextViewAccessPolicy.setText(R.string.text_view_access_policy_text);
	}

	private void addOnClickListener() {
		mButtonShow.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllPoliciesActivity.class);
				intent.putExtra("com.prajitdas.sprivacy.policyprovider", listOfPolicyRules);
				startActivity(intent);
			}
		});
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
	
	private void getAllPolicies() {
		listOfPolicyRules = new ArrayList<PolicyRule>(); 
		// Get all the policies sorted by app name
		Cursor c = getContentResolver().query(PolicyQuery.baseUri, 
				PolicyQuery.projection, 
				PolicyQuery.selection, 
				PolicyQuery.selectionArgs, 
				PolicyQuery.sort);
		
		if (c.moveToFirst()) {
			PolicyRule temp = new PolicyRule();
			do {
				temp.setId(Integer.parseInt(c.getString(c.getColumnIndex(PolicyProvider.getId()))));
				temp.setAppName(c.getString(c.getColumnIndex(PolicyProvider.getAppname())));
				temp.setResource(c.getString(c.getColumnIndex(PolicyProvider.getResource())));
				if(c.getString(c.getColumnIndex(PolicyProvider.getPolicy())).equals("1"))
					temp.setPolicyRule(true);
				else
					temp.setPolicyRule(false);
				listOfPolicyRules.add(temp);
			} while (c.moveToNext());
		}
	}
	
	private void togglePolicy(int idOfPolicy) {
		//Get a single policy to modify		
		Cursor c = getContentResolver().query(PolicyQuery.baseUri, 
				PolicyQuery.projection, 
				Integer.toString(idOfPolicy), 
				PolicyQuery.selectionArgs, 
				PolicyQuery.sort);
		if (!c.moveToFirst()) {
			SPrivacyApplication.makeToast(this, "Well, could not find the particular id!");
		}
		else {				
			ContentValues values = new ContentValues();
	
			values.put(PolicyProvider.getAppname(), c.getString(c.getColumnIndex(PolicyProvider.getAppname())));
		    values.put(PolicyProvider.getResource(), c.getString(c.getColumnIndex(PolicyProvider.getResource())));
		    if(c.getString(c.getColumnIndex(PolicyProvider.getPolicy())).equals("1"))
		    	values.put(PolicyProvider.getPolicy(), 0);
		    else
		    	values.put(PolicyProvider.getPolicy(), 1);
		    getContentResolver().update(PolicyQuery.baseUri, values, Integer.toString(idOfPolicy), null);	
			SPrivacyApplication.makeToast(this, "Updated: "+values.toString());
		}
	}
	
	private boolean isDataAccessAllowed(int idOfPolicy) {
		//Get a single policy to modify		
		Cursor c = getContentResolver().query(PolicyQuery.baseUri, 
				PolicyQuery.projection, 
				Integer.toString(idOfPolicy), 
				PolicyQuery.selectionArgs, 
				PolicyQuery.sort);
		if (!c.moveToFirst()) {
			SPrivacyApplication.makeToast(this, "Well, could not find the particular id setting default true!");
			return true;
		}
		c.moveToFirst();
	    if(c.getString(c.getColumnIndex(PolicyProvider.getPolicy())).equals("1"))
	    	return true;
		return false;
	}
}