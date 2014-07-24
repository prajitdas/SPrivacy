package com.prajitdas.sprivacy.policymanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.prajitdas.sprivacy.R;
import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.PolicyDBHelper;

public class DBOpsActivity extends Activity {
	private Button mBtnShowAllPolicies;
	private Button mBtnShowAllApplications;
	private Button mBtnShowAllProviders;
	private Button mBtnDelData;
	private PolicyDBHelper db;
	private SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbops);
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
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
	
	private void addOnClickListener() {
		mBtnShowAllPolicies.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllPoliciesActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnShowAllApplications.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllApplicationsActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnShowAllProviders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DisplayAllProvidersActivity.class);
				startActivity(intent);
			}
		});
		
		mBtnDelData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        // Use the Builder class for convenient dialog construction
		        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		        builder.setMessage(R.string.dialog_delete_data)
		               .setPositiveButton(R.string.dialog_resp_delete, new DialogInterface.OnClickListener() {
		            	   public void onClick(DialogInterface dialog, int id) {
		            		   db.deleteAllData(database);
		            	   }
		               })
		               .setNegativeButton(R.string.dialog_resp_NO, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   SPrivacyApplication.makeToast(getApplicationContext(), "Data was not deleted!");
		                   }
		               });

				// create alert dialog
				AlertDialog alertDialog = builder.create();

				// show it
				alertDialog.show();
			}
		});
	}

	private void instantiateViews() {
		mBtnShowAllPolicies = (Button) findViewById(R.id.btnShowPols);
		mBtnShowAllApplications = (Button) findViewById(R.id.btnShowApps);
		mBtnShowAllProviders = (Button) findViewById(R.id.btnShowPros);
		mBtnDelData = (Button) findViewById(R.id.btnDelData);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbops, menu);
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