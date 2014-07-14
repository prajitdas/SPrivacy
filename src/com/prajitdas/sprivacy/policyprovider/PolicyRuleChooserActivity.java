package com.prajitdas.sprivacy.policyprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.policyprovider.util.PolicyQuery;
import com.prajitdas.sprivacy.policyprovider.util.PolicyRules;

public class PolicyRuleChooserActivity extends Activity {
	private TextView mLargeTextViewContactsAccessPolicy;
	private ToggleButton mToggleButtonContactsAccessPolicy;
	private Button mButtonShow;
	private Button mButtonDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		
		instantiateViews();
		loadDefaultPoliciesIntoDB();
		addOnClickListener();
	}

	private void instantiateViews() {
		mLargeTextViewContactsAccessPolicy = (TextView) findViewById(R.id.textViewContactsAccessPolicy);
		mToggleButtonContactsAccessPolicy = (ToggleButton) findViewById(R.id.toggleButtonContactsAccessPolicy);
		mButtonShow = (Button) findViewById(R.id.btnShow);
		mButtonDelete = (Button) findViewById(R.id.btnDelete);
		
		mLargeTextViewContactsAccessPolicy.setText(R.string.text_view_contacts_access_policy_text);
	}

	private void addOnClickListener() {
		mToggleButtonContactsAccessPolicy.setOnClickListener(new OnClickListener() {
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
		
		mButtonDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAllPolicies();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_chooser, menu);
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
	
	private void deleteAllPolicies() {
		// delete all the records and the table of the database provider
		int count = getContentResolver().delete(PolicyQuery.baseUri, null, null);
		String display = "Policies deleted = "+ count;
		Log.v(SPrivacyApplication.getDebugTag(), display);
		SPrivacyApplication.makeToast(this, display);
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
	
	private void loadDefaultPoliciesIntoDB() {
		DefaultPolicyLoader defaultPolicy = new DefaultPolicyLoader();
		for (PolicyRules defaultPolicyRule : defaultPolicy.getDefaultPolicies()) {
			// Add a new policy record
			ContentValues values = new ContentValues();
		
		    values.put(PolicyProvider.getAppname(), defaultPolicyRule.getAppName());	    
		    values.put(PolicyProvider.getResource(), defaultPolicyRule.getResource());
		    if(defaultPolicyRule.isPolicyRule())
		    	values.put(PolicyProvider.getPolicy(), 1);
		    else
		    	values.put(PolicyProvider.getPolicy(), 0);
			try {
			    getContentResolver().insert(PolicyQuery.baseUri, values);
			} catch(SQLException sqlE){
				SPrivacyApplication.makeToast(this, sqlE.getMessage());
			}
		}
	}
}