package com.prajitdas.sprivacy.policyprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policyprovider.util.PolicyQuery;

public class PolicyRuleChooserActivity extends Activity {
	private TextView mLargeTextViewAccessPolicy;
	private ToggleButton mToggleBtnAccessPolicy;
	private Button mButtonShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		
		instantiateViews();
		addOnClickListener();
	}

	private void instantiateViews() {
		mLargeTextViewAccessPolicy = (TextView) findViewById(R.id.textViewAccessPolicy);
		mToggleBtnAccessPolicy = (ToggleButton) findViewById(R.id.toggleBtnAccessPolicy);
		mButtonShow = (Button) findViewById(R.id.btnShow);
		
		if(isDataAccessAllowed(1))
			mToggleBtnAccessPolicy.setChecked(true);
		else
			mToggleBtnAccessPolicy.setChecked(false);

		mLargeTextViewAccessPolicy.setText(R.string.text_view_access_policy_text);
	}

	private void addOnClickListener() {
		mToggleBtnAccessPolicy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//This is get(0) as there is only one application now
				togglePolicy(1);
			}
		});
		
		mButtonShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAllPolicies();			
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
	
	private void showAllPolicies() {
		// Show all the policies sorted by app name
		Cursor c = getContentResolver().query(PolicyQuery.baseUri, 
				PolicyQuery.projection, 
				PolicyQuery.selection, 
				PolicyQuery.selectionArgs, 
				PolicyQuery.sort);
		String result = "Results:";

		if (!c.moveToFirst()) {
			SPrivacyApplication.makeToast(this, result+" no content yet!");
		}
		else {
			do {
				result = result + "\n" + c.getString(c.getColumnIndex(PolicyProvider.getAppname())) +
						" trying to access resource " +  c.getString(c.getColumnIndex(PolicyProvider.getResource())) + 
								" has policy set as : " + c.getString(c.getColumnIndex(PolicyProvider.getPolicy()));
			} while (c.moveToNext());
			SPrivacyApplication.makeToast(this, result);
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