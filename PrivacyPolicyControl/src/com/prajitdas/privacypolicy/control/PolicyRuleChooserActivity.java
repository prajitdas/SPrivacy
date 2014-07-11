package com.prajitdas.privacypolicy.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prajitdas.privacypolicy.PrivacyPolicyApplication;
import com.prajitdas.privacypolicy.R;
import com.prajitdas.privacypolicy.provider.util.ApplicationPolicy;
import com.prajitdas.privacypolicy.provider.util.DefaultPolicyLoader;

public class PolicyRuleChooserActivity extends Activity {
	private TextView mLargeTextViewContactsAccessPolicy;
	private ToggleButton mToggleButtonContactsAccessPolicy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_chooser);
		DefaultPolicyLoader.loadDefaultPolicies();
		
		//Right now adding simple strings for the applications's info 
		//and policies eventually has to be objects the design needs to be done for that
		PrivacyPolicyApplication.getApplicationsInfo().getPolicies().add(
				new ApplicationPolicy(0,"contentparser", "contacts", false));
		
		mLargeTextViewContactsAccessPolicy = (TextView) findViewById(R.id.textViewContactsAccessPolicy);
		mToggleButtonContactsAccessPolicy = (ToggleButton) findViewById(R.id.toggleButtonContactsAccessPolicy);
		
		mLargeTextViewContactsAccessPolicy.setText(R.string.text_view_contacts_access_policy_text);
		
		addOnClickListener();
	}

	private void addOnClickListener() {
		mToggleButtonContactsAccessPolicy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//This is get(0) as there is only one application now
				PrivacyPolicyApplication.getApplicationsInfo().getPolicies().get(0).togglePolicy();
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
}